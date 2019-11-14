package com.gcox.fansmeet.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 9/10/2015.
 */
public class EmailUtil {

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmail(String email) {

        if (StringUtil.isNullOrEmptyString(email)) {
            return false;
        }
        email = email.trim();
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
