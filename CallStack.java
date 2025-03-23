import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * CallStack provides a static stack-based mechanism to manage execution frames.
 * Each frame is a mapping of variable names to their associated memory blocks (int arrays).
 */
public class CallStack {
    private static final Stack<Map<String, int[]>> frameStack = new Stack<>();

    
    public static void pushFrame(Map<String, int[]> frame) {
        frameStack.push(new HashMap<>(frame));
    }

    
    public static Map<String, int[]> popFrame() {
        validateStackNotEmpty("pop");
        return frameStack.pop();
    }

    
    public static Map<String, int[]> peekFrame() {
        validateStackNotEmpty("peek");
        return frameStack.peek();
    }

    
    public static boolean isEmpty() {
        return frameStack.isEmpty();
    }

    
    public static void clear() {
        frameStack.clear();
    }

    
    private static void validateStackNotEmpty(String operationName) {
        if (frameStack.isEmpty()) {
            System.out.println("ERROR: Attempted to " + operationName + " from an empty call stack");
            System.exit(1);
        }
    }
}
