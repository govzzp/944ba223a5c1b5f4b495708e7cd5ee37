package pers.me.monday.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String generic(String origin) throws NoSuchAlgorithmException {
        var instance  = MessageDigest.getInstance("MD5");
        var stringSlice = instance.digest(origin.getBytes());
        return new BigInteger(1,stringSlice).toString(16);
    }
}
