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
    protected HashMap<String, String> headers;

    public HttpIdentifiers(Socket socket, boolean type) throws IOException {
        this.socket = socket;
        if (type) {
            this.inputStream = socket.getInputStream();
            this.headers = new HashMap<>();
            this.params = new HashMap<>();
        } else {
            this.outpuStream = socket.getOutputStream();
            this.headers = new HashMap<>();
            initResponse();
        }
    }

    public String getHttpReplie(int code) {
        for (String[] line : HTTPREPLIES) {
            if (line[0].equals(String.valueOf(code))){
                return line[1];
            }
        }
        return "Bad Request";
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
    protected String RESPONSE_MESSAGE = "OK";
    protected String CONTENT_LENGHT;
    protected String CONTENT_TYPE;
    protected String ACCESS_CONTROL_ALLOW_ORIGIN;
    protected OutputStream outpuStream;
    protected int RESPONSE_CODE;

    private void initResponse() {
        this.CONTENT_TYPE = "content-type: text/html";
        this.CONTENT_LENGHT = "content-length: 0";
        this.ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin: " + this.cyCServ.getHost();

    }
    // </editor-fold>

    // <editor-fold desc="Getters and Setters">
    public void addHeader(String header) {
        try {
            String[] headerSplit = header.split(":");
            if (headerSplit.length == 2) {
                this.headers.put(headerSplit[0], headerSplit[1]);
            }
        } catch (Exception e) {

        }
    }

    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setRESPONSE_CODE(int status) {
        this.RESPONSE_CODE = status;
    }

    public int getRESPONSE_CODE() {
        return this.RESPONSE_CODE;
    }
    // </editor-fold>

}
