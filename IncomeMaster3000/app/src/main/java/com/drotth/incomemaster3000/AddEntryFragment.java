package com.drotth.incomemaster3000;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddEntryFragment extends Fragment {

    private String entry;
    private EditText date, title, amount;
    private DatabaseController database;

    public static AddEntryFragment newInstance(String entry) {
        AddEntryFragment fragment = new AddEntryFragment();
        Bundle args = new Bundle();
        args.putString("entry", entry);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DatabaseController(getActivity());
        if (getArguments() != null) {
            entry = getArguments().getString("entry");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        database.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_new_entry, container, false);

        amount = (EditText) root.findViewById(R.id.new_entry_amount);
        date = (EditText) root.findViewById(R.id.new_entry_date);
        title = (EditText) root.findViewById(R.id.new_entry_title);

        Button button = (Button) root.findViewById(R.id.create_entry);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAmount = amount.getText().toString();
                String newDate = date.getText().toString();
                String newTitle = title.getText().toString();

                database.newData(entry, newTitle, newAmount, newDate);
                getFragmentManager().popBackStack();
            }
        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        database.open();
    }
}
