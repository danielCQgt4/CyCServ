package com.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.Handlers.CyCRouter;

public class CyCServ {

    // <editor-fold desc="Properties">
    private static final Logger LOGGER = Logger.getLogger("com.Server");
    private static ServerSocket serverSocket;
    private static CyCServ cyCServ;
    private String host;
    private int port;
    private CyCRouter router;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    private CyCServ() {
        serverSocket = null;
        this.port = 80;
        // TODO handle the others properties
    }
    // </editor-fold>

    // <editor-fold desc="Getters and Setters">
    public String getHost() {
        return this.host;
    }

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
    
    public CyCRouter getRouter(){
        return this.router;
    }
    
    public void setRouter(CyCRouter router){
        this.router = router;
    }
    // </editor-fold>

    // <editor-fold desc="Actions">
    public void listen() {
        try {
            serverSocket = new ServerSocket(this.getPort());
            this.host = serverSocket.getInetAddress() + ":" + this.port;
            Thread runCore = new Thread(new Core(this));
            runCore.start();
            StringBuilder msj = new StringBuilder("The server is running on port ");
            msj.append(this.getPort())
                    .append("\nAnd waiting for connections ...");
            LOGGER.log(Level.INFO, msj.toString());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "The port {0} is not valid ", this.getPort());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "The server fail during his creation \nCause: ", e.getMessage());
        }
    }

    public void listen(int port) {
        this.port = port;
        this.listen();
    }
    // </editor-fold>

    // <editor-fold desc="Settings">
    public static CyCServ newInstance() {
        if (cyCServ == null) {
            cyCServ = new CyCServ();
        }
        return cyCServ;
    }
    // </editor-fold>
}
