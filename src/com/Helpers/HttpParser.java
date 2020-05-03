package com.Helpers;

import java.util.HashMap;

public final class HttpParser {

    // <editor-fold desc="Attributes">
    /* **** General ***** */
    private final String request;
    private String requestLine;
    private String requestHeaders;
    private String requestBody;
    private byte[] requestBodyBytes;//To handle files
    private boolean validRequest;
    /* **** Request line **** */
    private String method;
    private String route;
    private String routeOriginal;
    private String httpVersion;
    /* **** Request headers **** */
    private final HashMap<String, String> headers;
    /* **** Request body **** */
//    private HashMap<String, Object> bodyParams;
    private String getBodyParams;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public HttpParser(String request) {
        if (!request.isEmpty()) {
            /* *** Init finals *** */
            this.headers = new HashMap<>();
            this.request = request;
            this.validRequest = true;
            /* *** Separate all data in containers *** */
            this.initRequest();
        } else {
            this.headers = new HashMap<>();
            this.request = request;
            this.validRequest = false;
        }
    }
    // </editor-fold>

    // <editor-fold desc="Getters">
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

    public String getRequestBody() {
        return requestBody;
    }

    public byte[] getRequestBodyBytes() {
        return requestBodyBytes;
    }
    // </editor-fold>

    // <editor-fold desc="Init">
    private void initRequest() {
        try {
            /* *** Decode request line *** */
            String calcReq = this.request;
            this.requestLine = calcReq.substring(0, calcReq.indexOf("\r"));
            calcReq = calcReq.replace(this.requestLine, "");
            this.initRequestLine();

            /* *** Decode request headers *** */
            this.requestHeaders = calcReq.substring(2, calcReq.indexOf("\r\n\r\n"));
            calcReq = calcReq.replace(this.requestHeaders, "");
            this.initRequestHeaders();

            /* *** Decode request body *** */
            if (!this.method.equals("head")) {//Control head method
                if (this.method.equals("get") || this.getBodyParams != null) {
                    if (this.getBodyParams != null) {
                        this.requestBody = this.getBodyParams;
                        if (this.requestBody != null) {
                            this.requestBodyBytes = this.requestBody.getBytes();
                        }
                    } else {
                        this.requestBody = calcReq.replace("\n", "").replace("\r", "");
                        this.requestBodyBytes = this.requestBody.getBytes();
                    }
                } else {
                    this.requestBody = calcReq.replace("\n", "").replace("\r", "");
                    this.requestBodyBytes = this.requestBody.getBytes();
                }
            }
            this.validRequest = true;
        } catch (Exception e) {
            System.out.println(e);
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
                        String[] getDecode = components[1].toLowerCase().split("\\?", 2);
                        this.routeOriginal = getDecode[0];
                        if (getDecode.length == 2) {
                            this.getBodyParams = cleanBody(getDecode[1]);
                        } else {
                            this.getBodyParams = null;
                        }
                        this.route = cleanRoute(this.routeOriginal);
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
                this.validRequest = true;
            } catch (Exception e) {
                this.validRequest = false;
            }
        }
    }
    // </editor-fold>

    // <editor-fold desc="Tools">
    private String clean(String routeUncoding) {
        // <editor-fold desc="static">
        routeUncoding = routeUncoding.replace("%7f", (char) 127 + "");
        routeUncoding = routeUncoding.replace("%7e", (char) 126 + "");
        routeUncoding = routeUncoding.replace("%7d", (char) 125 + "");
        routeUncoding = routeUncoding.replace("%7c", (char) 124 + "");
        routeUncoding = routeUncoding.replace("%7b", (char) 123 + "");

        routeUncoding = routeUncoding.replace("%60", (char) 98 + "");

        routeUncoding = routeUncoding.replace("%5e", (char) 94 + "");
        routeUncoding = routeUncoding.replace("%5d", (char) 93 + "");
        routeUncoding = routeUncoding.replace("%5c", (char) 92 + "");
        routeUncoding = routeUncoding.replace("%5b", (char) 91 + "");

        routeUncoding = routeUncoding.replace("%40", (char) 64 + "");
        routeUncoding = routeUncoding.replace("%3f", (char) 63 + "");
        routeUncoding = routeUncoding.replace("%3e", (char) 62 + "");
        routeUncoding = routeUncoding.replace("%3d", (char) 61 + "");
        routeUncoding = routeUncoding.replace("%3c", (char) 60 + "");
        routeUncoding = routeUncoding.replace("%3b", (char) 59 + "");
        routeUncoding = routeUncoding.replace("%3a", (char) 58 + "");

        routeUncoding = routeUncoding.replace("%2f", (char) 47 + "");

        routeUncoding = routeUncoding.replace("%2c", (char) 44 + "");
        routeUncoding = routeUncoding.replace("%2b", (char) 43 + "");

        // </editor-fold>
        return routeUncoding;
    }

    private String cleanRoute(String routeUncoding) {
        // <editor-fold desc="static">
        routeUncoding = clean(routeUncoding);

        routeUncoding = routeUncoding.replace("%20", "");
        routeUncoding = routeUncoding.replace("\\+", "");
        // </editor-fold>
        for (int i = 33; i < 42; i++) {
            routeUncoding = routeUncoding.replace("%" + (i - 12), (char) i + "");
        }
        return routeUncoding;
    }

    private String cleanBody(String routeUncoding) {
        // <editor-fold desc="static">
        routeUncoding = clean(routeUncoding);

        routeUncoding = routeUncoding.replace("%20", " ");
        routeUncoding = routeUncoding.replace("\\+", " ");
        // </editor-fold>
        for (int i = 33; i < 42; i++) {
            routeUncoding = routeUncoding.replace("%" + (i - 12), (char) i + "");
        }
        return routeUncoding;
    }
    // </editor-fold>
}
