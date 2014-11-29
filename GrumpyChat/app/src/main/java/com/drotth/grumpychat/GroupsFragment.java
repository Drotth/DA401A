package com.drotth.grumpychat;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GroupsFragment extends Fragment implements ListView.OnItemClickListener{

    private ListView groupsList;
    private ListAdapter listAdapter;
    private OnGroupsInteractionListener listListener;

    //Placeholder group list names
    String[] temp_groups = {"Project P2", "Exam work", "DA401A team", "Family", "My cats control me"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, temp_groups);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        groupsList = (ListView) view.findViewById(R.id.groupsListView);
        groupsList.setAdapter(listAdapter);
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
            listListener.onGroupClick((String) parent.getItemAtPosition(position));
        }
    }

    public interface OnGroupsInteractionListener {
        public void onGroupClick(String groupName);
    }
}
