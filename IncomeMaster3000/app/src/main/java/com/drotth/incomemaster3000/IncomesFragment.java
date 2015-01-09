package com.drotth.incomemaster3000;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class IncomesFragment extends Fragment {

    private ListView incomesView;
    private EntryAdapter adapter;
    private DatabaseController database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DatabaseController(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        database.open();

        Cursor c = database.getData("Incomes");
        adapter = new EntryAdapter(getActivity(), c, true);
        incomesView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        database.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.fragment_incomes, container, false);

        incomesView = (ListView) root.findViewById(R.id.listViewIncomes);
        incomesView.setAdapter(adapter);

        Button button = (Button) root.findViewById(R.id.add_income);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addEntry("income");
            }
        });

        return root;
    }
}