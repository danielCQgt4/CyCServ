package com.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;

public class HttpIdentifiers {

    // <editor-fold desc="Request">
    private final InputStream inputStream;
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
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    // </editor-fold>
}
