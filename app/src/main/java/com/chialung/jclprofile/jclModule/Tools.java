package com.chialung.jclprofile.jclModule;


import android.util.Base64;
import android.util.Log;

/**
 * Created by chialung on 2015/8/20.
 */
public class Tools {

    public static void debug(String message, int lv) {
        debug("John", message, lv);
    }

    public static void debug(String message) {
        debug("John", message, 0);
    }

    public static void debug(String tag, String message) {
        debug(tag, message, 0);
    }

    public static void debug(String tag, int message, int i) {
        debug(tag, String.valueOf(message), i);
    }

    public static void debug(String tag, String message, int i) {
        if (tag == null || message == null) {
            return;
        }

        switch (i) {
            case 0:
                Log.v(tag, message);
                break;
            case 1:
                Log.d(tag, message);
                break;
            case 2:
                Log.i(tag, message);
                break;
            case 3:
                Log.w(tag, message);
                break;
            case 4:
                Log.e(tag, message);
                break;
        }
    }

    public static String xorEncrypt(String input, String key) {
        byte[] inputBytes = input.getBytes();
        int inputSize = inputBytes.length;

        byte[] keyBytes = key.getBytes();
        int keySize = keyBytes.length - 1;

        byte[] outBytes = new byte[inputSize];
        for (int i = 0; i < inputSize; i++) {
            outBytes[i] = (byte) (inputBytes[i] ^ keyBytes[i % keySize]);
        }

        return new String(Base64.encode(outBytes, Base64.DEFAULT));
    }

    public static String xorDecrypt(String input, String key) {
        byte[] inputBytes = Base64.decode(input, Base64.DEFAULT);
        int inputSize = inputBytes.length;

        byte[] keyBytes = key.getBytes();
        int keySize = keyBytes.length - 1;

        byte[] outBytes = new byte[inputSize];
        for (int i = 0; i < inputSize; i++) {
            outBytes[i] = (byte) (inputBytes[i] ^ keyBytes[i % keySize]);
        }

        return new String(outBytes);
    }
}
