package com.izooki.GTJwt.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderImpl {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("lizette"));
        System.out.println(passwordEncoder.encode("deborah"));
        System.out.println(passwordEncoder.encode("beth"));
    }
}
