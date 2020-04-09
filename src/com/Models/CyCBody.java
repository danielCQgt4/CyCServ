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
        return this.params.get(key);
    }

    public String getString(String key) {
        return this.get(key).toString();
    }

    public int getInteger(String key) {
        try {
            return Integer.parseInt(this.get(key).toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getDouble(String key) {
        try {
            return Double.parseDouble(this.get(key).toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public boolean getBoolean(String key) {
        try {
            return Boolean.parseBoolean(this.get(key).toString());
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // </editor-fold>
}
