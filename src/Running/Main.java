package Running;

import com.Server.CyCServ;

public class Main {

    public static void main(String[] args) {
        CyCServ cyCServ = CyCServ.newInstance();
        cyCServ.listen();
    }
}
