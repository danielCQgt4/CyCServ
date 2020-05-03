package com.Models;

import java.util.HashMap;

public class CyCParams<K, V> extends HashMap<K, V> {

    public Object getData(Object k) {
        return this.get(k);
    }

    public CyCParams<Object, Object> getObject(Object k) {
        CyCParams<Object, Object> re = new CyCParams<>();
        ((HashMap<Object, Object>) this.getData(k)).forEach((key, value) -> {
            re.put(key, value);
        });
        return re;
    }
}
