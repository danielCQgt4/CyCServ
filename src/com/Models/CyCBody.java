package com.Models;

import java.util.HashMap;

public class CyCBody {

    // <editor-fold desc="Attributes">
    private final HashMap<String, Object> params;
    // </editor-fold>

    // <editor-fold desc="Contructors">
    public CyCBody(HashMap<String, Object> params) {
        this.params = params;
    }
    // </editor-fold>

    // <editor-fold desc="General">
    public Object get(String key) {
        try {
            Object o = this.params.get(key);
            if (o == null) {
                o = "";
            }
            return o;
        } catch (Exception e) {
            return "";
        }
    }

    public String getString(String key) {
        try {
            String s = this.get(key).toString();
            if (s == null) {
                s = "";
            }
            return s;
        } catch (Exception e) {
            return "";
        }
    }

    public int getInteger(String key) {
        try {
            int i = Integer.parseInt(this.get(key).toString());
            return i;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getDouble(String key) {
        try {
            double d = Double.parseDouble(this.get(key).toString());
            return d;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public boolean getBoolean(String key) {
        try {
            boolean b = Boolean.parseBoolean(this.get(key).toString());
            return b;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // </editor-fold>
}
