package com.Handlers;

import com.Utils.Req;
import com.Utils.Res;
import java.util.HashMap;

public abstract class CyCRouter {

    private final HashMap<String, String> routes = new HashMap<>();

    public void addRoute(String method, String route) {
        routes.put(route, method.toLowerCase());
    }
    
    public void post(String route){
        routes.put(route, "post");
    }
    
    public void get(String route){
        routes.put(route, "get");
    }

    public boolean validRoute(String route, String method) {
        String m = routes.get(route);
        if (m != null){
            return m.equals(method.toLowerCase());
        }
        return false;
    }

    public abstract void routing();

    public abstract void handle(Req request, Res response, String method, String route);

    public abstract void middleWares(Req request, Res response);
}
