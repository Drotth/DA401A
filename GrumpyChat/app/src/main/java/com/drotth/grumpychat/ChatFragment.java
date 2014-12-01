package com.drotth.grumpychat;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatFragment extends Fragment {

    private String groupName = "Loading..";
    private String groupID = "NULL";

    public static ChatFragment newInstance(Group group) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("groupName", group.getName());
        args.putString("groupID", group.getId());
        fragment.setArguments(args);
        return fragment;
    }

    // Required empty public constructor
    public ChatFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupName = getArguments().getString("groupName");
            groupID = getArguments().getString("groupID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        ((MainActivity) getActivity()).actionBar.setTitle(groupName);
    }
}
