import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CallStack {
    private static Stack<Map<String, int[]>> frames = new Stack<>();
    
    public static void pushFrame(Map<String, int[]> frame) {
        frames.push(new HashMap<>(frame));
    }
    
    public static Map<String, int[]> popFrame() {
        if (frames.isEmpty()) {
            System.out.println("ERROR: Call stack is empty");
            System.exit(1);
        }
        return frames.pop();
    }
    
    public static Map<String, int[]> peekFrame() {
        if (frames.isEmpty()) {
            System.out.println("ERROR: Call stack is empty");
            System.exit(1);
        }
        return frames.peek();
    }
    
    public static boolean isEmpty() {
        return frames.isEmpty();
    }
    
    public static void clear() {
        frames.clear();
    }
}