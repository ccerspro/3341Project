import java.util.HashMap;
import java.util.Map;

public class FunctionMap {
    private static Map<String, Function> funcMap = new HashMap<>();

    public static void addFunction(String name, Function func) {
        funcMap.put(name, func);
    }

    public static Function getFunction(String name) {
        return funcMap.get(name);
    }

    public static boolean hasFunction(String name) {
        return funcMap.containsKey(name);
    }

    public static Map<String, Function> getFuncMap() {
        return funcMap;
    }
}