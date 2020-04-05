package com.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.Handlers.CyCRouter;
import com.Handlers.FileHandler;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.stream.Collectors;

public class CyCServ {

    // <editor-fold desc="Properties">
    private static final Logger LOGGER = Logger.getLogger("com.Server");
    private ServerSocket serverSocket;
    private String host;
    private int port;
    private CyCRouter router;
    private String viewPath;
    private String error404 = "./com/StaticContent/404.html";
    private String error400 = "./com/StaticContent/400.html";
    private final FileHandler fileHandler = FileHandler.newInstance();
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public CyCServ() {
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

    public CyCRouter getRouter() {
        return this.router;
    }

    public void setRouter(CyCRouter router) {
        this.router = router;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getError404() {
        return error404;
    }

    public void setError404(String error404) {
        this.error404 = error404;
    }

    public String getError400() {
        return error400;
    }

    public void setError400(String error400) {
        this.error400 = error400;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }
    // </editor-fold>

    // <editor-fold desc="Actions">
    public void listen() {
        try {
            serverSocket = new ServerSocket(this.getPort());
            this.host = serverSocket.getInetAddress() + ":" + this.port;
            Thread runCore = new Thread(new Core(this, serverSocket));
            runCore.start();
            StringBuilder msj = new StringBuilder("The server is running on port ");
            msj.append(this.getPort()).append("\nAnd waiting for connections ...");
            System.out.println(msj.toString());
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

    public String readFile(String path) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
        String s = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        return s;
    }
    // </editor-fold>
}
