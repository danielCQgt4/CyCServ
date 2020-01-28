package com.Handlers;

import com.Utils.Req;
import com.Utils.Res;

public interface CyCRouter {

    public void handle(Req request, Res response, String method, String route);
    
    public void middleWares(Req request, Res response);
}
