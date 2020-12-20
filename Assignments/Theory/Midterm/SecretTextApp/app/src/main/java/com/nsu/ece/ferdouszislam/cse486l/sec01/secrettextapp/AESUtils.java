package com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *Class for message encryption/decryption based on an encryption key
 * Courtesy - <https://wajahatkarim.com/2018/08/encrypt-/-decrypt-strings-in-android/#aesutils-class>
 */
public class AESUtils {

    private static byte[] ENCRYPTION_KEY =
            // default 128 bits encryption key
            new byte[]{'n', 'e', 'e', 'd', 'S', 'i', 'x', 't', 'e', 'e', 'n', 'l', 'e', 't', 'e', 'r'};

    public static String encrypt(String cleartext)
            throws Exception {
        byte[] rawKey = getRawKey();
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    public static String decrypt(String encrypted)
            throws Exception {

        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(enc);
        return new String(result);
    }

    private static byte[] getRawKey() throws Exception {
        SecretKey key = new SecretKeySpec(ENCRYPTION_KEY, "AES");
        byte[] raw = key.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKey skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] encrypted)
            throws Exception {
        SecretKey skeySpec = new SecretKeySpec(ENCRYPTION_KEY, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    public static void setEncryptionKey(String encryptionKey) {

        // need a 16 letter(128 bits) encryption key
        while(encryptionKey.length()<16)
            encryptionKey+= "@";

        ENCRYPTION_KEY = encryptionKey.getBytes();
    }
}
