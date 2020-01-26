package com.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Req extends HttpIdentifiers {

    private static final Logger LOGGER = Logger.getLogger("com.Utils");

    public Req(Socket socket) throws IOException {
        super(socket);
        this.decodeRequest();
    }

    private String getData() {
        try {
            this.setDataRequest(null);
            byte buffer[] = new byte[this.getInputStream().available()];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(buffer, 0, this.getInputStream().read(buffer));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < buffer.length; i++) {
                stringBuilder.append((char) buffer[i]);
            }
            this.setDataRequest(stringBuilder.toString());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while reading the response ", e);
        }
        return this.getDataRequest();
    }

    public String getRemoteAddress() {
        return this.getSocket().getInetAddress().toString();
    }

    public void decodeRequest() {
//        HttpParser httpParser = new HttpParser(this.getInputStream());
//        this.setMethod(httpParser.getMethod());
//        this.setParams(httpParser.getParams());
//        this.setReqHeaders(httpParser.getHeaders());
//        this.setHttpVersion("HTTP/" + httpParser.getMethod());
//        System.out.println("Method" + this.getMethod());
//        System.out.println("Params");
//        this.getParams().forEach((k, v) -> System.out.println("     key: " + k + "value: " + v));
//        System.out.println("Headers");
//        this.getReqHeaders().forEach((k, v) -> System.out.println("     key: " + k + "value: " + v));
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
        if (method && this.getMethod().equals("GET")) {
            System.out.println(data);
            decodeGETRequest(data, i);
        } else if (method && this.getMethod().equals("POST")) {
            System.out.println("\n\nData received\n" + data); ///// TODO delete this line
            decodePOSTRequest(data, i);
        } else if (method && this.getMethod().equals("PUT")) {
            // TODO Handle PUT request
            System.out.println("Put request");
        } else if (method && this.getMethod().equals("DELETE")) {
            // TODO Handle DELETE request
            System.out.println("Delete request");
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
    }

}
