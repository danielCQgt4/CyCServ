package Running;

import com.Handlers.CyCRouter;
import com.Utils.Req;
import com.Utils.Res;

public class Router extends CyCRouter {

    @Override
    public void routing() {
        get("/");
    }

    @Override
    public void handle(Req request, Res response, String method, String route) {
        switch (route) {
            case "/":
                String param = request.getParam("key");
                response.send("<h1>hola "+param+"</h1>");
                break;
        }
    }

    @Override
    public void middleWares(Req request, Res response) {
        //System.out.println("Middle");
    }

}
