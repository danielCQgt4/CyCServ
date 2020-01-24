package com.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CyCServ {

    // <editor-fold desc="Properties">
    private static final Logger LOGGER = Logger.getLogger("com.Server");
    private int port;
    private static CyCServ cyCServ;
    private static ServerSocket serverSocket;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    private CyCServ() {
        serverSocket = null;
        this.port = 80;
        // TODO handle the others properties
    }
    // </editor-fold>

    // <editor-fold desc="Getters and Setters">
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public CyCServ getCyCServ() {
        return cyCServ;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket _serverSocket) {
        serverSocket = _serverSocket;
    }

    // </editor-fold>

    // <editor-fold desc="Actions">
    public static CyCServ newInstance() {
        if (cyCServ == null) {
            cyCServ = new CyCServ();
        }
        return cyCServ;
    }

    public void listen() {
        try {
            serverSocket = new ServerSocket(this.port);
            Runnable r = new Core(this);
            Thread runCore = new Thread(r);
            runCore.start();
            String msj = "The server is running on port " + this.port +"\nAnd waiting for connections ...";
            LOGGER.log(Level.WARNING, msj);
        } catch (IOException e) {
            String msj = "The server fail during his creation \nCause: " + e.getMessage();
            LOGGER.log(Level.ALL, msj);
        }
    }
    // </editor-fold>

    // <editor-fold desc="Settings">
    // </editor-fold>
}
