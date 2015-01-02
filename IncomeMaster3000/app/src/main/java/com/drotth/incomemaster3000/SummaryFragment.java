package com.drotth.incomemaster3000;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.Legend;

import java.util.ArrayList;

public class SummaryFragment extends Fragment {
    private DatabaseController database;
    private int totalIncomes, totalExpenses, totalBudget;
    private PieChart chart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DatabaseController(getActivity());
        database.open();
    }

    @Override
    public void onResume() {
        super.onResume();
        database.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        totalIncomes = database.getIncomesSum();
        totalExpenses = database.getExpensesSum();
        totalBudget = totalIncomes - totalExpenses;

        // Thanks to the creator; https://github.com/PhilJay/MPAndroidChart
        chart = (PieChart) view.findViewById(R.id.chart);
        String summary = "Summary: " + totalBudget + " Kr";
        chart.setCenterText(summary);
        chart.setDescription("");
        chart.setHoleColor(getResources().getColor(R.color.extraLightGrey));
        chart.setHoleRadius(60f);
        chart.setRotationEnabled(false);
        setData(totalIncomes, totalExpenses);
        chart.animateXY(1000, 1000);

        Legend legend = chart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        return view ;
    }

    @Override
    public void onPause() {
        super.onPause();
        database.close();
    }

    private void setData(int income, int expense) {

        ArrayList<Entry> y_values = new ArrayList<Entry>();
        y_values.add(new Entry((float) income, 0));
        y_values.add(new Entry((float) expense, 1));

        ArrayList<String> x_values = new ArrayList<String>();
        x_values.add("Incomes");
        x_values.add("Expenses");

        PieDataSet dataset = new PieDataSet(y_values, "");
        dataset.setSliceSpace(3f);

        int red = getResources().getColor(R.color.red);
        int green = getResources().getColor(R.color.green);
        dataset.setColors(new int[]{green, red});

        PieData data = new PieData(x_values, dataset);
        chart.setData(data);
    }
}
