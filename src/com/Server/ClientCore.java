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
            if (this.respose.getRESPONSE_CODE() == 400) {
                this.badRequest();
            } else {
                if (cyCServ.getRouter() != null) {
                    cyCServ.getRouter().middleWares(
                            this.request,
                            this.respose
                    );
                    cyCServ.getRouter().handle(
                            this.request,
                            this.respose,
                            this.request.getMethod(),
                            this.request.getRoute()
                    );
                } else {
                    this.respose.setRESPONSE_CODE(404);
                    this.respose.send("<h1>" + this.respose.getHttpReplie(404) + "</h1>");
                }
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
        String bodyRes = "<!DOCTYPE html>"
                + "<html>"
                + "    <head>"
                + "        <title>Title</title>"
                + "        <meta charset=\"UTF-8\">"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "    </head>"
                + "    <body>"
                + "        <h1>Bad request</h1>"
                + "    </body>"
                + "</html>";
        respose.send(bodyRes);
    }
}
