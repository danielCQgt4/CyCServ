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
    private InputStream inputStream;
    private String request;
    /* *** Decode request line *** */
    private String method;
    private String route;
    private String httpVerion;
    /* *** Decode request headers *** */
    private HashMap<String, String> headers;
    /* *** Decode request body *** */
    private String body;
    private byte[] bodyBytes;
    private CyCBody cyCBody;
    //TODO ver como manejar

    // </editor-fold>

    // <editor-fold desc="Constructors">
    public Req(Socket socket) throws IOException {
        this.headers = new HashMap<>();
        this.inputStream = socket.getInputStream();
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
            this.request = "";
            int max = 3, tries = 0;
            while (this.request.isEmpty() && tries++ < max) {
                byte buffer[] = new byte[this.inputStream.available()];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(buffer, 0, this.inputStream.read(buffer));
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < buffer.length; i++) {
                    stringBuilder.append((char) buffer[i]);
                }
                this.request = stringBuilder.toString();
            }
        } catch (IOException e) {
            System.out.println(e);
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

    }

    // </editor-fold>
}
