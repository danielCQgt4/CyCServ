package com.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;

public class HttpIdentifiers {

    // <editor-fold desc="Request">
    private final InputStream inputStream;
    private final HashMap<String, String> reqHeaders = new HashMap<>();
    private final HashMap<String, String> params = new HashMap<>();
    private String dataRequest;
    private String method;
    private String route;
    private String httpVersion;

    public HttpIdentifiers(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
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

    public String addParam(String key, String value){
        return params.put(key, value);
    }
    
    public String getParam(String key){
        return params.get(key);
    }
    
    // </editor-fold>
    // <editor-fold desc="Response">
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
