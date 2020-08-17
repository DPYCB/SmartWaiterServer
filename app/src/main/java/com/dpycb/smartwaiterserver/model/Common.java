package com.dpycb.smartwaiterserver.model;

public class Common {
    public static User currentUser;
    public static Request currentRequest;
    public static final int PICK_IMAGE_REQUEST = 71;

    public static String convertCodeToStatus(String code) {
        String result = "";
        if (code.equals("0")) {
            result = "Готовится";
        }
        else if (code.equals("1")) {
            result = "Завершен";
        }
        return result;
    }

}
