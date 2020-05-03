package com.Server;

import com.Utils.Req;
import com.Utils.Res;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ClientCore implements Runnable {

    // <editor-fold desc="Attribbutes">
    private HashMap<Object, String> cache = new HashMap<>();
    private CyCServ cyCServ;
    private Socket socket;
    private Res respose;
    private Req request;
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public ClientCore(CyCServ cyCServ, Socket socket, HashMap<Object, String> cache) {
        this.cyCServ = cyCServ;
        this.socket = socket;
        this.cache = cache;
    }
    // </editor-fold>

    // <editor-fold desc="Actions">
    @Override
    public void run() {
        try {
            String s = this.cache.get(socket.getInetAddress());
            this.request = new Req(socket, this.cache.get(socket.getInetAddress()));
            this.respose = new Res(request, socket, cyCServ);
            if (this.request.isValidRequest()) {
                if (this.cache.get(socket.getInetAddress()) != null) {
                    this.respose.send("Hola desde cache");
                } else {
                    this.respose.send("Hola");
                }
            } else {
                respose.setStatus(400).sendFile(cyCServ.getError(400));
            }
            if (!this.request.getRequest().isEmpty()) {
                System.out.println("Update cache");
                
                this.cache.put(socket.getInetAddress(), request.getRequest());
            }
        } catch (IOException e) {
            System.err.println("The request has failed -> " + e);
        } finally {
            try {
                request.close();
            } catch (Exception e) {

            }
            try {
                respose.close();
            } catch (Exception e) {

            }
            try {
                this.socket.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    // </editor-fold>

}
