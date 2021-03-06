package com.drotth.incomemaster3000;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class ExpensesFragment extends Fragment {

    private ListView expensesView;
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

        Cursor c = database.getData("Expenses");
        adapter = new EntryAdapter(getActivity(), c, true);
        expensesView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        database.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme_red);
        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        // inflate the layout using the cloned inflater, not default inflater
        View root = localInflater.inflate(R.layout.fragment_expenses, container, false);

        expensesView = (ListView) root.findViewById(R.id.listViewExpenses);
        expensesView.setAdapter(adapter);

        Button button = (Button) root.findViewById(R.id.add_expense);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addEntry("expense");
            }
        });

        return root;
    }
}