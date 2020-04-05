package com.Server;

import com.Utils.Req;
import com.Utils.Res;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCore implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("com.Server");
    private final Socket socket;
    private Res respose;
    private Req request;
    private final CyCServ cyCServ = CyCServ.newInstance();

    public ClientCore(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.request = new Req(socket);
            this.respose = new Res(socket);
            this.respose.setRESPONSE_CODE(this.request.getRESPONSE_CODE());
            this.respose.setACCESS_CONTROL_ALLOW_ORIGIN("*"); //TODO Delete line
            if (this.respose.getRESPONSE_CODE() == 0) {
                this.respose.setRESPONSE_CODE(400);
            }
            if (this.respose.getRESPONSE_CODE() == 400) {
                this.badRequest();
            } else {
                if (cyCServ.getRouter() != null) {
                    cyCServ.getRouter().middleWares(
                            this.request,
                            this.respose
                    );
                    cyCServ.getRouter().routing();
                    if (cyCServ.getRouter().validRoute(this.request.getRoute(), this.request.getMethod())) {
                        this.respose.setRESPONSE_CODE(200);
                        cyCServ.getRouter().handle(
                                this.request,
                                this.respose,
                                this.request.getMethod(),
                                this.request.getRoute()
                        );
                    } else {
                        this.notFound();
                    }
                } else {
                    this.notFound();
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "The process during the communication FAIL {0}", e);
        } finally {
            try {
                this.request.getInputStream().close();
            } catch (IOException | NullPointerException ex) {
                System.out.println(ex);
            }
            try {
                this.respose.getOutputStream().close();
            } catch (IOException | NullPointerException ex) {
                System.out.println(ex);
            }
            try {
                this.socket.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    private void badRequest() throws IOException {
        this.respose.setRESPONSE_CODE(400);
        String body = cyCServ.readFile(cyCServ.getError400());
        respose.send(body);
    }

    private void notFound() throws IOException {
        this.respose.setRESPONSE_CODE(404);
        String body = cyCServ.readFile(cyCServ.getError404());
        respose.send(body);
    }

}
