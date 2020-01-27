package com.Utils;

import java.util.logging.Logger;
import javax.swing.JOptionPane;

public final class HttpParser {

    private static final Logger LOGGER = Logger.getLogger("com.Utils");
    private final String httpData;
    private final String httpInfoLine;
    private final String httpHeaders;
    private final String httpBody;

    // <editor-fold desc="INIT">
    public HttpParser(String data) {
        this.httpData = data;
        this.httpInfoLine = setHttpInfoLine(this.httpData);
        this.httpHeaders = setHttpHeaders(this.httpData);
        this.httpBody = setHttpBody(this.httpData);
        this.decodeData();
    }

    private String setHttpInfoLine(String data) {
        try {
            return data.substring(0, data.indexOf("\n"));
        } catch (Exception e) {
            return null;
        }
    }

    private String setHttpHeaders(String data) {
        try {
            return data.substring(data.indexOf("\n") + 1, data.indexOf("\n\r"));
        } catch (Exception e) {
            return null;
        }
    }

    private String setHttpBody(String data) {
        try {
            return data.substring(data.indexOf("\n\r") + 3);
        } catch (Exception e) {
            return null;
        }
    }
    // </editor-fold>

    // <editor-fold desc="GeneralActions">
    private void decodeData() {
        System.out.println("Infoline:\n" + this.httpInfoLine);
        System.out.println("\n\nHeaders:\n" + this.httpHeaders);
        System.out.println("\n\nBody:\n" + this.httpBody);
    }

    private String setHttpMethod(String infoLine) {
        return null;
    }
    // </editor-fold>

    // <editor-fold desc="getGET">
    // </editor-fold>
    
    // <editor-fold desc="getPOST">
    // </editor-fold>
    
