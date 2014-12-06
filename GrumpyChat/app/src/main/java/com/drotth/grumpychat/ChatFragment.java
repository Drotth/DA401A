package com.drotth.grumpychat;

import android.os.Bundle;
import android.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment implements Button.OnClickListener {

    private String groupName = "Loading..";
    private String groupID = "NULL";
    private Firebase firebase;
    private ListView chatsList;
    //private ArrayAdapter<ChatMessage> chatsListAdapter;
    private ChatsArrayAdapter<ChatMessage> chatsListAdapter;
    private ArrayList<ChatMessage> chats = new ArrayList<ChatMessage>();
    private View view;

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

        firebase = new Firebase((String)getResources().getText(R.string.firebase_url)).child(groupID).child("messages");
        chatsListAdapter = new ChatsArrayAdapter<ChatMessage>(getActivity(),
                android.R.layout.simple_list_item_1, chats);

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Map<String, Object> data = (Map<String, Object>) snapshot.getValue();

                String id = snapshot.getName();
                String from = (String) data.get("from");
                String message = (String) data.get("message");
                String timestamp = (String) data.get("time");

                chatsListAdapter.add(new ChatMessage(id, from, message, timestamp));
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
                //TODO: handle firebase errors
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatsList = (ListView) view.findViewById(R.id.chatFlow);
        chatsList.setAdapter(chatsListAdapter);
        Button sendButton = (Button) view.findViewById(R.id.sendBtn);
        sendButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        ((MainActivity) getActivity()).actionBar.setTitle(groupName);
    }

    @Override
    public void onClick(View v) {
        EditText chatMessageInput = (EditText) view.findViewById(R.id.chatInputField);
        String message = chatMessageInput.getText().toString();
        String id = firebase.push().getName();

        Time now = new Time();
        now.setToNow();
        String timestamp = now.format("%d-%m-%Y %H:%M:%S");
        String from = firebase.getAuth().getProviderData().get("email").toString();

        Map<String, Object> chatMessages = new HashMap<String, Object>();
        // TODO: FirebaseException: Failed to parse node with class class com.drotth.grumpychat.ChatMessage, seems like firebase bug
        //ChatMessage cm = new ChatMessage(id, from, message, timestamp);

        Map<String, Object> chatMsg = new HashMap<String, Object>();
        chatMsg.put("from", from);
        chatMsg.put("message", message);
        chatMsg.put("time", timestamp);

        chatMessages.put(id, chatMsg);
        firebase.updateChildren(chatMessages);
    }
}
