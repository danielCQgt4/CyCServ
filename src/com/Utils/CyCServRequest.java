package com.Utils;

import java.io.InputStream;
import java.util.HashMap;

public abstract interface CyCServRequest {

// <editor-fold desc="Getter and Setters from req">
    public abstract String getDataRequest();

    public abstract void setDataRequest(String data);

    public abstract InputStream getInputStream();

    public abstract String getMethod();

    public abstract void setMethod(String method);

    public abstract String getRoute();

    public abstract void setRoute(String route);

    public abstract String getHttpVersion();

    public abstract void setHttpVersion(String httpVersion);

    public abstract String addParam(String key, String value);

    public abstract String getParam(String key);

    public abstract HashMap<String, String> getParams();

    public abstract void setParams(HashMap<String, String> params);
    
    public void setHeaders(HashMap<String, String> headers);
    // </editor-fold>    
}
