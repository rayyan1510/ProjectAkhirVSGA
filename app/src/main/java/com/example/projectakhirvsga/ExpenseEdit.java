package com.example.projectakhirvsga;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectakhirvsga.helper.DbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseEdit extends AppCompatActivity {
    EditText txtId, txtAmount, txtCategory, txtDate;
    Button btnSubmit, btnCancel;
    DbHelper SQLite = new DbHelper(this);
    String id, amount, category, date;
    final Calendar myCalendar = Calendar.getInstance();
    String[] categories = {"Food", "Shop", "Transport", "Utilities", "Entertainment", "Healthcare", "Education", "Tax"};
    boolean[] selectedCategories = new boolean[categories.length];
    ArrayList<Integer> categoryList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtId = findViewById(R.id.txt_id);
        txtAmount = findViewById(R.id.txt_amount);
        txtCategory = findViewById(R.id.txt_category);
        txtDate = findViewById(R.id.txt_date);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);

        // Set input type for amount to numberDecimal
        txtAmount.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        id = getIntent().getStringExtra(MainActivity.TAG_EXP_ID);
        amount = getIntent().getStringExtra(MainActivity.TAG_EXP_AMOUNT);
        category = getIntent().getStringExtra(MainActivity.TAG_EXP_CATEGORY);
        date = getIntent().getStringExtra(MainActivity.TAG_EXP_DATE);

        setTitle("Edit Expense");

        txtId.setText(id);
        txtAmount.setText(amount);
        txtCategory.setText(category);
        txtDate.setText(date);

        // Date and time picker
        final DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(ExpenseEdit.this, (timeView, hourOfDay, minute) -> {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
        };

        txtDate.setOnClickListener(v -> new DatePickerDialog(ExpenseEdit.this, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        // Multi-choice category selection
        txtCategory.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseEdit.this);
            builder.setTitle("Select Categories");
            builder.setMultiChoiceItems(categories, selectedCategories, (dialog, which, isChecked) -> {
                if (isChecked) {
                    if (!categoryList.contains(which)) {
                        categoryList.add(which);
                    }
                } else {
                    categoryList.remove((Integer) which);
                }
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < categoryList.size(); i++) {
                    stringBuilder.append(categories[categoryList.get(i)]);
                    if (i != categoryList.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }
                txtCategory.setText(stringBuilder.toString());
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.setNeutralButton("Clear All", (dialog, which) -> {
                for (int i = 0; i < selectedCategories.length; i++) {
                    selectedCategories[i] = false;
                    categoryList.clear();
                    txtCategory.setText("");
                }
            });

            builder.show();
        });

        btnSubmit.setOnClickListener(v -> {
            try {
                edit();
            } catch (Exception e) {
                Log.e("Submit", e.toString());
            }
        });

        btnCancel.setOnClickListener(v -> {
            blank();
            finish();
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd HH:mm"; // In which you need to put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        txtDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            blank();
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void blank() {
        txtAmount.requestFocus();
        txtId.setText(null);
        txtAmount.setText(null);
        txtCategory.setText(null);
        txtDate.setText(null);
    }

    public void edit() {
        if (txtAmount.getText().toString().trim().isEmpty() || txtCategory.getText().toString().trim().isEmpty() ||
                txtDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please input amount, category, and date ...", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.updateExpense(Integer.parseInt(txtId.getText().toString().trim()),
                    txtAmount.getText().toString().trim(),
                    txtCategory.getText().toString().trim(),
                    txtDate.getText().toString().trim());
            blank();
            finish();
        }
    }
}
