package com.ImperioElevator.ordermanagement.entity;

import java.security.SecureRandom;
import java.util.Random;

public class TokenGenerator {
    public static final String CHARACTERS  = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final  int TOKEN_LENGTH  = 42;
    public static final Random RANDOM = new SecureRandom();

    public static String generateToken(){
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for(int i = 0; i < TOKEN_LENGTH; i ++){
            token.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return token.toString();
    }
}
