import java.util.HashMap;
import java.util.Map;

/**
 * Manages heap-allocated objects and their reference counts.
 */
public class HeapManager {
    private static final Map<String, HeapObject> heap = new HashMap<>();

    public static void addReference(String varName, HeapObject obj) {
        if (obj != null) {
            obj.refCount++;
        }
        heap.put(varName, obj);
        printLiveCount();
    }

    public static void removeReference(String varName) {
        HeapObject obj = heap.get(varName);
        if (obj != null) {
            obj.refCount--;
            if (obj.refCount <= 0) {
                heap.remove(varName); // cleanup unreachable object
            }
        }
        printLiveCount();
    }

    public static HeapObject get(String varName) {
        return heap.get(varName);
    }

    public static void clearReferences(String... varNames) {
        for (String varName : varNames) {
            removeReference(varName);
        }
    }

    private static void printLiveCount() {
        long count = heap.values().stream().filter(o -> o.refCount > 0).count();
        System.out.println("gc:" + count);
    }
}
