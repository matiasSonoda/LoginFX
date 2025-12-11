package com.sonoda.login.model.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    private static final String hasUpper = "(?=.*[A-Z])";
    private static final String hasLower = "(?=.*[a-z])";
    private static final String hasDigit = "(?=.*\\d)";
    private static final String hasSpecial = "(?=.*[#?!@$%^&*-])";
    private static final String minLength = ".{8,124}";

    private static final Pattern pUpper = Pattern.compile(hasUpper + minLength);
    private static final Pattern pLower = Pattern.compile(hasLower + minLength);
    private static final Pattern pDigit = Pattern.compile(hasDigit + minLength);
    private static final Pattern pSpecial = Pattern.compile(hasSpecial + minLength);

    public static List<String> isPasswordValid(String password){
        List<String> errors = new ArrayList<>();
        if(!pUpper.matcher(password).matches()){
            errors.add("Falta al menos una mayuscula (A-z)");
        }
        if(!pLower.matcher(password).matches()){
            errors.add("Falta al menos una minuscula (a-z)");
        }
        if(!pDigit.matcher(password).matches()){
            errors.add("Falta al menos un digito (0-9)");
        }
        if(!pSpecial.matcher(password).matches()){
            errors.add("Falta al menos un caracter especial (#?!@$%^&*-)");
        }
        if(password.length() < 8){
            errors.add("La contraseña debe tener al menos 8 caracteres");
        }
        return errors;
    }
}
