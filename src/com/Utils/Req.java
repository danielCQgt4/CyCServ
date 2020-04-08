package com.Utils;

import com.Handlers.HttpParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public final class Req {

    // <editor-fold desc="Attributtes">
    private final InputStream inputStream;

    //TEMP
    private final OutputStream out;

    private String completeRequest;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public Req(Socket socket) throws IOException {
        this.inputStream = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.getRequestToString();
        this.decodeRequest();
    }
    // </editor-fold>

    private String getRequestToString() throws IOException {
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
        return completeRequest;
    }

    private void decodeRequest() throws IOException {
        HttpParser parser = new HttpParser(this.completeRequest);

        String temp = "Full-Full-Full-Full-Full \n" + this.completeRequest + "\n\n\n"; 
        temp += "Line-Line-Line-Line-Line\n" + parser.getRequestLine() + "\n\n\n";
        temp += "Headers-Headers-Headers-Headers-Headers\n" + parser.getRequestHeaders() + "\n\n\n";
        temp += "Body-Body-Body-Body-Body\n" + parser.getRequestBody();

        out.write(temp.getBytes());
        System.out.println(this.completeRequest);
        this.inputStream.close();
        this.out.close();
    }
}
