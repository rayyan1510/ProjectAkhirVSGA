package com.example.projectakhirvsga.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projectakhirvsga.R;
import com.example.projectakhirvsga.model.ExpenseData;

import java.util.List;

public class ExpenseAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ExpenseData> items;

    public ExpenseAdapter(Activity activity, List<ExpenseData> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_expense, null);

        TextView id = convertView.findViewById(R.id.exp_id);
        TextView amount = convertView.findViewById(R.id.exp_amount);
        TextView category = convertView.findViewById(R.id.exp_category);
        TextView date = convertView.findViewById(R.id.exp_date);

        ExpenseData expense = items.get(position);

        id.setText(expense.getId());
        amount.setText(expense.getAmount());
        category.setText(expense.getCategory());
        date.setText(expense.getDate());

        return convertView;
    }
}
