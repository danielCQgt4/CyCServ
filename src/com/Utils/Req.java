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
        String data = getData();
//        System.out.println(data + "\n\n\n");
        HttpParser httpParser = new HttpParser(data);
        this.setDataRequest(data);
        this.setHttpVersion(httpParser.getHttpVersion());
        this.setMethod(httpParser.getHttpMethod());
        this.setParams(httpParser.getHttpParams());
        this.setReqHeaders(httpParser.getHttpHeaders());
        this.setRoute(httpParser.getHttpRoute());
        this.setSatatus(httpParser.getHttpStatus());
//        System.out.println("Metodo:" + httpParser.getHttpMethod());
//        System.out.println("Route:" + httpParser.getHttpRoute());
//        System.out.println("Http\\v:" + httpParser.getHttpVersion());
//        System.out.println("Status:" + httpParser.getHttpStatus());
//        httpParser.getHttpHeaders().forEach((k, v) -> System.err.println(k + " k:v " + v));
//        httpParser.getHttpParams().forEach((k, v) -> System.err.println(k + ":" + v));
    }

}
