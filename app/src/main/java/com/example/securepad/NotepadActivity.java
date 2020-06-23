package com.example.securepad;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NotepadActivity extends AppCompatActivity {
    private static final String FILE_NAME = "note.enc";
    private static final String PASS_FILE = "pass";
    Secret secret = new Secret();

    EditText mEditText;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        mEditText = findViewById(R.id.edit_text);
    }

    public void setPass(View v) {
        Intent intent = new Intent(this, SetPassActivity.class);
        startActivity(intent);
    }

    public void save(View v) throws Exception {
        String text = mEditText.getText().toString();
        String encText = secret.encryptMsg(text);
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(encText.getBytes());

            Toast.makeText(this, "Plik zapisany " +
                    getFilesDir() +
                            "/" +
                    FILE_NAME,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void load(View v) throws Exception {
        FileInputStream fis = null;
        String decText = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            System.out.println("From LOAD dec msg: " + sb.toString());
            decText = secret.decryptMsg(sb.toString());

            if(decText.equals("HACKERMAN!")) {
                Toast.makeText(this, "Nie da rady! :D", Toast.LENGTH_LONG).show();
                mEditText.setText("HACKERMAN!\nXOXOXOXO");
            }
            else {
                mEditText.setText(decText);
            }
            //mEditText.setText(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void getPass() {
        File passFile = new File(PASS_FILE);
        BufferedReader br = null;
        String stringPass = null;
        try {
            br = new BufferedReader(new FileReader(passFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();

        while (true) {
            try {
                if (!((stringPass = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(stringPass);
        }
    }
}