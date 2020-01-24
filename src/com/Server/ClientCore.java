package com.Server;

import com.Utils.Req;
import com.Utils.Res;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCore implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("com.Server");
    private final Socket socket;
    private Res respose;
    private Req request;

    public ClientCore(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.request = new Req(socket.getInputStream());
            this.respose = new Res(socket.getOutputStream());
            String bodyRes = "<!DOCTYPE html>"
                    + "<html>"
                    + "    <head>"
                    + "        <title>Title</title>"
                    + "        <meta charset=\"UTF-8\">"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                    + "    </head>"
                    + "    <body>"
                    + "        <h1>HelloWorld</h1>"
                    + "        <p>" + this.request.getData() + "</p>"
                    + "    </body>"
                    + "</html>";
            respose.send(bodyRes);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "The process during the communication FAIL {0}", e);
        } finally {
            try {
                if (this.request.getInputStream() != null) {
                    this.request.getInputStream().close();
                }
            } catch (IOException ex) {
            }
            try {
                if (this.respose.getOutputStream() != null) {
                    this.respose.getOutputStream().close();
                }
            } catch (IOException ex) {
            }
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (IOException ex) {
            }
        }
    }

}
