package com.Utils;

import com.Server.CyCServ;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

public class HttpIdentifiers {

    // <editor-fold desc="General">
    private final CyCServ cyCServ = CyCServ.newInstance();
    private static final String[][] HTTPREPLIES = {
        {"100", "Continue"},
        {"101", "Switching Protocols"},
        {"200", "OK"},
        {"201", "Created"},
        {"202", "Accepted"},
        {"203", "Non-Authoritative Information"},
        {"204", "No Content"},
        {"205", "Reset Content"},
        {"206", "Partial Content"},
        {"300", "Multiple Choices"},
        {"301", "Moved Permanently"},
        {"302", "Found"},
        {"303", "See Other"},
        {"304", "Not Modified"},
        {"305", "Use Proxy"},
        {"306", "(Unused)"},
        {"307", "Temporary Redirect"},
        {"400", "Bad Request"},
        {"401", "Unauthorized"},
        {"402", "Payment Required"},
        {"403", "Forbidden"},
        {"404", "Not Found"},
        {"405", "Method Not Allowed"},
        {"406", "Not Acceptable"},
        {"407", "Proxy Authentication Required"},
        {"408", "Request Timeout"},
        {"409", "Conflict"},
        {"410", "Gone"},
        {"411", "Length Required"},
        {"412", "Precondition Failed"},
        {"413", "Request Entity Too Large"},
        {"414", "Request-URI Too Long"},
        {"415", "Unsupported Media Type"},
        {"416", "Requested Range Not Satisfiable"},
        {"417", "Expectation Failed"},
        {"500", "Internal Server Error"},
        {"501", "Not Implemented"},
        {"502", "Bad Gateway"},
        {"503", "Service Unavailable"},
        {"504", "Gateway Timeout"},
        {"505", "HTTP Version Not Supported"}};
    private final Socket socket;
    private int satatus;
    private HashMap<String, String> headers;

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void setSatatus(int satatus) {
        this.satatus = satatus;
    }

    public Socket getSocket() {
        return socket;
    }

    public HttpIdentifiers(Socket socket, boolean type) throws IOException {
        this.socket = socket;
        if (type) {
            this.inputStream = socket.getInputStream();
            this.headers = new HashMap<>();
            this.params = new HashMap<>();
        } else {
            this.outpuStream = socket.getOutputStream();
            initResponse();
        }
    }

    public static String[][] getHttpReplies() {
        return HTTPREPLIES;
    }
    // </editor-fold>

    // <editor-fold desc="Request">
    protected InputStream inputStream;
    protected HashMap<String, String> params;
    protected String dataRequest;
    protected String method;
    protected String route;
    protected String httpVersion;
    // </editor-fold>

    // <editor-fold desc="Response">
    protected static final Logger LOGGER = Logger.getLogger("com.Utils");
    protected final String CRLF = "\n\r";
    protected final String HTTP_VERSION = "HTTP/1.1";
    protected final String RESPONSE_MESSAGE = "OK";
    protected String CONTENT_LENGHT;
    protected String CONTENT_TYPE;
    protected String ACCESS_CONTROL_ALLOW_ORIGIN;//TEMP localhost:port
    protected OutputStream outpuStream;
    protected String RESPONSE_CODE;

    private void initResponse() {
        this.CONTENT_TYPE = "content-type: text/html";
        this.CONTENT_LENGHT = "content-length: 0";
        //this.ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin: " + this.cyCServ.getHost(); TODO UNCOMMENT
        this.ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin: *"; // TODO DELETE
        
    }
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

    public void setACCESS_CONTROL_ALLOW_ORIGIN(String url) {
        url = url.toLowerCase();
        url = url.replace("access-control-allow-origin:", "");
        this.ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin: " + url;
    }
    // </editor-fold>
    // </editor-fold>

}
