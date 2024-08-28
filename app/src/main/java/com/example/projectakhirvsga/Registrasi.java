package com.example.projectakhirvsga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectakhirvsga.helper.DbHelper;

public class Registrasi extends AppCompatActivity {

    private EditText emailEditTextRegister;
    private EditText usernameEditTextRegister;
    private EditText passwordEditTextRegister;
    private TextView loginTextView;
    private Button registerButton;

    DbHelper SQLite = new DbHelper(this);

    EditText[] ets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrasi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRegisterScreens), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailEditTextRegister = findViewById(R.id.etEmailRegsiter);
        usernameEditTextRegister = findViewById(R.id.etUserameRegsiter);
        passwordEditTextRegister = findViewById(R.id.etPasswordRegsiter);
        registerButton = findViewById(R.id.btnRegister);
        loginTextView = findViewById(R.id.txtInfo2Login);

        /* ketika button register di tekan jalankan fungsi register*/
        registerButton.setOnClickListener(v -> {
            /*cek validasi*/
            if (isValidation()) {
                /*jika valid jalankan fungsi register*/
                register();
            }
            else {
                Toast.makeText(this, "Mohon Lengkapi Seluruh Data", Toast.LENGTH_SHORT).show();
                blank();
            }
        });

        /*mengumpulkan semua data dari text field*/
        ets = new EditText[]{
                emailEditTextRegister,
                usernameEditTextRegister,
                passwordEditTextRegister,
        };

        /*FUNGSI jika sudah punya akun*/
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle register logic here
                Intent intent = new Intent(Registrasi.this, Login.class);
                startActivity(intent);
                // Toast.makeText(Registrasi.this, "Sudah punya akun clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*fungsi check validasi jika teks field tidak kosong*/
    boolean isValidation(){
        for (EditText et : ets)
            if (et.getText().toString().isEmpty())
                return false;
        return true;
    }

    // Kosongkan semua Edit Teks
    private void blank() {
        emailEditTextRegister.requestFocus();
        emailEditTextRegister.setText(null);
        passwordEditTextRegister.setText(null);
        usernameEditTextRegister.setText(null);
    }

    /* fungsi register untuk insert data user ke dalam tabel user*/
    private void register(){
        /* check jika ada yang memasukkan username yang sama*/
        if (SQLite.checkRegisterUser(usernameEditTextRegister.getText().toString())){
            Toast.makeText(this, "User telah terdaftar!. Silahkan buat user baru!", Toast.LENGTH_SHORT).show();
            blank();
            return;
        }else{
            /*jika tidak ada yg kosong di form register lakukan proses registrasi*/
            boolean regitrasiSukses = SQLite.registerUser(usernameEditTextRegister.getText().toString(), emailEditTextRegister.getText().toString(), passwordEditTextRegister.getText().toString());
            if(regitrasiSukses){
                Toast.makeText(this, "Registrasi berhasil!. Silahkan login!", Toast.LENGTH_SHORT).show();
                blank();
                finish();
            }else {
                Toast.makeText(this, "Registrasi gagal!. Silahkan coba lagi!", Toast.LENGTH_SHORT).show();
                blank();
                finish();
            }
        }

    }
}