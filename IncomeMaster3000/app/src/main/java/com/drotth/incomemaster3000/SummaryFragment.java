package com.drotth.incomemaster3000;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SummaryFragment extends Fragment {
    private DatabaseController database;
    private TextView totalIncomesView, totalExpensesView, totalDiffView;
    private int totalIncomes, totalExpenses, totalBudget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DatabaseController(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        database.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_summary, container, false);

        totalExpensesView = (TextView) myInflater.findViewById(R.id.summary_expenses2);
        totalIncomesView = (TextView) myInflater.findViewById(R.id.summary_incomes2);
        totalDiffView = (TextView) myInflater.findViewById(R.id.summary_diff2);

        Button button = (Button) myInflater.findViewById(R.id.summary_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalIncomes = database.getIncomesSum();
                totalExpenses = database.getExpensesSum();

                totalIncomesView.setText(""+totalIncomes);
                totalExpensesView.setText(""+totalExpenses);
                totalBudget = totalIncomes-totalExpenses;
                totalDiffView.setText(""+totalBudget);
            }
        });

        return myInflater ;
    }

    @Override
    public void onPause() {
        super.onPause();
        database.close();
    }
}