    // <editor-fold desc="Pre">
/*
    public void decodeRequest() {
        boolean method = false;
        StringBuilder value = new StringBuilder();
        String data = getData();
        int i;
        for (i = 0; i < data.length(); i++) {
            if (!method) {
                if (data.charAt(i) != 32) {
                    value.append(data.charAt(i));
                } else {
                    this.setMethod(value.toString().toUpperCase());
                    value.setLength(0);
                    method = true;
                    break;
                }
            }
        }
        i++;
        System.out.println(data.replace("\n", "@ \n").replace("\r", "#"));
        if (method && this.getMethod().equals("GET")) {
            //decodeGETRequest(data, i);
        } else if (method && this.getMethod().equals("POST")) {
            //decodePOSTRequest(data, i);
        } else if (method && this.getMethod().equals("PUT")) {
            // TODO Handle PUT request
        } else if (method && this.getMethod().equals("DELETE")) {
            // TODO Handle DELETE request
        }
    }

    private void decodeGETRequest(String data, int start) {
        boolean route = false,
                versionHttp = false,
                mode = false;
        StringBuilder value = new StringBuilder();
        StringBuilder key = new StringBuilder();
        for (int i = start; i < data.length(); i++) {
            if (!route) {
                if (data.charAt(i) != 32) {
                    value.append(data.charAt(i));
                } else {
                    this.setRoute(value.toString().replace("%20", ""));
                    value.setLength(0);
                    route = true;
                }
            } else if (!versionHttp) {
                if (data.charAt(i) != 13 && data.charAt(i + 1) != 10) {
                    value.append(data.charAt(i));
                } else {
                    this.setHttpVersion(value.toString().replace("\n\r", ""));
                    value.setLength(0);
                    versionHttp = true;
                }
            } else {
                if (!mode) {
                    if (data.charAt(i) != 58) {
                        key.append(data.charAt(i));
                    } else {
                        mode = !mode;
                    }
                } else {
                    if (data.charAt(i) != 13 && data.charAt(i + 1) != 10) {
                        value.append(data.charAt(i));
                    } else {
                        this.addReqHeader(key.toString().replace("\n", "").replace("\r", ""),
                                value.toString().replace(" ", ""));
                        key.setLength(0);
                        value.setLength(0);
                        mode = !mode;
                    }
                }
            }
        }
    }

    private void decodePOSTRequest(String data, int start) {
        boolean route = false,
                bodyType = false,
                versionHttp = false,
                headers = false,
                mode = false;
        String temp = "";
        StringBuilder value = new StringBuilder();
        StringBuilder key = new StringBuilder();
        for (int i = start; i < data.length(); i++) {
            // REAL CODE
            if (!route) {
                if (data.charAt(i) != 32) {
                    value.append(data.charAt(i));
                } else {
                    String vRoute = value.toString().replace("%20", "");

                    temp += "Route: " + vRoute + "\n";

                    bodyType = vRoute.indexOf('?') >= 0;
                    this.setRoute(vRoute);
                    value.setLength(0);
                    route = true;
                }
            } else {
                if (!bodyType) {

                    if (!versionHttp) {
                        if (data.charAt(i) != 13 && data.charAt(i + 1) != 10) {
                            value.append(data.charAt(i));
                        } else {

                            temp += "HTTP/VERSION: " + value.toString().replace("\n\r", "") + "\n";

                            this.setHttpVersion(value.toString().replace("\n\r", ""));
                            value.setLength(0);
                            versionHttp = true;
                        }
                    } else {

                    }

                } else {
                    if (!versionHttp) {
                        if (data.charAt(i) != 13 && data.charAt(i + 1) != 10) {
                            value.append(data.charAt(i));
                        } else {

                            temp += "HTTP/VERSION: " + value.toString().replace("\n\r", "") + "\n";

                            this.setHttpVersion(value.toString().replace("\n\r", ""));
                            value.setLength(0);
                            versionHttp = true;
                        }
                    } else {
                        if (!mode) {
                            if (data.charAt(i) != 58) {
                                key.append(data.charAt(i));
                            } else {
                                mode = !mode;
                            }
                        } else {
                            if (data.charAt(i) != 13 && data.charAt(i + 1) != 10) {
                                value.append(data.charAt(i));
                            } else {

                                temp += "Header -> " + key.toString().replace("\n", "").replace("\r", "")
                                        + ":"
                                        + value.toString().replace(" ", "") + "\n";

                                this.addReqHeader(key.toString().replace("\n", "").replace("\r", ""),
                                        value.toString().replace(" ", ""));
                                key.setLength(0);
                                value.setLength(0);
                                mode = !mode;
                            }
                        }
                    }
                }
            }

//              CODE BASE            
//            if (!route) {
//                if (data.charAt(i) != 32) {
//                    value.append(data.charAt(i));
//                } else {
//
//                    temp += "Route: " + value.toString().replace("%20", "") + "\n";
//
//                    this.setRoute(value.toString().replace("%20", ""));
//                    value.setLength(0);
//                    route = true;
//                }
//            } else if (!versionHttp) {
//                if (data.charAt(i) != 13 && data.charAt(i + 1) != 10) {
//                    value.append(data.charAt(i));
//                } else {
//
//                    temp += "HTTP/VERSION: " + value.toString().replace("\n\r", "") + "\n";
//
//                    this.setHttpVersion(value.toString().replace("\n\r", ""));
//                    value.setLength(0);
//                    versionHttp = true;
//                }
//            } else {
//                if (!mode) {
//                    if (data.charAt(i) != 58) {
//                        key.append(data.charAt(i));
//                    } else {
//                        mode = !mode;
//                    }
//                } else {
//                    if (data.charAt(i) != 13 && data.charAt(i + 1) != 10) {
//                        value.append(data.charAt(i));
//                    } else {
//
//                        temp += key.toString().replace("\n", "").replace("\r", "") + 
//                                ":" +
//                                value.toString().replace(" ", "") + "\n";
//
//                        this.addReqHeader(key.toString().replace("\n", "").replace("\r", ""),
//                                value.toString().replace(" ", ""));
//                        key.setLength(0);
//                        value.setLength(0);
//                        mode = !mode;
//                    }
//                }
//            }
        }
        //System.out.println("\n\nData decoded\n" + temp);
    }*/
    // </editor-fold>
}
