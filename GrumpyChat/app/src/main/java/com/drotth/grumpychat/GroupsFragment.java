package com.drotth.grumpychat;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class GroupsFragment extends Fragment implements ListView.OnItemClickListener{

    private static final String FIREBASE_URL = "https://testda401a.firebaseio.com";
    private Firebase firebase;
    private ListView groupsList;
    private ArrayAdapter<Group> groupListAdapter;
    private OnGroupsInteractionListener listListener;
    private ArrayList<Group> groups = new ArrayList<Group>();

    //Placeholder group list names
    String[] temp_groups = {"Project P2", "Exam work", "DA401A team", "Family", "My cats control me"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = new Firebase(FIREBASE_URL);

        groupListAdapter = new ArrayAdapter<Group>(getActivity(),
                android.R.layout.simple_list_item_1, groups);

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                groupListAdapter.add(new Group(snapshot.getName(), (String) snapshot.child("name").getValue()));
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        groupsList = (ListView) view.findViewById(R.id.groupsListView);
        groupsList.setAdapter(groupListAdapter);
        groupsList.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        ((MainActivity) getActivity()).actionBar.setTitle(R.string.groups);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listListener = (OnGroupsInteractionListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listListener != null) {
            listListener.onGroupClick(groupListAdapter.getItem(position));
        }
    }

    public interface OnGroupsInteractionListener {
        public void onGroupClick(Group group);
    }
}
