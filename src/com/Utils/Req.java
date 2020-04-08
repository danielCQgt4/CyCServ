package com.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Req {

    private Socket socket;
    private InputStream inputStream;

    public Req(Socket socket) throws IOException{
        this.socket = socket;
        this.inputStream = socket.getInputStream();
    }

}
