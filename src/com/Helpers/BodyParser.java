package com.Helpers;

import com.Models.CyCBody;
import com.Models.CyCParams;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

public final class BodyParser {

    // <editor-fold desc="Attributes">
    private final String body;
    private final byte[] bodyBytes;//To handle files
    private final CyCParams<Object, Object> params;
    private final String contentType;
    private final CyCBody cyCBody;
    // </editor-fold>

    // <editor-fold desc="Constructor">
    private BodyParser(String contentType, String body, byte[] bodyBytes) {
        this.params = new CyCParams<>();
        this.cyCBody = new CyCBody(params);
        this.body = body;
        this.bodyBytes = bodyBytes;
        this.contentType = contentType;
        this.filterArea();
    }

    private CyCBody getCyCBody() {
        return this.cyCBody;
    }

    public static CyCBody Build(String contentType, String body, byte[] bodyBytes) {
        if (contentType == null) {
            contentType = "application/x-www-form-urlencoded";
        }
        BodyParser bodyParser = new BodyParser(contentType, body, bodyBytes);
        return bodyParser.getCyCBody();
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
                json();
                this.params.forEach((k, v) -> {
                    System.out.println(k + ":" + v);
                });
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

    // <editor-fold desc="json">
    private void json() {
        if (this.body != null) {
            String file = "src\\com\\jsparser\\jsonParser.js";
            ScriptObjectMirror o = (ScriptObjectMirror) execJs(file, "isJson", this.body);
            if (o != null) {
                initJson(file, o);
            } else {
                System.err.println("Error parsing data from JSON");
            }
        }
    }

    private void initJson(String file, ScriptObjectMirror json) {
        if (json.isArray()) {
            decodedArrayJson(json, this.params);
        } else {
            decodeObjectJson(json, this.params);
        }
    }

    private void decodeObjectJson(ScriptObjectMirror json, HashMap<Object, Object> parent) {
        String[] keys = json.getOwnKeys(true);
        for (String key : keys) {
            try {
                Object k = json.get(key);
                if (k instanceof String || k instanceof Character || k instanceof Byte || k instanceof Boolean
                        || k instanceof Short || k instanceof Integer || k instanceof Double || k instanceof Float
                        || k instanceof Long) {
                    parent.put(key, json.get(key));
                } else {
                    if (((ScriptObjectMirror) k).isArray()) {
                        HashMap<Object, Object> subP = new HashMap<>();
                        parent.put(key, subP);
                        decodedArrayJson(json, subP);
                    } else {
                        HashMap<Object, Object> subP = new HashMap<>();
                        parent.put(key, subP);
                        decodeObjectJson((ScriptObjectMirror) k, subP);
                    }
                }
            } catch (Exception e) {
                parent.put(key, null);
            }
        }
    }

    private void decodedArrayJson(ScriptObjectMirror json, HashMap<Object, Object> parent) {
        int t = json.size();
        for (int i = 0; i < t; i++) {
            try {
                Object k = json.getSlot(i);
                if (((ScriptObjectMirror) k).isArray()) {
                    HashMap<Object, Object> subP = new HashMap<>();
                    parent.put(i, subP);
                    decodedArrayJson(json, subP);
                } else {
                    HashMap<Object, Object> subP = new HashMap<>();
                    parent.put(i, subP);
                    decodeObjectJson((ScriptObjectMirror) k, subP);
                }
            } catch (Exception e) {
                parent.put(i, null);
            }
        }
    }

    private Object execJs(String file, String function, Object p) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            engine.eval(Files.newBufferedReader(Paths.get(file), StandardCharsets.UTF_8));
            Invocable inv = (Invocable) engine;
            Object o = inv.invokeFunction(function, p);
            return o;
        } catch (IOException | NoSuchMethodException | ScriptException e) {
            System.out.println(e);
            return null;
        }
    }
    // </editor-fold>
}
