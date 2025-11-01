package com.web.tilotoma.utill;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Password hash করা
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // Password যাচাই করা
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
