package com.example.projectakhirvsga;

import com.example.projectakhirvsga.adapter.IncomeAdapter;
import com.example.projectakhirvsga.adapter.ExpenseAdapter;
import com.example.projectakhirvsga.helper.DbHelper;
import com.example.projectakhirvsga.model.IncomeData;
import com.example.projectakhirvsga.model.ExpenseData;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listViewIncomes, listViewExpenses;
    Button btnAddIncome, btnAddExpense, btnLogout;
    TextView txtBalance;
    AlertDialog.Builder dialog;
    List<IncomeData> incomeList = new ArrayList<>();
    List<ExpenseData> expenseList = new ArrayList<>();
    IncomeAdapter incomeAdapter;
    ExpenseAdapter expenseAdapter;
    DbHelper SQLite = new DbHelper(this); // Keep this initialization

    public static final String TAG_ID = "id";
    public static final String TAG_AMOUNT = "amount";
    public static final String TAG_DATE = "date";
    public static final String TAG_EXP_ID = "exp_id";
    public static final String TAG_EXP_AMOUNT = "exp_amount";
    public static final String TAG_EXP_CATEGORY = "exp_category";
    public static final String TAG_EXP_DATE = "exp_date";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize SQLite helper
        SQLite = new DbHelper(getApplicationContext());

        listViewIncomes = findViewById(R.id.list_view_incomes);
        listViewExpenses = findViewById(R.id.list_view_expenses);
        btnAddIncome = findViewById(R.id.button_add_income);
        btnAddExpense = findViewById(R.id.button_add_expense);
        txtBalance = findViewById(R.id.txt_balance);
        btnLogout = findViewById(R.id.button_logout); // Inisialisasi tombol logout

        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IncomeAdd.class);
                startActivity(intent);
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExpenseAdd.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        incomeAdapter = new IncomeAdapter(MainActivity.this, incomeList);
        expenseAdapter = new ExpenseAdapter(MainActivity.this, expenseList);

        listViewIncomes.setAdapter(incomeAdapter);
        listViewExpenses.setAdapter(expenseAdapter);

        listViewIncomes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final String idx = incomeList.get(position).getId();
                final String amount = incomeList.get(position).getAmount();
                final String date = incomeList.get(position).getDate();

                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(MainActivity.this, IncomeEdit.class);
                                intent.putExtra(TAG_ID, idx);
                                intent.putExtra(TAG_AMOUNT, amount);
                                intent.putExtra(TAG_DATE, date);
                                startActivity(intent);
                                break;
                            case 1:
                                SQLite.deleteIncome(Integer.parseInt(idx));
                                incomeList.clear();
                                getAllIncomeData();
                                updateBalance();
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });

        listViewExpenses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final String idx = expenseList.get(position).getId();
                final String amount = expenseList.get(position).getAmount();
                final String category = expenseList.get(position).getCategory();
                final String date = expenseList.get(position).getDate();

                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(MainActivity.this, ExpenseEdit.class);
                                intent.putExtra(TAG_EXP_ID, idx);
                                intent.putExtra(TAG_EXP_AMOUNT, amount);
                                intent.putExtra(TAG_EXP_CATEGORY, category);
                                intent.putExtra(TAG_EXP_DATE, date);
                                startActivity(intent);
                                break;
                            case 1:
                                SQLite.deleteExpense(Integer.parseInt(idx));
                                expenseList.clear();
                                getAllExpenseData();
                                updateBalance();
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });

        getAllIncomeData();
        getAllExpenseData();
        updateBalance();
    }

    private void getAllIncomeData() {
        incomeList.clear(); // Clear list before adding new data
        ArrayList<HashMap<String, String>> row = SQLite.getAllIncomeData();
        for (int i = 0; i < row.size(); i++) {
            String id = row.get(i).get(TAG_ID);
            String amount = row.get(i).get(TAG_AMOUNT);
            String date = row.get(i).get(TAG_DATE);
            IncomeData data = new IncomeData();
            data.setId(id);
            data.setAmount(amount);
            data.setDate(date);
            incomeList.add(data);
        }
        incomeAdapter.notifyDataSetChanged();
    }

    private void getAllExpenseData() {
        expenseList.clear(); // Clear list before adding new data
        ArrayList<HashMap<String, String>> row = SQLite.getAllExpenseData();
        for (int i = 0; i < row.size(); i++) {
            String id = row.get(i).get(TAG_EXP_ID);
            String amount = row.get(i).get(TAG_EXP_AMOUNT);
            String category = row.get(i).get(TAG_EXP_CATEGORY);
            String date = row.get(i).get(TAG_EXP_DATE);
            ExpenseData expense = new ExpenseData();
            expense.setId(id);
            expense.setAmount(amount);
            expense.setCategory(category);
            expense.setDate(date);
            expenseList.add(expense);
        }
        expenseAdapter.notifyDataSetChanged();
    }

    private void updateBalance() {
        double totalIncome = 0;
        double totalExpense = 0;

        for (IncomeData income : incomeList) {
            totalIncome += Double.parseDouble(income.getAmount());
        }

        for (ExpenseData expense : expenseList) {
            totalExpense += Double.parseDouble(expense.getAmount());
        }

        double balance = totalIncome - totalExpense;
        txtBalance.setText(String.format("Rp. %.2f", balance));
    }


    @Override
    protected void onResume() {
        super.onResume();
        incomeList.clear();
        expenseList.clear();
        getAllIncomeData();
        getAllExpenseData();
        updateBalance();
    }

    private void logout() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Menutup MainActivity sehingga tidak bisa kembali dengan tombol back
    }
}