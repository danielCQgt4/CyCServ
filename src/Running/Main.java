package Running;

import com.Server.CyCServ;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.script.*;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class Main {

    public static void main(String[] args) {
        initServer();

//        System.out.println();
    }

    public static void test(String text) {
        text = text.replaceAll("\\+", " ");
        System.out.println(text);
    }

    public static void initServer() {
        try {
            CyCServ cyCServ = new CyCServ();
//            cyCServ.setRouter(new Router());
            cyCServ.setError(404, "test.html");
            CyCServ.CyCServResult r = cyCServ.listen(8080);
            cyCServ.setMaxConnections(100);
            if (!r.isStarted()) {
                System.out.println(r.getError());
            } else {
                System.out.println("Server on port -> " + cyCServ.getPort());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
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

    public static void jsRun() throws IOException, ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
// read script file
        engine.eval(Files.newBufferedReader(Paths.get("D:\\Projects\\JavaProjects\\Desktop\\CyCServ\\src\\com\\StaticContent\\temp.js"), StandardCharsets.UTF_8));

        Invocable inv = (Invocable) engine;
// call function from script file
        Object o = inv.invokeFunction("temp", 1);
        if (o instanceof ScriptObjectMirror) {
            System.out.println("json");
        } else {
            System.out.println(o);
        }
    }
}
