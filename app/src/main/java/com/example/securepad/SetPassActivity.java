package com.example.securepad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SetPassActivity extends AppCompatActivity {

    private EditText textViewPassword1;
    private EditText textViewPassword2;
    private Button button;
    private static final String PASS_FILE = "pass";
    private Secret secret = new Secret();
    private String pass_1;
    private String pass_2;
    private String hashedPass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass);

        textViewPassword1 = (EditText) findViewById(R.id.editTextPassword1);
        textViewPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        button = (Button) findViewById(R.id.set_pass_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPass();
            }
        });
    }

    private void setPass() {
        pass_1 = textViewPassword1.getText().toString();
        pass_2 = textViewPassword2.getText().toString();

        if(pass_1.equals(pass_2)) {
            Toast.makeText(this,
                    "Hasło zmienione!", Toast.LENGTH_LONG).show();
            finish();
        }
        else
            Toast.makeText(this, "Hasła się nie zgadzają! ", Toast.LENGTH_LONG).show();

        hashedPass = secret.hashPass(pass_1);
        savePass(hashedPass);
    }

    private void savePass(String pass) {
        FileOutputStream pos = null;

        try {
            pos = openFileOutput(PASS_FILE, MODE_PRIVATE);
            pos.write(pass.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pos != null) {
                try {
                    pos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}