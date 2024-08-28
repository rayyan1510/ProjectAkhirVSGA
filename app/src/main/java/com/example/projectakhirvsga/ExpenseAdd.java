package com.example.projectakhirvsga;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectakhirvsga.helper.DbHelper;

public class ExpenseAdd extends AppCompatActivity {
    EditText txtAmount, txtDate, txtCategory;
    Button btnSubmit, btnCancel;
    DbHelper SQLite = new DbHelper(this);
    Calendar myCalendar = Calendar.getInstance();
    boolean[] selectedCategories;
    ArrayList<Integer> categoryList = new ArrayList<>();
    String[] categories = {"Food", "Shop", "Transport", "Utilities", "Entertainment", "Healthcare", "Education", "Tax"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtAmount = findViewById(R.id.txt_amount);
        txtCategory = findViewById(R.id.txt_category);
        txtDate = findViewById(R.id.txt_date);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);

        setTitle("Add Expense");

        // Setup categories
        selectedCategories = new boolean[categories.length];

        // Set current date and time as default
        updateLabel();

        // Setup date and time picker
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ExpenseAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        new TimePickerDialog(ExpenseAdd.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                myCalendar.set(Calendar.MINUTE, minute);
                                updateLabel();
                            }
                        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Setup category picker
        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseAdd.this);
                builder.setTitle("Select Categories");
                builder.setMultiChoiceItems(categories, selectedCategories, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            categoryList.add(which);
                        } else {
                            categoryList.remove((Integer.valueOf(which)));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < categoryList.size(); i++) {
                            stringBuilder.append(categories[categoryList.get(i)]);
                            if (i != categoryList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        txtCategory.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedCategories.length; i++) {
                            selectedCategories[i] = false;
                            categoryList.clear();
                            txtCategory.setText("");
                        }
                    }
                });

                builder.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    save();
                } catch (Exception e) {
                    Log.e("Submit", e.toString());
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blank();
                finish();
            }
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

    public void blank() {
        txtAmount.requestFocus();
        txtAmount.setText(null);
        txtCategory.setText(null);
        txtDate.setText(null);
    }

    public void save() {
        if (txtAmount.getText().toString().trim().isEmpty() || txtCategory.getText().toString().trim().isEmpty() || txtDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please input name, amount, category, and date ...", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.insertExpense(
                    txtAmount.getText().toString().trim(),
                    txtCategory.getText().toString().trim(),
                    txtDate.getText().toString().trim()
            );
            blank();
            finish();
        }
    }
}