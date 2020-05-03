package com.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Core implements Runnable {

    // <editor-fold desc="Attributes">
    private HashMap<Object, String> cache = new HashMap<>();
    private ServerSocket serverSocket;
    private CyCServ cyCServ;
    private Socket socket;

    private final ExecutorService pool;
    // </editor-fold>

    // <editor-fold desc="Contructors">
    public Core(CyCServ cyCServ, ServerSocket serverSocket) {
        this.cyCServ = cyCServ;
        this.serverSocket = serverSocket;
        this.pool = Executors.newFixedThreadPool(cyCServ.getMaxConnections());
    }
    // </editor-fold>

    // <editor-fold desc="Actions">
    @Override
    public void run() {
        while (true) {
            try {
                this.socket = this.serverSocket.accept();
                pool.execute(new ClientCore(this.cyCServ, this.socket, this.cache));
            } catch (IOException e) {
                System.out.println("Error handling client ->" + e);
            }
        }
    }
// </editor-fold>

}
