package com.Helpers;

import com.Models.CyCBody;
import java.util.HashMap;

public final class BodyParser {

    // <editor-fold desc="Attributes">
    private final String body;
    private final byte[] bodyBytes;//To handle files
    private final HashMap<String, Object> params;
    private final String contentType;
    private final CyCBody cyCBody;
    private static BodyParser bodyParser;
    // </editor-fold>

    // <editor-fold desc="Constructor">
    private BodyParser(CyCBody cyCBody, String contentType, String body, byte[] bodyBytes) {
        this.params = new HashMap<>();
        cyCBody = new CyCBody(params);
        this.cyCBody = cyCBody;
        this.body = body;
        this.bodyBytes = bodyBytes;
        this.contentType = contentType;
        this.filterArea();
    }

    public static synchronized BodyParser Build(CyCBody cyCBody, String contentType, String body, byte[] bodyBytes) {
        if (bodyParser == null) {
            bodyParser = new BodyParser(cyCBody, contentType, body, bodyBytes);
        }
        return bodyParser;
    }
    // </editor-fold>

    // <editor-fold desc="MainsAndFilterByArea">
    private void filterArea() {
        if (this.contentType != null) {
            if (this.contentType.startsWith("application")) {
                this.applicationFilter();
            } else if (this.contentType.startsWith("other")) {
                //Handle later
            }
        }
    }

    private void applicationFilter() {
        /*
        application/x-www-form-urlencoded -> DEFAULT
        application/json
        application/xml
        application/pdf
        application/javascript
        application/xhtml+xml
        
        application/EDI-X12
        application/EDIFACT
        application/octet-stream
        application/ogg
        application/x-shockwave-flash
        application/ld+json
        application/zip
        
         */
        switch (this.contentType) {
            case "application/json":
                //NOT SUPPORTED
                break;
            case "application/xml":
                //NOT SUPPORTED
                break;
            case "application/pdf":
                //NOT SUPPORTED
                break;
            case "application/javascript":
                //NOT SUPPORTED
                break;
            case "application/xhtml+xml":
                //NOT SUPPORTED
                break;
            default:
                this.formUrlencoded();
        }
    }
    // </editor-fold>

    // <editor-fold desc="x-www-form-urlencoded">
    private void formUrlencoded() {
        if (this.body != null) {
            String[] keyValues = this.body.split("&");
            for (String keyValue : keyValues) {
                String[] parts = keyValue.split("=", 2);
                if (parts.length == 2) {
                    this.params.put(parts[0], parts[1]);
                }
            }
        }
    }
    // </editor-fold>
}
