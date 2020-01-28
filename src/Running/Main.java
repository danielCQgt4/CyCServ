package Running;

import com.Server.CyCServ;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
//        Temp temp = new Temp();
//        temp.tasto();
        CyCServ cyCServ = CyCServ.newInstance();
        cyCServ.listen();
    }

    public static class Temp {

        public void tasto() {
            System.out.println(System.getProperty("java.class.path"));
            InputStream in = this.getClass().getClassLoader()
                    .getResourceAsStream(System.getProperty("java.class.path") + "/index.html");
            String s = new BufferedReader(new InputStreamReader(in))
                    .lines().collect(Collectors.joining("\n"));
            System.out.println(s);
        }
    }
}
