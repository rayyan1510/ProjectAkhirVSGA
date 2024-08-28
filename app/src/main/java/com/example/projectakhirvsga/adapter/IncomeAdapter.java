package com.example.projectakhirvsga.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projectakhirvsga.R;
import com.example.projectakhirvsga.model.IncomeData;

import java.util.List;

public class IncomeAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<IncomeData> items;

    public IncomeAdapter(Activity activity, List<IncomeData> items) {
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
            convertView = inflater.inflate(R.layout.list_row_income, null);

        TextView id = convertView.findViewById(R.id.id);
        TextView amount = convertView.findViewById(R.id.amount);
        TextView date = convertView.findViewById(R.id.date);

        IncomeData income = items.get(position);

        id.setText(income.getId());
        amount.setText(income.getAmount());
        date.setText(income.getDate());

        return convertView;
    }
}
