package Running;

import com.Handlers.CyCRouter;
import com.Utils.Req;
import com.Utils.Res;

public class Router implements CyCRouter{

    @Override
    public void handle(Req request, Res response, String method, String route) {
        switch (route) {
            case "/":
                response.send("hola mundo");
                break;
        }
    }

    @Override
    public void middleWares(Req request, Res response) {
        System.out.println("Middle");
    }
    
}
