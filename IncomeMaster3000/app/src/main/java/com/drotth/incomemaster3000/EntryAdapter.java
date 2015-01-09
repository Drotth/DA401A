package com.drotth.incomemaster3000;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class EntryAdapter extends CursorAdapter{

    public EntryAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View root = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.Date = (TextView) root.findViewById(R.id.date);
        holder.Title = (TextView) root.findViewById(R.id.title);
        holder.Amount = (TextView) root.findViewById(R.id.amount);
        holder.ProductID = (TextView) root.findViewById(R.id.product_id);
        root.setTag(holder);

        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.Title.setText("Title:\t"+cursor.getString(3));
        holder.Date.setText("Date:\t"+cursor.getString(1));
        holder.Amount.setText("Amount:\t"+cursor.getString(2));
        holder.ProductID.setText("Product ID:\t"+cursor.getString(0));
    }

    private class ViewHolder {
        TextView Date;
        TextView Title;
        TextView Amount;
        TextView ProductID;
    }
}
