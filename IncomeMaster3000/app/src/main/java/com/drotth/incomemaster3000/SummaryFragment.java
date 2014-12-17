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
    private TextView totalAmountIncomes, totalAmountExpenses, totalDiff;
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

        totalAmountExpenses = (TextView)myInflater.findViewById(R.id.textView_sumExpenses);
        totalAmountIncomes= (TextView)myInflater.findViewById(R.id.textView_sumIncomes);
        totalDiff=(TextView)myInflater.findViewById(R.id.textView_sumDiff);

        Button button = (Button) myInflater.findViewById(R.id.button_summary);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalAmountExpenses.setText("Expenses:\t"+database.getExpensesSum());
                totalAmountIncomes.setText("Incomes:\t"+database.getIncomesSum());
                int totalBudget = database.getIncomesSum()-database.getExpensesSum();
                totalDiff.setText("Total relation:\t"+totalBudget);
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
