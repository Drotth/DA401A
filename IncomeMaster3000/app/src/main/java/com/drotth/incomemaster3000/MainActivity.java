package com.drotth.incomemaster3000;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    FragmentManager fm;
    IncomesFragment incomes;
    ExpensesFragment expenses;
    SummaryFragment summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getFragmentManager();
        incomes = new IncomesFragment();
        expenses = new ExpensesFragment();
        summary = new SummaryFragment();

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, incomes);
        ft.commit();
    }

@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        switch (item.getItemId()){
            case R.id.menu_incomes:
                ft.replace(R.id.container, incomes);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;

            case R.id.menu_expenses:
                ft.replace(R.id.container, expenses);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;

            case R.id.menu_summary:
                ft.replace(R.id.container, summary);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addEntry(String entry){
        AddEntryFragment addEntry = AddEntryFragment.newInstance(entry);
        fm.beginTransaction()
                .replace(R.id.container, addEntry)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
