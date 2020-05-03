package com.Models;

public class CyCBody {

    // <editor-fold desc="Attributes">
    /* FORMURLENCODED */
    private final CyCParams<Object, Object> params;
    // </editor-fold>

    // <editor-fold desc="Contructors">
    public CyCBody(CyCParams<Object, Object> params) {
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

    public CyCParams<Object, Object> getObject(Object key) {
        try {
            CyCParams<Object, Object> o = this.params.getObject(key);
            return o;
        } catch (Exception e) {
            return null;
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
