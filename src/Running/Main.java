package Running;

import com.Server.CyCServ;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        try {
            CyCServ cyCServ = new CyCServ();
//            cyCServ.setRouter(new Router());
            CyCServ.CyCServResult r = cyCServ.listen(3000);
            if (!r.isStarted()) {
                System.out.println(r.getError());
            } else {
                System.out.println("Server on port -> " + cyCServ.getPort());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void test(String text) {
        text = text.replaceAll("\\+", " ");
        System.out.println(text);
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
