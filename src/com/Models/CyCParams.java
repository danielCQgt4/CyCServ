package com.Models;

import java.util.HashMap;

public class CyCParams<K, V> extends HashMap<K, V> {

    public Object getData(Object k) {
        Object o = this.get(k);
        if (o == null) {
            return "[no-data]";
        } else {
            return o;
        }
    }

    public CyCParams<Object, Object> getObject(Object k) {
        CyCParams<Object, Object> re = new CyCParams<>();
        try {
            ((HashMap<Object, Object>) this.getData(k)).forEach((key, value) -> {
                re.put(key, value);
            });
        } catch (Exception e) {
            System.out.println(k + "->[no-object]");
        } finally {
            return re;
        }
    }
}
