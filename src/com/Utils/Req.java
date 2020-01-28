package com.Utils;

import com.Handlers.HttpParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;

public final class Req extends HttpIdentifiers implements CyCServRequest {

    public Req(Socket socket) throws IOException {
        super(socket, true);
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
        if (!data.equals("") && !data.isEmpty()) {
            HttpParser httpParser = new HttpParser(data);
            this.setDataRequest(data);
            this.setHttpVersion(httpParser.getHttpVersion());
            this.setMethod(httpParser.getHttpMethod());
            this.setParams(httpParser.getHttpParams());
            this.setHeaders(httpParser.getHttpHeaders());
            this.setRoute(httpParser.getHttpRoute());
            this.setRESPONSE_CODE(httpParser.getHttpStatus());
        }
        if (this.getRESPONSE_CODE() == 0){
            this.setRESPONSE_CODE(400);
        }
        //System.out.println("Code: " + this.getRESPONSE_CODE());
    }

    // <editor-fold desc="Getter and Setters">
    @Override
    public String getDataRequest() {
        return super.dataRequest;
    }

    @Override
    public void setDataRequest(String data) {
        super.dataRequest = data;
    }

    @Override
    public InputStream getInputStream() {
        return super.inputStream;
    }

    @Override
    public String getMethod() {
        return super.method;
    }

    @Override
    public void setMethod(String method) {
        super.method = method;
    }

    @Override
    public String getRoute() {
        return super.route;
    }

    @Override
    public void setRoute(String route) {
        super.route = route;
    }

    @Override
    public String getHttpVersion() {
        return super.httpVersion;
    }

    @Override
    public void setHttpVersion(String httpVersion) {
        super.httpVersion = httpVersion;
    }

    @Override
    public String addParam(String key, String value) {
        return super.params.put(key, value);
    }

    @Override
    public String getParam(String key) {
        return super.params.get(key);
    }

    @Override
    public HashMap<String, String> getParams() {
        return super.params;
    }

    @Override
    public void setParams(HashMap<String, String> params) {
        super.params = params;
    }

    @Override
    public void setHeaders(HashMap<String, String> headers) {
        super.headers = headers;
    }
    // </editor-fold>
}
