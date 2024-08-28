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

public class Login extends AppCompatActivity {

    private EditText usernameEditTextLogin;
    private EditText passwordEditTextLogin;
    private TextView registerTextViewLogin;
    private Button loginButtonLogin;

    DbHelper SQLite = new DbHelper(this);

    EditText[] ets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLoginScreens), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditTextLogin = findViewById(R.id.etUserameLogin);
        passwordEditTextLogin = findViewById(R.id.etPasswordLogin);
        loginButtonLogin = findViewById(R.id.buttonLogin);
        registerTextViewLogin = findViewById(R.id.txtInfo2);

        /* ketika button login di tekan jalankan fungsi login*/
        loginButtonLogin.setOnClickListener(v -> {
            /*cek validasi*/
            if (isValidation()) {
                /*jika berhasil jalankan fungsi login*/
                login();
            }
            else {
                Toast.makeText(this, "Mohon Lengkapi Seluruh Data", Toast.LENGTH_SHORT).show();
                blank();
                return;
            }
        });

        /*mengumpulkan semua data dari text field*/
        ets = new EditText[]{
                usernameEditTextLogin,
                passwordEditTextLogin,
        };

        /*FUNGSI jika belum punya akun*/
        registerTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle register logic here
                Intent intent = new Intent(Login.this, Registrasi.class);
                startActivity(intent);
//                Toast.makeText(Login.this, "Belum punya akun clicked", Toast.LENGTH_SHORT).show();
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
        usernameEditTextLogin.requestFocus();
        usernameEditTextLogin.setText(null);
        passwordEditTextLogin.setText(null);
    }

    void login(){
        String username = usernameEditTextLogin.getText().toString();
        String password = passwordEditTextLogin.getText().toString();

        boolean loginSukses = SQLite.checkUser(username, password);

        /*check jika ditemukan username dan password */
        if (loginSukses) {
            // Proceed with login
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(Login.this, "Login sukses", Toast.LENGTH_SHORT).show();
            blank();
            finish();
        } else {
            /*jika tidak silahkan coba lagi*/
            Toast.makeText(Login.this, "Gagal Login! Silahkan Coba Lagi!", Toast.LENGTH_SHORT).show();
            blank();
            return;
        }
    }
}