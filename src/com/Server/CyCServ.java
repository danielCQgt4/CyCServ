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
import java.util.HashMap;
import java.util.stream.Collectors;

public final class CyCServ {

    // <editor-fold desc="Properties">
    private final HashMap<Integer, String> errors;
    private final FileHandler fileHandler;
    private ServerSocket serverSocket;
    private static Logger LOGGER;
    private final int MAX_PORT;
    private final int MIN_PORT;
    private CyCRouter router;
    private Thread runCore;
    private String host;
    private int port;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public CyCServ() {
        this.serverSocket = null;
        this.port = 80;
        this.errors = new HashMap<>();
        this.fileHandler = FileHandler.newInstance();
        CyCServ.LOGGER = Logger.getLogger("com.Server");
        this.setDefaultErrors();
        this.MAX_PORT = 65535;
        this.MIN_PORT = 1;
    }

    private void setDefaultErrors() {
        this.errors.put(400, "./com/StaticContent/400.html");
        this.errors.put(404, "./com/StaticContent/404.html");
    }
    // </editor-fold>

    // <editor-fold desc="Getters and Setters">
    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) throws Exception {
        if (port <= MAX_PORT && MIN_PORT <= port) {
            this.port = port;
        } else {
            throw new Exception("Cannot be set the port " + port);
        }
    }

    public CyCRouter getRouter() {
        return this.router;
    }

    public void setRouter(CyCRouter router) {
        this.router = router;
    }

    public void setError(int httpError, String render) {
        this.errors.put(httpError, render);
    }

    public String getError(int httpError) {
        String render = this.errors.get(httpError);
        return render != null ? render : "No render route";
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }
    // </editor-fold>

    // <editor-fold desc="Actions">
    private CyCServResult startServer() {
        try {
            if (this.getPort() <= MAX_PORT && MIN_PORT <= this.getPort()) {
                throw new CyCServError("Cannot be set the port " + this.getPort());
            }
            serverSocket = new ServerSocket(this.getPort());
            this.host = serverSocket.getInetAddress() + ":" + this.getPort();
            if (this.runCore != null) {
                if (this.runCore.isAlive()) {
                    throw new CyCServError("This server is already running in the port " + this.getPort());
                }
            }
            this.runCore = new Thread(new Core(this, serverSocket));
            runCore.start();
            return new CyCServResult(runCore.isAlive(), null);
        } catch (CyCServError e) {
            return new CyCServResult(false, "Server error: \n" + e.getMessage());
        } catch (IOException eio) {
            return new CyCServResult(false, "Server error: \n" + eio.getMessage());
        }
    }

    public CyCServResult listen() {
        return this.startServer();
    }

    public CyCServResult listen(int port) throws Exception {
        this.setPort(port);
        return this.startServer();
    }

    public void listen(final ICyCServ cb) {
        new Thread(() -> {
            CyCServResult r = startServer();
            cb.onComplete(r.started, r.error);
        }).start();
    }

    public void listen(int port, final ICyCServ cb){
        new Thread(() -> {
            try {
                CyCServ.this.setPort(port);
                CyCServResult r = startServer();
                cb.onComplete(r.started, r.error);
            } catch (Exception e) {
                cb.onComplete(false, "Cannot be set the port " + port);
            }
        }).start();
    }

    public String readFile(String path) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
        String s = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        return s;
    }
    // </editor-fold>

    //<editor-fold desc="Async code">
    public interface ICyCServ {

        void onComplete(boolean active, String error);
    }

    public class CyCServResult {

        private final boolean started;
        private final String error;

        public CyCServResult(boolean started, String error) {
            this.started = started;
            this.error = error;
        }

        public boolean isStarted() {
            return started;
        }

        public String getError() {
            return error;
        }

    }

    private class CyCServError extends IOException {

        private String message;

        public CyCServError() {
        }

        public CyCServError(String message) {
            super(message);
            this.message = message;
        }

        public void setMessageFromError(int errCode) {
            switch (errCode) {
                //TODO Completar
                default:
                    this.message = "Uknown error";
            }
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return this.message;
        }

    }
    //</editor-fold>
}
