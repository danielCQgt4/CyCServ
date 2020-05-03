package com.Utils;

import com.Helpers.BodyParser;
import com.Helpers.HttpParser;
import com.Models.CyCBody;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public final class Req {

    // <editor-fold desc="Attributtes">
    private final InputStream inputStream;
    private String request;
    /* *** Decode request line *** */
    private String method;
    private String route;
    private String httpVerion;
    /* *** Decode request headers *** */
    private final HashMap<String, String> headers;
    /* *** Decode request body *** */
    private String body;
    private byte[] bodyBytes;
    private CyCBody cyCBody;
    //TODO ver como manejar

    //TEMP
    private final OutputStream out;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public Req(Socket socket) throws IOException {
        this.headers = new HashMap<>();
        this.inputStream = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.decodeRequest();
    }
    // </editor-fold>

    // <editor-fold desc="Getters">
    public CyCBody getCyCBody() {
        return cyCBody;
    }

    public String getRequest() {
        return request;
    }

    public String getMethod() {
        return method;
    }

    public String getRoute() {
        return route;
    }

    public String getHttpVerion() {
        return httpVerion;
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }
    // </editor-fold>

    // <editor-fold desc="Actions">
    private String getRequestToString() throws IOException {
        try {
            byte buffer[] = new byte[this.inputStream.available()];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(buffer, 0, this.inputStream.read(buffer));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < buffer.length; i++) {
                stringBuilder.append((char) buffer[i]);
            }
            this.request = stringBuilder.toString();
            System.out.println(this.request);
        } catch (IOException e) {
            this.request = null;
        }
        return request;
    }

    private void decodeRequest() throws IOException {
        HttpParser parser = new HttpParser(this.getRequestToString());
        if (parser.isValidRequest()) {
            //Line
            this.method = parser.getMethod();
            this.route = parser.getRoute();
            this.httpVerion = parser.getHttpVersion();
            //Headers
            parser.getHeaders().forEach((k, v) -> {
                this.headers.put(k, v);
            });
            //Body
            this.body = parser.getRequestBody();
            this.bodyBytes = parser.getRequestBodyBytes();
            this.cyCBody = BodyParser.Build(
                    this.headers.get("content-type"),
                    body,
                    bodyBytes
            );
        }

        // <editor-fold desc="TEMP">
        String temp;
        if (parser.isValidRequest() && this.cyCBody != null) {
            //full
            temp = "Full-Full-Full-Full-Full \n" + this.request + "\n\n\n";

            temp += "-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            //line
            temp += "Line-Line-Line-Line-Line\n\n";
            temp += "   Method: " + this.method + "\n";
            temp += "   Route: " + this.route + "\n";
            temp += "   Version: " + this.httpVerion + "\n\n\n";

            temp += "-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            //headers
            temp += "Headers-Headers-Headers-Headers-Headers\n\n\n";

            final StringBuilder heads = new StringBuilder();

            this.headers.forEach((k, v) -> {
                heads.append("  ").append(k).append(" -:- ").append(v).append("\n");
            });
            temp += heads.toString() + "\n\n\n";

            temp += "-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            //body
            temp += "Body-Body-Body-Body-Body\n";

            System.out.println(this.cyCBody.getObject(1).getObject("key2").get("key3"));
            temp += this.cyCBody.getObject(0).get("key");
//            temp += this.cyCBody.get("key2");
        } else {
            temp = "No data";
        }
        // </editor-fold>

        out.write(temp.getBytes());
        //System.out.println(this.completeRequest);
        this.inputStream.close();
        this.out.close();
    }
    // </editor-fold>
}
