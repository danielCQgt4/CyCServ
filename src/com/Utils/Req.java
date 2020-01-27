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
        HttpParser httpParser = new HttpParser(data);
    }

}
