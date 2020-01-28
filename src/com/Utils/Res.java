package com.Utils;

import com.Helpers.CyCServResponse;
import com.Helpers.HttpIdentifiers;
import com.Server.CyCServ;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Res extends HttpIdentifiers implements CyCServResponse {

    private String httpContent;
    private final CyCServ cyCServ = CyCServ.newInstance();

    // <editor-fold desc="Constructor">
    public Res(Socket socket) throws IOException {
        super(socket, false);
    }
    // </editor-fold>

    // <editor-fold desc="Actions">
    public void sendHttpResponse(String httpResponse) {
        String respose = httpResponse;
        try {
            this.getOutputStream().write(respose.getBytes());
        } catch (IOException e) {
            //LOGGER.log(Level.INFO, "Error while sending response {0}", e);
        }
    }

    private String composeHttpResponse(String body) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getHTTP_VERSION()).append(" ");
        stringBuilder.append(super.getRESPONSE_CODE()).append(" ");
        stringBuilder.append(super.getHttpReplie(super.getRESPONSE_CODE())).append(" ");
        stringBuilder.append(this.getCRLF());
        //Headers
        stringBuilder.append(" ").append("\n");
        this.setCONTENT_LENGHT(body);
        super.addHeader(this.getCONTENT_TYPE());
        this.getHeaders().forEach((k, v)
                -> stringBuilder.append(k)
                        .append(": ")
                        .append(v)
                        .append("\n")
        );
        stringBuilder.append(this.getACCESS_CONTROL_ALLOW_ORIGIN()).append("\n\r\n");
        //Body
        stringBuilder.append(body);
        stringBuilder.append(this.getCRLF());
        stringBuilder.append(this.getCRLF());
        //System.out.println(stringBuilder.toString());
        this.httpContent = stringBuilder.toString();
        return stringBuilder.toString();
    }

    public String send(String body) {
        String respose = composeHttpResponse(body);
        try {
            this.getOutputStream().write(respose.getBytes());
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Error while sending response {0}", e);
        }
        return respose;
    }

    public String sendText(String text) {
        this.setCONTENT_TYPE("text/plain");
        String respose = composeHttpResponse(text);
        try {
            this.getOutputStream().write(respose.getBytes());
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Error while sending response {0}", e);
        }
        return respose;
    }

    public String sendJson(String json) {
        this.setCONTENT_TYPE("application/json");
        String respose = composeHttpResponse(json);
        try {
            this.getOutputStream().write(respose.getBytes());
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Error while sending response {0}", e);
        }
        return respose;
    }

    public void sendFile(String path) {
        try {
//            System.out.println(cyCServ.getFileHandler().getContentTypeFromExtension(path.substring(path.indexOf("."))));
//            this.addHeader(cyCServ.getFileHandler().getContentTypeFromExtension(path.substring(path.indexOf("."))));
            File file = new File(path);
            byte[] byFile = new byte[(int) file.length()];
            FileInputStream inFile = new FileInputStream(file);
            BufferedInputStream burFile = new BufferedInputStream(inFile);
            burFile.read(byFile, 0, byFile.length);
            this.getOutputStream().write(byFile, 0, byFile.length);
            this.getOutputStream().flush();
        } catch (FileNotFoundException ex) {
            this.setRESPONSE_CODE(404);
            LOGGER.log(Level.INFO, "Error while finding file {0}", ex);
        } catch (IOException ex) {
            Logger.getLogger(Res.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() throws IOException {
        this.getOutputStream().close();
    }
    // </editor-fold>

    // <editor-fold desc="Getter and Setters">
    public String getLasResponse() {
        return this.httpContent;
    }

    @Override
    public OutputStream getOutputStream() {
        return super.outpuStream;
    }

    @Override
    public String getCRLF() {
        return super.CRLF;
    }

    @Override
    public String getHTTP_VERSION() {
        return super.HTTP_VERSION;
    }

    @Override
    public String getRESPONSE_MESSAGE() {
        return super.RESPONSE_MESSAGE;
    }

    @Override
    public String getCONTENT_LENGHT() {
        return super.CONTENT_LENGHT;
    }

    @Override
    public String getCONTENT_TYPE() {
        return super.CONTENT_TYPE;
    }

    @Override
    public String getACCESS_CONTROL_ALLOW_ORIGIN() {
        return super.ACCESS_CONTROL_ALLOW_ORIGIN;
    }

    @Override
    public void setCONTENT_LENGHT(String content) {
        content = content.toLowerCase();
        content = content.replace("content-length:", "");
        super.CONTENT_LENGHT = "content-length: " + content.length();
        super.addHeader(super.CONTENT_LENGHT);
    }

    @Override
    public void setCONTENT_TYPE(String contentType) {
        contentType = contentType.toLowerCase();
        contentType = contentType.replace("content-type:", "");
        super.CONTENT_TYPE = "content-type: " + contentType;
        super.addHeader(super.CONTENT_TYPE);
    }

    @Override
    public void setACCESS_CONTROL_ALLOW_ORIGIN(String url) {
        url = url.toLowerCase();
        url = url.replace("access-control-allow-origin:", "");
        super.ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin: " + url;
    }
    // </editor-fold>
}
