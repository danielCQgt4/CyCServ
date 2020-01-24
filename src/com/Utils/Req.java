package com.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Req {

    private final InputStream inputStream;
    private Socket socket;
    private String data;

    public Req(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
    }

    public Req(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getData() {
        try {
            byte buffer[] = new byte[this.inputStream.available()];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(buffer, 0, this.inputStream.read(buffer));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < buffer.length; i++) {
                stringBuilder.append((char) buffer[i]);
            }
            this.data = stringBuilder.toString();
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
        return data;
    }

    public String getRemoteAddress() {
        return this.socket.getInetAddress().toString();
    }
    
    public InputStream getInputStream(){
        return this.inputStream;
    }
}
