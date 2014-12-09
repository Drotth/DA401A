package com.drotth.grumpychat;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupsFragment extends Fragment implements ListView.OnItemClickListener, View.OnClickListener{

    private Firebase firebase;
    private ListView groupsList;
    private ArrayAdapter<Group> groupListAdapter;
    private ArrayList<Group> groups;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = new Firebase((String)getResources().getText(R.string.firebase_url));
        groups = new ArrayList<Group>();

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
        view = inflater.inflate(R.layout.fragment_groups, container, false);

        groupsList = (ListView) view.findViewById(R.id.groupsListView);
        groupsList.setAdapter(groupListAdapter);
        groupsList.setOnItemClickListener(this);
        Button newGroupButton = (Button) view.findViewById(R.id.btn_new_group);
        newGroupButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        ((MainActivity) getActivity()).actionBar.setTitle(R.string.groups);
    }

    @Override
    public void onClick(View v){
        EditText groupNameInput = (EditText) view.findViewById(R.id.editText_new_group);
        String groupName = groupNameInput.getText().toString();
        if(groupName.isEmpty()){
            Toast.makeText(getActivity(), R.string.name_unvalid, Toast.LENGTH_SHORT).show();
        }
        else {
            String id = firebase.push().getName();
            Map<String, Object> node = new HashMap<String, Object>();
            Map<String, Object> nodeValues = new HashMap<String, Object>();

            nodeValues.put("name", groupName);
            nodeValues.put("id", id);
            node.put(id, nodeValues);

            firebase.updateChildren(node);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((MainActivity) getActivity()).onGroupClick(groupListAdapter.getItem(position));
    }
}
