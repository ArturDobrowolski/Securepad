package com.example.securepad;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements PasswordDialog.PasswordDialogListener {
    private static final String FILE_NAME = "/data/data/com.example.securepad/files/note.enc";
    private static final String PASS_FILE = "/data/data/com.example.securepad/files/pass";

    private Button button;
    private Secret secret = new Secret();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfPassExists();
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void openSetPass() {
        Intent intent = new Intent(this, SetPassActivity.class);
        startActivity(intent);
    }

    private void openDialog() {
        PasswordDialog passwordDialog = new PasswordDialog();
        passwordDialog.show(getSupportFragmentManager(), "Password dialog");
    }

    private void openNotepadActivity() {
        Intent intent = new Intent(this, NotepadActivity.class);
        startActivity(intent);
    }

    @Override
    public void checkPass(String inputPass) throws IOException {

        File passFile = new File(PASS_FILE);
        BufferedReader br = new BufferedReader(new FileReader(passFile));
        StringBuilder sb = new StringBuilder();
        String stringPass;
        String hashedInputPass = secret.hashPass(inputPass);

        while ((stringPass = br.readLine()) != null) {
            sb.append(stringPass);
        }

        // Debug
        System.out.println("Input pass plain: " + inputPass);
        System.out.println("Pass from file: " + sb);
        System.out.println("Hashed input pass: " + hashedInputPass);

        if (hashedInputPass.equals(sb.toString())) {
            openNotepadActivity();
        }
        else {
            Toast.makeText(this, "Hasło niepoprawne!", Toast.LENGTH_LONG).show();
        }
        //textViewPassword.setText(password);
    }

    private void checkIfPassExists() {
        File passFile = new File(PASS_FILE);
        boolean exists = passFile.isFile();
        System.out.println(exists);
        System.out.println(PASS_FILE);
        if(!passFile.isFile()) {
            Toast.makeText(this,
                    "Proszę ustawić pierwsze hasło.",
                    Toast.LENGTH_LONG).show();
            openSetPass();
        }
    }
}