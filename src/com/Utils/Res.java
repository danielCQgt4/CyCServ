package com.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;

public class Res {

    // <editor-fold desc="Attributtes">
    private Req request;
    private OutputStream out;
    private int status;
    private String CRLF;
    private String HTTP_VERSION;
    private HashMap<String, Object> headers;
    private static final Object[][] HTTPREPLIES = {
        {100, "Continue"},
        {101, "Switching Protocols"},
        {200, "OK"},
        {201, "Created"},
        {202, "Accepted"},
        {203, "Non-Authoritative Information"},
        {204, "No Content"},
        {205, "Reset Content"},
        {206, "Partial Content"},
        {300, "Multiple Choices"},
        {301, "Moved Permanently"},
        {302, "Found"},
        {303, "See Other"},
        {304, "Not Modified"},
        {305, "Use Proxy"},
        {306, "(Unused)"},
        {307, "Temporary Redirect"},
        {400, "Bad Request"},
        {401, "Unauthorized"},
        {402, "Payment Required"},
        {403, "Forbidden"},
        {404, "Not Found"},
        {405, "Method Not Allowed"},
        {406, "Not Acceptable"},
        {407, "Proxy Authentication Required"},
        {408, "Request Timeout"},
        {409, "Conflict"},
        {410, "Gone"},
        {411, "Length Required"},
        {412, "Precondition Failed"},
        {413, "Request Entity Too Large"},
        {414, "Request-URI Too Long"},
        {415, "Unsupported Media Type"},
        {416, "Requested Range Not Satisfiable"},
        {417, "Expectation Failed"},
        {500, "Internal Server Error"},
        {501, "Not Implemented"},
        {502, "Bad Gateway"},
        {503, "Service Unavailable"},
        {504, "Gateway Timeout"},
        {505, "HTTP Version Not Supported"}};
    // </editor-fold>

    // <editor-fold desc="Constructors">
    public Res(Req request, Socket socket) throws IOException {
        this.request = request;
        this.out = socket.getOutputStream();
        this.CRLF = "\r\n";
        this.HTTP_VERSION = "HTTP/1.1";
        this.headers = new HashMap<>();
        this.init();
    }
    // </editor-fold>

    // <editor-fold desc="FINAL ACTIONS">\
    private void init() {
        this.preData();
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    private void preData() {
        this.headers.put("Server", "CyCServer v0.0.1");
        this.headers.put("Date", getDate());
        this.headers.put("Connection", "Closed");
        this.headers.put("Access-Control-Allow-Origin", "*");
    }
    // </editor-fold>

    // <editor-fold desc="Actions">
    public Res setStatus(int status) {
        this.status = status;
        return this;
    }

    private String getMessageStatus(int code) {
        for (Object[] obj : HTTPREPLIES) {
            if ((int) obj[0] == code) {
                return obj[1].toString();
            }
        }
        return "Bad Request";
    }

    private String getResponseLine() {
        StringBuilder resLine = new StringBuilder();
        resLine.append(this.HTTP_VERSION);
        resLine.append(" ");
        resLine.append(status);
        resLine.append(" ");
        resLine.append(this.getMessageStatus(status));
        resLine.append(this.CRLF);
        try {
            Thread.sleep(15);
        } catch (InterruptedException ex) {

        }
        return resLine.toString();
    }

    private String getHeadersResponse() {
        StringBuilder builder = new StringBuilder();
        this.headers.forEach((k, v) -> {
            builder.append(k)
                    .append(": ")
                    .append(v)
                    .append(this.CRLF);
        });
        return builder.toString();
    }

    private byte[] composeResponse() {
        StringBuilder composed = new StringBuilder("");
        // <editor-fold desc="RESPONSE_LINE">
        composed.append(getResponseLine());
        // </editor-fold>
        // <editor-fold desc="HEADERS">
        composed.append(getHeadersResponse());
        // </editor-fold> 
        // <editor-fold desc="name">
        composed.append(CRLF);
        // </editor-fold>
        return composed.toString().getBytes();
    }

    private void send(byte[] data) {
        try {
            byte[] top = composeResponse();
            this.headers.put("Content-Length", data.length);
            this.headers.put("Content-Encoding", "gzip");
            byte[] allResponse = new byte[top.length + data.length];
            ByteBuffer process = ByteBuffer.wrap(allResponse);
            process.put(top);
            process.put(data);
            allResponse = process.array();
            out.write(allResponse);
            out.flush();
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void sendText(String text) {
        this.headers.put("Content-Type", "text/plain");
        send(text.getBytes());
    }
    // </editor-fold>
}
