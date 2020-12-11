package com.findthebusiness.backend.utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class BasicEncrypt {
    private static final String key = System.getenv("CRYPT_KEY") == null ? "Bar12345Bar12345" : System.getenv("CRYPT_KEY");
    private static final Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
    private static Cipher cipher = null;

    static {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public BasicEncrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    public static byte[] encrypt(String text) {
        try {
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());

            return encrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decrypt(byte[] text) {
        try {
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(text));
            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
