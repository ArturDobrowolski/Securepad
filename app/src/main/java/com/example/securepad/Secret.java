package com.example.securepad;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


import java.util.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Secret {

    private static final String PASS_FILE = "/data/data/com.example.securepad/files/pass";

    @SuppressLint("NewApi")
    protected String encryptMsg(String plainMsg) throws Exception {
        String stringPass = getPassFromFile();
        byte[] KEY = Base64.getEncoder().encode(stringPass.getBytes());
        SecretKeySpec SKS = new SecretKeySpec(KEY, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, SKS);
        byte[] content = cipher.doFinal(plainMsg.getBytes("UTF-8"));
        return new String(Base64.getMimeEncoder().encode(content));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected String decryptMsg(String encMsg) throws Exception {
        String stringPass = getPassFromFile();
        byte[] KEY = Base64.getEncoder().encode(stringPass.getBytes());
        SecretKeySpec SKS = new SecretKeySpec(KEY, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, SKS);
        try {
            byte[] content = cipher.doFinal(Base64.getMimeDecoder().decode(encMsg));
            return new String(content);
        }
        catch (BadPaddingException e) {
            return "HACKERMAN!";
        }
    }

    public String hashPass(String plainPass) {
        String hashedPass = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(plainPass.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPass = sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hashedPass;
    }

    public static String getPassFromFile() throws IOException {
        File passFile = new File(PASS_FILE);
        BufferedReader br = new BufferedReader(new FileReader(passFile));
        StringBuilder sb = new StringBuilder();
        String stringPass;

        while ((stringPass = br.readLine()) != null) {
            sb.append(stringPass);
        }
        return sb.toString();
    }
}

