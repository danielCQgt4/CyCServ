package com.Handlers;

import java.util.HashMap;

public final class HttpParser {

    // <editor-fold desc="Attributes">
    /* **** General ***** */
    private final String request;
    private String requestLine;
    private String requestHeaders;
    private String requestBody;
    private boolean validRequest;
    /* **** Request line **** */
    private String method;
    private String route;
    private String httpVersion;
    /* **** Request headers **** */
    private HashMap<String, String> headers;
    /* **** Request body **** */
    private String body;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public HttpParser(String request) {
        this.request = request;
        this.initRequest();
    }
    // </editor-fold>

    // <editor-fold desc="Getters">
    public String getRequestLine() {
        return requestLine;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public String getRequestBody() {
        return requestBody;
    }
    // </editor-fold>

    // <editor-fold desc="Setters">
    // </editor-fold>
    private void initRequest() {
        try {
            //Request Line
            String calcReq = this.request;
            this.requestLine = calcReq.substring(0, calcReq.indexOf("\r"));
            calcReq = calcReq.replace(this.requestLine, "");
            //Request Headers
            this.requestHeaders = calcReq.substring(2,calcReq.indexOf("\r\n\r\n"));
            calcReq = calcReq.replace(this.requestHeaders, "");
            //Request Body
            this.requestBody = calcReq.replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            this.validRequest = false;
        }
    }
}
