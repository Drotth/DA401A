package com.drotth.grumpychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class ChatsArrayAdapter<Object> extends ArrayAdapter<Object> {

    private final Context context;
    private final ArrayList<Object> values;

    public ChatsArrayAdapter(Context context, ArrayList<Object> values) {
        super(context, R.layout.chats_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.chats_item, parent, false);
        TextView msgView = (TextView) view.findViewById(R.id.Msg);
        TextView timeView = (TextView) view.findViewById(R.id.MsgTimeFrom);
        msgView.setText(((ChatMessage)values.get(position)).getMessage());
        timeView.setText(((ChatMessage)values.get(position)).getFrom() + ", " +
                ((ChatMessage)values.get(position)).getTimestamp());

        Firebase firebase = new Firebase((String) context.getResources().getText(R.string.firebase_url));
        String user = firebase.getAuth().getProviderData().get("email").toString();

        if( ((ChatMessage)values.get(position)).getFrom().equals(user) ) {
            msgView.setGravity(Gravity.RIGHT);
            timeView.setGravity(Gravity.RIGHT);
        }
        return view;
    }

    public ChatMessage getItemById(String id){
        ChatMessage msg = null;

        for (int i = 0; i < values.size(); i++){
            msg = (ChatMessage) values.get(i);
            if (msg.getId().equals(id)){
                break;
            }
        }
        return msg;
    }

    //TODO: Makes scrolling much smoother but the text positioning doesn't work.
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder = new ViewHolder();
//
//        if (convertView == null){
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            convertView = inflater.inflate(R.layout.chats_item, parent, false);
//            viewHolder.text = (TextView) convertView.findViewById(R.id.Msg);
//            viewHolder.timeFrom = (TextView) convertView.findViewById(R.id.MsgTimeFrom);
//
//            convertView.setTag(viewHolder);
//        }
//        else{
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        viewHolder.text.setText(((ChatMessage)values.get(position)).getMessage());
//        viewHolder.timeFrom.setText(((ChatMessage)values.get(position)).getFrom() + ", " +
//                ((ChatMessage)values.get(position)).getTimestamp());
//
//        Firebase firebase = new Firebase((String) context.getResources().getText(R.string.firebase_url));
//        String user = firebase.getAuth().getProviderData().get("email").toString();
//
//        if( ((ChatMessage)values.get(position)).getFrom().equals(user) ) {
//            viewHolder.text.setGravity(Gravity.END);
//            viewHolder.timeFrom.setGravity(Gravity.END);
//        }
//        return convertView;
//    }
//
//    static class ViewHolder {
//        TextView text;
//        TextView timeFrom;
//    }
}
