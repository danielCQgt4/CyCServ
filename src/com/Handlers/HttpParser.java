package com.Handlers;

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
    private HashMap<String, Object> bodyParams;
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
            if (!this.requestLine.toLowerCase().startsWith("head")) {//Control head method
                this.requestBody = calcReq.replace("\n", "").replace("\r", "");
                this.requestBodyBytes = this.requestBody.getBytes();
            }
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
                        this.routeOriginal = components[1].toLowerCase().split("\\?", 2)[0];
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

    // <editor-fold desc="WorkArea">
    private void initRequestBody() {
        if (this.validRequest && !this.method.equals("head")) {
            try {
                String content = this.headers.get("content-type");
                if (content != null) {
                    if (content.startsWith("application")) {
                        initApplicatonBodyParser();
                    } // <editor-fold desc="Other">
                    else if (content.startsWith("audio")) {

                    } else if (content.startsWith("image")) {

                    } else if (content.startsWith("multipart")) {

                    } else if (content.startsWith("text")) {

                    } else if (content.startsWith("video")) {

                    } else if (content.startsWith("application/vnd.")) {

                    } else {

                    }
                    // </editor-fold>
                } else {
                    //Handle body parser
                }
            } catch (Exception e) {
                this.validRequest = false;
            }
        }
    }

    private void initApplicatonBodyParser() {
        try {
            this.bodyParams = new HashMap<>();
            switch (this.headers.get("content-type")) {
                // <editor-fold desc="Unhandle">
                case "application/EDI-X12":
                    break;
                case "application/EDIFACT":
                    break;
                case "application/octet-stream":
                    break;
                case "application/ogg":
                    break;
                case "application/xhtml+xml":
                    break;
                case "application/x-shockwave-flash":
                    break;
                case "application/javascript"://FILE
                    break;
                case "application/pdf"://FILE
                    break;
                case "application/zip"://FILE
                    break;
                // </editor-fold>
                case "application/json"://FORMATTER
                    break;
                case "application/ld+json"://FORMATTER
                    break;
                case "application/xml"://FORMATTER
                    break;
                default://application/x-www-form-urlencoded
                    this.formUrlenconded();
                    break;
            }
        } catch (Exception e) {

        }
    }

    private void formUrlenconded() {
        try {
            String[] objects = this.requestBody.split("&");
            for (String object : objects) {
                try {
                    String[] keyValue = object.split("=", 2);
                    if (keyValue.length == 2) {
                        this.bodyParams.put(keyValue[0], keyValue[1]);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing: " + object + "\n\n");
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing: " + this.requestBody + "\nAs application/x-www-form-urlencoded \n\n");
        }
    }
    // </editor-fold>

    private String cleanRoute(String routeUncoding) {
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

        routeUncoding = routeUncoding.replace("%20", "");
        routeUncoding = routeUncoding.replace("+", "");
        // </editor-fold>
        for (int i = 33; i < 42; i++) {
            routeUncoding = routeUncoding.replace("%" + (i - 12), (char) i + "");
        }
        return routeUncoding;
    }
}
