package com.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Core implements Runnable {

    // <editor-fold desc="Attributes">
    private static final Logger LOGGER = Logger.getLogger("com.Server");
    private final ServerSocket serverSocket;
    private final CoreBalancer balancer;
    private final CyCServ cyCServ;
    private Socket socket;
    // </editor-fold>

    // <editor-fold desc="Contructors">
    public Core(CyCServ cyCServ, ServerSocket serverSocket) {
        this.cyCServ = cyCServ;
        this.serverSocket = serverSocket;
        this.balancer = new CoreBalancer(cyCServ.getMaxConnections());
    }
    // </editor-fold>

    // <editor-fold desc="Actions">
    @Override
    public void run() {
        while (true) {
            try {
                this.socket = this.serverSocket.accept();
                if (this.balancer.isAccept()) {
                    new Thread(new ClientCore(this.cyCServ, this.socket, this.balancer)).start();
                } else {
                    this.socket.getOutputStream().write("Max connections".getBytes());
                    try {
                        this.socket.close();
                    } catch (IOException ex) {
                    }
                    LOGGER.log(Level.INFO, "The server have the max connections ", this.cyCServ.getMaxConnections());
                }
            } catch (IOException e) {
                System.out.println();
                LOGGER.log(Level.INFO, "The communication fail with a client\nCause: ", e.getMessage());
            }
        }
    }
// </editor-fold>

    // <editor-fold desc="Tools">
    public class CoreBalancer {

        private int upTimeConnections;
        private final int maxConnections;

        public CoreBalancer(int maxConnections) {
            this.maxConnections = maxConnections;
        }

        public int getUpTimeConnections() {
            return upTimeConnections;
        }
        
        private void decrease() {
            this.upTimeConnections--;
            if (this.upTimeConnections < 0) {
                this.upTimeConnections = 0;
            }
        }

        private boolean increase() {
            if (this.upTimeConnections > maxConnections) {
                this.upTimeConnections = maxConnections;
                return false;
            } else {
                this.upTimeConnections++;
                return true;
            }
        }

        public boolean isAccept() {
            return this.increase();
        }

        public boolean close(Socket so) {
            if (so.isClosed()) {
                this.decrease();
                return true;
            } else {
                return false;
            }
        }
    }
    // </editor-fold>
}
