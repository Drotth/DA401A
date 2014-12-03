package com.drotth.grumpychat;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class ChatFragment extends Fragment {

    private static final String FIREBASE_URL = "https://testda401a.firebaseio.com";
    private String groupName = "Loading..";
    private String groupID = "NULL";
    private Firebase firebase;
    private ListView chatsList;
    private ArrayAdapter<ChatMessage> chatsListAdapter;
    private ArrayList<ChatMessage> chats = new ArrayList<ChatMessage>();

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

        firebase = new Firebase(FIREBASE_URL);
        chatsListAdapter = new ArrayAdapter<ChatMessage>(getActivity(),
                android.R.layout.simple_list_item_1, chats);

        //firebase.addChildEventListener(new ChildEventListener() {
        firebase.child(groupID + "/messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                chatsListAdapter.add(new ChatMessage(snapshot.getName(),
                        (String) snapshot.child("from").getValue(),
                        (String) snapshot.child("message").getValue(),
                        (String) snapshot.child("time").getValue()));
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatsList = (ListView) view.findViewById(R.id.chatFlow);
        chatsList.setAdapter(chatsListAdapter);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        ((MainActivity) getActivity()).actionBar.setTitle(groupName);
    }
}
