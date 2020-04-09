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

    private String completeRequest;

    //TEMP
    private final OutputStream out;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public Req(Socket socket) throws IOException {
        this.inputStream = socket.getInputStream();
        this.out = socket.getOutputStream();
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
            System.out.println(this.completeRequest.replace("\n", "@\n").replace("\r", "#"));
        } catch (IOException e) {
            this.completeRequest = null;
        }
        return completeRequest;
    }

    private void decodeRequest() throws IOException {
        HttpParser parser = new HttpParser(this.getRequestToString());
        if (parser.isValidRequest()) {

        }

        String temp = "";

        if (parser.isValidRequest()) {
            //full
            temp = "Full-Full-Full-Full-Full \n" + this.completeRequest + "\n\n\n";

            temp += "-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            //line
            temp += "Line-Line-Line-Line-Line\n\n";
            temp += "   Method: " + parser.getMethod() + "\n";
            temp += "   Route: " + parser.getRoute() + "\n";
            temp += "   Version: " + parser.getHttpVersion() + "\n\n\n";

            temp += "-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            //headers
            temp += "Headers-Headers-Headers-Headers-Headers\n"
                    .replace("\n", "@\n").replace("\r", "#") + "\n\n";

            final StringBuilder heads = new StringBuilder();

            parser.getHeaders().forEach((k, v) -> {
                heads.append("  ").append(k).append(" -:- ").append(v).append("\n");
            });
            temp += heads.toString() + "\n\n\n";

            temp += "-------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            //body
            temp += "Body-Body-Body-Body-Body\n" + parser.getRequestBody();
        } else {
            temp = "No data";
        }

        
        out.write(temp.getBytes());
        //System.out.println(this.completeRequest);
        this.inputStream.close();
        this.out.close();
    }
}
