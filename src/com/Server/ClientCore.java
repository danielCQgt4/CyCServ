package com.Server;

import com.Utils.Req;
import com.Utils.Res;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCore implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("com.Server");
    private final Socket socket;
    private Res respose;
    private Req request;
    private String idClient;
    private final CyCServ cyCServ = CyCServ.newInstance();

    public ClientCore(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.idClient = cyCServ.getRemoteAddress(this.socket);
            this.request = new Req(socket);
            this.respose = new Res(socket);
            this.respose.setRESPONSE_CODE(this.request.getRESPONSE_CODE());
            this.respose.setACCESS_CONTROL_ALLOW_ORIGIN("*"); //TODO Delete line
            if (this.respose.getRESPONSE_CODE() == 400) {
                String last = Connections.getLastReq(idClient);
                if (last != null) {
                    this.respose.sendHttpResponse(last);
                } else {
                    this.badRequest();
                }
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
                Connections.addConnection(idClient, this.respose.getLasResponse());
            }
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "The process during the communication FAIL {0}", e);
        } finally {
            try {
                this.request.getInputStream().close();
            } catch (IOException | NullPointerException ex) {
            }
            try {
                this.respose.getOutputStream().close();
            } catch (IOException | NullPointerException ex) {
            }
            try {
                if (this.socket.isConnected()) {
                    this.socket.close();
                }
            } catch (IOException ex) {
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

    private static class Connections {

        private final static HashMap<String, String> CONNECTIONS = new HashMap<>();

        public static void addConnection(String idClient, String lastReq) {
            CONNECTIONS.put(idClient, lastReq);
        }

        public static String getLastReq(String idClient) {
            String lastReq = CONNECTIONS.get(idClient);
            if (lastReq != null) {
                return lastReq;
            }
            return null;
        }
    }
}
