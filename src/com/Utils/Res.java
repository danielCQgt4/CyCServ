package com.Utils;

import com.Server.CyCServ;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Res {

    //TODO handle and make fix for header(Works already)
    // <editor-fold desc="Properties">
    private static final Logger LOGGER = Logger.getLogger("com.Utils");
    private final CyCServ cyCServ = CyCServ.newInstance();
    private final String CRLF;
    private final String HTTP_VERSION;
    private final String RESPONSE_CODE;
    private final String RESPONSE_MESSAGE;
    private String CONTENT_LENGHT;
    private String CONTENT_TYPE;
    private String ACCESS_CONTROL_ALLOW_ORIGIN;
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
        this.CONTENT_LENGHT = "content-length: " + content.length();
        return CONTENT_LENGHT;
    }

    public String getCONTENT_LENGHT() {
        return CONTENT_LENGHT;
    }

    public String getCONTENT_TYPE() {
        return CONTENT_TYPE;
    }

    public String getCONTENT_TYPE(String contentType) {
        this.CONTENT_TYPE = contentType;
        return CONTENT_TYPE;
    }

    public String getACCESS_CONTROL_ALLOW_ORIGIN() {
        return ACCESS_CONTROL_ALLOW_ORIGIN;
    }

    public void setACCESS_CONTROL_ALLOW_ORIGIN(String ACCESS_CONTROL_ALLOW) {
        this.ACCESS_CONTROL_ALLOW_ORIGIN = ACCESS_CONTROL_ALLOW;
    }
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public Res(OutputStream outputStream) {
        this.outpuStream = outputStream;
        this.CRLF = "\n\r";
        this.HTTP_VERSION = "HTTP/1.1";
        this.RESPONSE_CODE = "200";
        this.RESPONSE_MESSAGE = "OK";
        this.CONTENT_TYPE = "content-type: text/html";
        this.CONTENT_LENGHT = "content-length: 0";
        //this.ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin: " + this.cyCServ.getHost(); TODO UNCOMMENT
        this.ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin: *"; // TODO DELETE
    }
    // <editor-fold>

    // <editor-fold desc="Actions">
    public String send(String bodyRes) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(this.getHTTP_VERSION()).append(" ");
            stringBuilder.append(this.getRESPONSE_CODE()).append(" ");
            stringBuilder.append(this.getRESPONSE_MESSAGE()).append(" ");
            stringBuilder.append(this.getCRLF());
            //Headers
            stringBuilder.append(" ").append("\n");
            stringBuilder.append(this.getCONTENT_LENGHT(bodyRes)).append("\n");
            stringBuilder.append(this.getCONTENT_TYPE()).append("\n");
            stringBuilder.append(this.getACCESS_CONTROL_ALLOW_ORIGIN()).append("\n");
            stringBuilder.append(this.getCRLF());
            stringBuilder.append(this.getCRLF());
            //Body
            stringBuilder.append(bodyRes);
            stringBuilder.append(this.getCRLF());
            stringBuilder.append(this.getCRLF());
            this.outpuStream.write(stringBuilder.toString().getBytes());
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Error while sending response {0}", e);
        }
        return stringBuilder.toString();
    }

    public void close() throws IOException {
        this.outpuStream.close();
    }
    // </editor-fold>
}
