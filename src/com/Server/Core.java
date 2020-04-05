package com.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Core implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("com.Server");
    private final CyCServ cyCServ;
    private final ServerSocket serverSocket;
    private Socket socket;

    public Core(CyCServ cyCServ1, ServerSocket serverSocket) {
        this.cyCServ = cyCServ1;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.socket = this.serverSocket.accept();
                Runnable clientCore = new ClientCore(this.cyCServ, this.socket);
                Thread runClientCore = new Thread(clientCore);
                runClientCore.start();
            } catch (IOException e) {
                LOGGER.log(Level.INFO, "The communication fail with a client\nCause: ", e.getMessage());
            }
        }
    }

}
