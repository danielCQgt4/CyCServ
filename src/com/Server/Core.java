package com.Server;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

public class Core implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("com.Server");
    private Socket socket;
    private final CyCServ cyCServ;

    public Core(CyCServ cyCServ) {
        this.cyCServ = cyCServ;
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.socket = cyCServ.getServerSocket().accept();
                Runnable clientCore = new ClientCore(this.socket);
                Thread runClientCore = new Thread(clientCore);
                runClientCore.start();
            } catch (IOException e) {
                System.err.println("The communication fail with a client\nCause: " + e.getMessage());
            }
        }
    }

}
