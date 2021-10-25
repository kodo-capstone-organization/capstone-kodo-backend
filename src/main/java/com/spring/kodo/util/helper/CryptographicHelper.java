package com.spring.kodo.util.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class CryptographicHelper
{
    private static final char[] HEX_STRINGS = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String getSHA256Digest(String plainText, String salt)
    {
        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }

        byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
        md.update(saltBytes);
        byte[] hashDigestWithSalt = md.digest(plainText.getBytes(StandardCharsets.UTF_8));
        String hex = bytesToHex(hashDigestWithSalt);

        return hex;
    }

    public static String generateRandomString(int len)
    {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++)
        {
            sb.append(HEX_STRINGS[random.nextInt(HEX_STRINGS.length)]);
        }
        return sb.toString();
    }

    private static String bytesToHex(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
        {
            sb.append(byteToHex(b));
        }
        return sb.toString().toUpperCase();
    }

    private static String byteToHex(byte num)
    {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
}
