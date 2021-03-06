package com.drotth.grumpychat;

import android.os.Bundle;
import android.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabWidget;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment implements Button.OnClickListener, ListView.OnItemLongClickListener {

    private View view;
    private String groupName = "Loading..";
    private String groupID = "NULL";
    private Firebase firebase;
    private ListView chatsList;
    private ChatsArrayAdapter<ChatMessage> chatsListAdapter;
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

        firebase = new Firebase((String) getResources().getText(R.string.firebase_url)).child(groupID).child("messages");
        chatsListAdapter = new ChatsArrayAdapter<ChatMessage>(getActivity(), chats);

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
                Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                String id = snapshot.getName();
                ChatMessage msg = chatsListAdapter.getItemById(id);
                msg.setId((String) snapshot.getName());
                msg.setFrom((String) data.get("from"));
                msg.setMessage((String) data.get("message"));
                msg.setTimestamp((String) data.get("time"));
                chatsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                String id = snapshot.getName();
                chatsListAdapter.remove(chatsListAdapter.getItemById(id));
                chatsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatsList = (ListView) view.findViewById(R.id.chatFlow);
        chatsList.setAdapter(chatsListAdapter);
        chatsList.setOnItemLongClickListener(this);

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
        if(message.isEmpty()){
            Toast.makeText(getActivity(), R.string.message_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        String id = firebase.push().getName();
        Time now = new Time();
        now.setToNow();
        String timestamp = now.format("%d-%m-%Y %H:%M");
        String from = firebase.getAuth().getProviderData().get("email").toString();

        Map<String, Object> chatMessages = new HashMap<String, Object>();
        Map<String, Object> chatMsg = new HashMap<String, Object>();
        chatMsg.put("from", from);
        chatMsg.put("message", message);
        chatMsg.put("time", timestamp);
        chatMessages.put(id, chatMsg);

        firebase.updateChildren(chatMessages);
        chatMessageInput.setText("");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getActivity(), "hehe " + position, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(), ((ChatMessage)chatsListAdapter.getItem(position)).getMessage(), Toast.LENGTH_SHORT).show();
        ChatMessage msg = (ChatMessage)chatsListAdapter.getItem(position);
        firebase.child(msg.getId()).setValue(null);
        chatsListAdapter.notifyDataSetChanged();
        return false;
    }
}
