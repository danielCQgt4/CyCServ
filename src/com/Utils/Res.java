package com.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Res {

    // <editor-fold desc="Properties">
    private static final Logger LOGGER = Logger.getLogger("com.Utils");
    private final String CRLF;
    private final String HTTP_VERSION;
    private final String RESPONSE_CODE;
    private final String RESPONSE_MESSAGE;
    private String CONTENT_LENGHT;
    private String CONTENT_TYPE;
    private final OutputStream outpuStream;
    // </editor-fold>

    // <editor-fold desc="Getters and Setters">
    public OutputStream getOutputStream() {
        return this.outpuStream;
    }

    public String getCRLF() {
        return CRLF;
    }

    public String getHTTP_VERSION() {
        return HTTP_VERSION;
    }

    public String getRESPONSE_CODE() {
        return RESPONSE_CODE;
    }

    public String getRESPONSE_MESSAGE() {
        return RESPONSE_MESSAGE;
    }

    public String getCONTENT_LENGHT(String content) {
        this.CONTENT_LENGHT = "Content-Length: " + content.length();
        return CONTENT_LENGHT;
    }

    public String getCONTENT_LENGHT() {
        this.CONTENT_LENGHT = "Content-Length: 0";
        return CONTENT_LENGHT;
    }

    public String getCONTENT_TYPE() {
        this.CONTENT_TYPE = "Content-type: text/html";
        return CONTENT_TYPE;
    }

    public String getCONTENT_TYPE(String contentType) {
        this.CONTENT_TYPE = contentType;
        return CONTENT_TYPE;
    }
    // </editor-fold>

    public Res(OutputStream outputStream) {
        this.outpuStream = outputStream;
        this.CRLF = "\n\r";
        this.HTTP_VERSION = "HTTP/1.1";
        this.RESPONSE_CODE = "200";
        this.RESPONSE_MESSAGE = "OK";
        this.CONTENT_TYPE = "Content-type: text/html";
        this.CONTENT_LENGHT = "0";
    }

    // <editor-fold desc="Actions">
    public String send(String bodyRes) {
        String res = null;
        try {
            res = this.getHTTP_VERSION() + " " + this.getRESPONSE_CODE() + " " + this.getRESPONSE_MESSAGE() + this.getCRLF()
                    + this.getCONTENT_LENGHT(bodyRes) + " " + this.getCONTENT_TYPE() + this.getCRLF() + this.getCRLF()
                    + bodyRes
                    + this.getCRLF() + this.getCRLF();
            this.outpuStream.write(res.getBytes());
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Error while sending response {0}", e);
        }
        return res;
    }

    public void close() throws IOException {
        this.outpuStream.close();
    }
    // </editor-fold>
}
