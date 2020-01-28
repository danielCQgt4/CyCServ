package com.Utils;

import java.io.OutputStream;

public interface CyCServResponse {

    // <editor-fold desc="Getters and Setters from res">
    public OutputStream getOutputStream();

    public String getCRLF();

    public String getHTTP_VERSION();

    public String getRESPONSE_CODE();
    
    public void setRESPONSE_CODE(int code);

    public String getRESPONSE_MESSAGE();

    public String setCONTENT_LENGHT(String content);

    public String getCONTENT_LENGHT();

    public String getCONTENT_TYPE();
    
    public void setCONTENT_TYPE(String contentType);

    public String getACCESS_CONTROL_ALLOW_ORIGIN();

    public void setACCESS_CONTROL_ALLOW_ORIGIN(String url);
    // </editor-fold>
}
