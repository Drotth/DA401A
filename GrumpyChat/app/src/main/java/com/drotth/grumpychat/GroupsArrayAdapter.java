package com.drotth.grumpychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupsArrayAdapter<Object> extends ArrayAdapter<Object> {

    private final Context context;
    private final ArrayList<Object> values;

    public GroupsArrayAdapter(Context context, ArrayList<Object> values) {
        super(context, android.R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView msgView = (TextView) view.findViewById(R.id.Msg);
        TextView timeView = (TextView) view.findViewById(R.id.MsgTimeFrom);
        msgView.setText(((ChatMessage)values.get(position)).getMessage());
        timeView.setText(((ChatMessage)values.get(position)).getFrom() + ", " +
                ((ChatMessage)values.get(position)).getTimestamp());

        return view;
    }
}
