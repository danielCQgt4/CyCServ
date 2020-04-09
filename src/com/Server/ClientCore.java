package com.Server;

import com.Utils.Req;
import com.Utils.Res;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCore implements Runnable {

    // <editor-fold desc="Attribbutes">
    private static final Logger LOGGER = Logger.getLogger("com.Server");
    private final Core.CoreBalancer balancer;
    private final CyCServ cyCServ;
    private final Socket socket;
    private Res respose;
    private Req request;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public ClientCore(CyCServ cyCServ, Socket socket, Core.CoreBalancer balancer) {
        this.cyCServ = cyCServ;
        this.socket = socket;
        this.balancer = balancer;
    }
    // </editor-fold>

    // <editor-fold desc="Actions">
    @Override
    public void run() {
        try {
            this.request = new Req(socket);
            //TODO Continue
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "The process during the communication FAIL {0}", e);
        } finally {
            try {
                this.socket.close();
                this.balancer.close(socket);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    // </editor-fold>

    // <editor-fold desc="Tools">
    // </editor-fold>
}
