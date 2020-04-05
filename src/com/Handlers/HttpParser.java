package com.Handlers;

import java.util.HashMap;
import java.util.logging.Logger;

public final class HttpParser {

    // <editor-fold desc="Properties">
    private static final Logger LOGGER = Logger.getLogger("com.Utils");
    private final String httpData;
    private final String httpInfoLine;
    private final String httpHeadersString;
    private final String httpBodyString;
    //DATA
    private int httpStatus = 200;
    private String httpMethod;
    private String httpRoute;
    private boolean routeType = false;
    private HashMap<String, String> httpParams = new HashMap<>();
    private String httpVersion;
    private HashMap<String, String> httpHeaders = new HashMap<>();
    private String httpBody;
    // </editor-fold>

    // <editor-fold desc="INIT">
    public HttpParser(String data) {
        this.httpData = data;
        this.httpInfoLine = setHttpInfoLine(this.httpData);
        this.httpHeadersString = setHttpHeaders(this.httpData);
        this.httpBodyString = setHttpBody(this.httpData);
        this.decodeData();
    }

    private String setHttpInfoLine(String data) {
        try {
            String info = data.substring(0, data.indexOf("\n"));
            return info;
        } catch (Exception e) {
            this.httpStatus = 400;
            return null;
        }
    }

    private String setHttpHeaders(String data) {
        try {
            String headers = data.substring(data.indexOf("\n") + 1, data.indexOf("\n\r"));
            return headers;
        } catch (Exception e) {
            this.httpStatus = 400;
            return null;
        }
    }

    private String setHttpBody(String data) {
        try {
            String body = data.substring(data.indexOf("\n\r") + 3);
            return body;
        } catch (Exception e) {
            this.httpStatus = 400;
            return null;
        }
    }
    // </editor-fold>

    // <editor-fold desc="GeneralActions">
    private void decodeData() {
        if (this.httpStatus != 400) {
            if (this.httpInfoLine != null) {
                this.setHttpMethod();
                this.setRoute();
                this.setHttpVersion();
            }
            if (this.httpHeadersString != null) {
                this.setHttpHeaders();
            }
            if (this.httpBodyString != null) {
                this.setHttpBody();
            }
            if (this.httpStatus != 400) {
                this.httpStatus = 200;
            }
        }
    }
    // </editor-fold>

    // <editor-fold desc="Headers">
    private void setHttpHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        try {
            String[] headersSplit = this.httpHeadersString.split("\n");
            for (String headerContent : headersSplit) {
                String[] headerContentSplit = headerContent.split(":", 2);
                headers.put(headerContentSplit[0], headerContentSplit[1].trim());
            }
        } catch (Exception e) {
            this.httpStatus = 400;
        }
        this.httpHeaders = headers;
    }

    // <editor-fold desc="InfoLine">
    private void setHttpMethod() {
        try {
            String method = this.httpInfoLine.substring(0, this.httpInfoLine.indexOf(32));
            this.httpMethod = method.trim();
        } catch (Exception e) {
            this.httpStatus = 400;
        }
    }

    private void setRoute() {
        try {
            String route = this.httpInfoLine.substring(this.httpInfoLine.indexOf(32), this.httpInfoLine.lastIndexOf(32));
            route = route.trim().replace("%20", "");
            if (route.indexOf("?") > 0) {
                String[] routeSplit = route.split("\\?");
                try {
                    String paramsString = routeSplit[1];
                    this.httpParams = setParams(paramsString);
                    this.routeType = false;
                } catch (ArrayIndexOutOfBoundsException e) {
                    this.httpStatus = 400;
                } finally {
                    route = routeSplit[0];
                }
            } else {
                this.routeType = true;
            }
            this.httpRoute = route;
        } catch (Exception e) {
            this.httpStatus = 400;
        }
    }

    private HashMap<String, String> setParams(String paramsString) {
        HashMap<String, String> param = new HashMap<>();
        if (!paramsString.equals("")) {
            try {
                String[] splitParams = paramsString.split("&");
                for (String splitParam : splitParams) {
                    String[] keyValue = splitParam.split("=");
                    param.put(keyValue[0], keyValue[1]);
                }
            } catch (Exception e) {
                this.httpStatus = 400;
            }
        }
        return param;
    }

    private void setHttpVersion() {
        try {
            String version = this.httpInfoLine.substring(this.httpInfoLine.lastIndexOf(32));
            this.httpVersion = version.trim();
        } catch (Exception e) {
            this.httpStatus = 400;
            this.httpVersion = null;
        }
    }
    // </editor-fold>
    // </editor-fold>

    // <editor-fold desc="Body">
    private void setHttpBody() {
        this.httpBody = this.httpBodyString;
        if (this.routeType) {
            HashMap<String, String> params = this.setParams(this.httpBody);
            this.httpParams = params;
        }
    }
    // </editor-fold>

    // <editor-fold desc="Getter">
    public String getHttpData() {
        return httpData;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getHttpRoute() {
        return httpRoute;
    }

    public HashMap<String, String> getHttpParams() {
        return httpParams;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HashMap<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public String getHttpBody() {
        return httpBody;
    }
    // </editor-fold>

}
