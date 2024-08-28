package com.example.projectakhirvsga;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectakhirvsga.helper.DbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class IncomeEdit extends AppCompatActivity {
    EditText txtId, txtAmount, txtDate;
    Button btnSubmit, btnCancel;
    DbHelper SQLite = new DbHelper(this);
    String id, amount, date;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_income);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtId = findViewById(R.id.txt_id);
        txtAmount = findViewById(R.id.txt_amount);
        txtDate = findViewById(R.id.txt_date);
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);

        id = getIntent().getStringExtra(MainActivity.TAG_ID);
        amount = getIntent().getStringExtra(MainActivity.TAG_AMOUNT);
        date = getIntent().getStringExtra(MainActivity.TAG_DATE);

        setTitle("Edit Income");
        txtId.setText(id);
        txtAmount.setText(amount);
        txtDate.setText(date);

        final DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(IncomeEdit.this, (timeView, hourOfDay, minute) -> {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
        };

        txtDate.setOnClickListener(v -> new DatePickerDialog(IncomeEdit.this, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    edit();
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

    private void blank() {
        txtAmount.requestFocus();
        txtId.setText(null);
        txtAmount.setText(null);
        txtDate.setText(null);
    }

    private void edit() {
        if (txtAmount.getText().toString().trim().isEmpty() || txtDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please input amount or date...", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.updateIncome(Integer.parseInt(txtId.getText().toString().trim()),
                    txtAmount.getText().toString().trim(),
                    txtDate.getText().toString().trim());
            blank();
            finish();
        }
    }
}
