package vn.iotstar.util;

import java.util.regex.Pattern;

public final class FormValidation {
    private static final Pattern EMAIL = Pattern.compile(
            "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$");
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9._-]{3,100}$");
    private static final Pattern PHONE = Pattern.compile("^[0-9+() .-]{7,30}$");

    private FormValidation() {
    }

    public static boolean validEmail(String value) {
        return value != null && value.length() <= 255 && EMAIL.matcher(value).matches();
    }

    public static boolean validUsername(String value) {
        return value != null && USERNAME.matcher(value).matches();
    }

    public static boolean validPhone(String value) {
        return value == null || value.isBlank() || PHONE.matcher(value).matches();
    }
}
