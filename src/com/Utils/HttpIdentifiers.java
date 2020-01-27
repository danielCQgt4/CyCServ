package com.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;

public class HttpIdentifiers {

    // <editor-fold desc="Request">
    private InputStream inputStream;
    private HashMap<String, String> reqHeaders;
    private HashMap<String, String> params;
    private String dataRequest;
    private String method;
    private String route;
    private String httpVersion;

    public HttpIdentifiers(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.reqHeaders = new HashMap<>();
        this.params = new HashMap<>();
    }

    public String getDataRequest() {
        return dataRequest;
    }

    public void setDataRequest(String data) {
        this.dataRequest = data;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String addReqHeader(String key, String value) {
        return reqHeaders.put(key, value);
    }

    public String getReqHeader(String key) {
        return reqHeaders.get(key);
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String addParam(String key, String value) {
        return params.put(key, value);
    }

    public String getParam(String key) {
        return params.get(key);
    }

    public HashMap<String, String> getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(HashMap<String, String> reqHeaders) {
        this.reqHeaders = reqHeaders;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
    // </editor-fold>

    // <editor-fold desc="Response">
    public void fill() {

    }
    // </editor-fold>

    // <editor-fold desc="General">
    private static final String[][] HTTPREPLIES = {{"100", "Continue"},
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
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public HttpIdentifiers() {
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public static String[][] getHttpReplies() {
        return HTTPREPLIES;
    }
    // </editor-fold>
}
