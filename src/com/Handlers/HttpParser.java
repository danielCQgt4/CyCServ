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
    private final HashMap<String, String> headers;
    /* **** Request body **** */
    private String body;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public HttpParser(String request) {
        this.request = request;
        /* *** Separate all data in containers *** */
        this.initRequest();
        /* *** Decode request line *** */
        this.initRequestLine();
        /* *** Decode request headers *** */
        this.headers = new HashMap<>();
        this.initRequestHeaders();
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

    public boolean isValidRequest() {
        return validRequest;
    }

    public String getMethod() {
        return method;
    }

    public String getRoute() {
        return route;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }
    // </editor-fold>

    // <editor-fold desc="Init">
    private void initRequest() {
        try {
            //Request Line
            String calcReq = this.request;
            this.requestLine = calcReq.substring(0, calcReq.indexOf("\r"));
            calcReq = calcReq.replace(this.requestLine, "");
            //Request Headers
            this.requestHeaders = calcReq.substring(2, calcReq.indexOf("\r\n\r\n"));
            calcReq = calcReq.replace(this.requestHeaders, "");
            //Request Body
            this.requestBody = calcReq.replace("\n", "").replace("\r", "");
            this.validRequest = true;
        } catch (Exception e) {
            this.validRequest = false;
        }
    }

    private void initRequestLine() {
        if (this.validRequest) {
            try {
                String calcReq = this.requestLine;
                if (!this.requestLine.isEmpty()) {
                    String[] components = calcReq.split(" ");
                    if (components.length == 3) {
                        this.method = components[0].toLowerCase();
                        this.route = components[1].toLowerCase();
                        this.httpVersion = components[2].toLowerCase();
                        if (!this.httpVersion.equals("http/1.1")) {
                            this.validRequest = false;
                        }
                    } else {
                        this.validRequest = false;
                    }
                } else {
                    this.validRequest = false;
                }
            } catch (Exception e) {
                this.validRequest = false;
            }
        }
    }
    // </editor-fold>

    private void initRequestHeaders() {
        if (this.validRequest) {
            try {
                String calcReq = this.requestHeaders;
                String[] headerKeyValue = calcReq.split("\r\n");
                for (String headLine : headerKeyValue) {
                    String[] headLineKeyValue = headLine.split(":", 2);
                    if (headLineKeyValue.length == 2) {
                        this.headers.put(
                                headLineKeyValue[0].trim().toLowerCase(),//Key
                                headLineKeyValue[1].trim().toLowerCase()//Value
                        );
                    } else {
                        this.validRequest = false;
                        break;
                    }
                }
                String temp = "break";
            } catch (Exception e) {
                this.validRequest = false;
            }
        }
    }
}
