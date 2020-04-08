package com.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public final class Req {

    // <editor-fold desc="Attributtes">
    private final InputStream inputStream;

    private String completeRequest;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public Req(Socket socket) throws IOException {
        this.inputStream = socket.getInputStream();
        this.getRequestToString();
    }
    // </editor-fold>

    private String getRequestToString() throws IOException{
        try {
            byte buffer[] = new byte[this.inputStream.available()];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(buffer, 0, this.inputStream.read(buffer));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < buffer.length; i++) {
                stringBuilder.append((char) buffer[i]);
            }
            this.completeRequest = stringBuilder.toString();
        } catch (IOException e) {
            this.completeRequest = null;
        }
        System.out.println(this.completeRequest);
        this.inputStream.close();
        return completeRequest;
    }
    
}
