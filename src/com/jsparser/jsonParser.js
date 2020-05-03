function isJson(body) {
    try {
        var t = JSON.parse(body);
        return t;
    } catch (e) {
        return null;
    }
}
