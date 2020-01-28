package com.Handlers;

import java.util.HashMap;

public final class FileHandler {

    private static FileHandler fileHandler;
    private final HashMap<String, String> imgExtentions = new HashMap<>();

    public static FileHandler newInstance() {
        if (fileHandler == null) {
            fileHandler = new FileHandler();
        }
        return fileHandler;
    }

    private FileHandler() {
        initContentDoc();
    }

    public void initContentDoc() {
        imgExtentions.put(".aac", "audio/aac");
        imgExtentions.put(".abw", "application/x-abiword");
        imgExtentions.put(".arc", "application/octet-stream");
        imgExtentions.put(".avi", "video/x-msvideo");
        imgExtentions.put(".azw", "application/vnd.amazon.ebook");
        imgExtentions.put(".bin", "application/octet-stream");
        imgExtentions.put(".bz", "application/x-bzip");
        imgExtentions.put(".bz2", "application/x-bzip2");
        imgExtentions.put(".csh", "application/x-csh");
        imgExtentions.put(".css", "text/css");
        imgExtentions.put(".csv", "text/csv");
        imgExtentions.put(".doc", "application/msword");
        imgExtentions.put(".epub", "application/epub+zip");
        imgExtentions.put(".htm", "text/html");
        imgExtentions.put(".html", "text/html");
        imgExtentions.put(".ico", "image/x-icon");
        imgExtentions.put(".jar", "application/java-archive");
        imgExtentions.put(".ics", "text/calendar");
        imgExtentions.put(".ics", "text/calendar");
        imgExtentions.put(".jpeg", "image/jpeg");
        imgExtentions.put(".jpg", "image/jpeg");
        imgExtentions.put(".js", "application/javascript");
        imgExtentions.put(".json", "application/json");
        imgExtentions.put(".mid", "audio/midi");
        imgExtentions.put(".midi", "audio/midi");
        imgExtentions.put(".mpeg", "video/mpeg");
        imgExtentions.put(".mpkg", "application/vnd.apple.installer+xml");
        imgExtentions.put(".odp", "application/vnd.oasis.opendocument.presentation");
        imgExtentions.put(".ods", "application/vnd.oasis.opendocument.spreadsheet");
        imgExtentions.put(".odt", "application/vnd.oasis.opendocument.text");
        imgExtentions.put(".oga", "audio/ogg");
        imgExtentions.put(".ogv", "video/ogg");
        imgExtentions.put(".ogx", "application/ogg");
        imgExtentions.put(".pdf", "application/pdf");
        imgExtentions.put(".ppt", "application/vnd.ms-powerpoint");
        imgExtentions.put(".rar", "application/x-rar-compressed");
        imgExtentions.put(".rtf", "application/rtf");
        imgExtentions.put(".sh", "application/x-sh");
        imgExtentions.put(".svg", "image/svg+xml");
        imgExtentions.put(".swf", "application/x-shockwave-flash");
        imgExtentions.put(".tar", "application/x-tar");
        imgExtentions.put(".tif", "image/tiff");
        imgExtentions.put(".tiff", "image/tiff");
        imgExtentions.put(".ttf", "font/ttf");
        imgExtentions.put(".vsd", "application/vnd.visio");
        imgExtentions.put(".wav", "audio/x-wav");
        imgExtentions.put(".weba", "audio/webm");
        imgExtentions.put(".webm", "video/webm");
        imgExtentions.put(".webp", "image/webp");
        imgExtentions.put(".woff", "font/woff");
        imgExtentions.put(".woff2", "font/woff2");
        imgExtentions.put(".xhtml", "application/xhtml+xml");
        imgExtentions.put(".xls", "application/vnd.ms-excel");
        imgExtentions.put(".xml", "application/xml");
        imgExtentions.put(".xul", "application/vnd.mozilla.xul+xml");
        imgExtentions.put(".zip", "application/zip");
        imgExtentions.put(".7z", "application/x-7z-compressed");

    }

    public String getContentTypeFromExtension(String ex){
        String content = this.imgExtentions.get(ex);
        if (content == null){
            content = "";
        }
        return content;
    }
}
