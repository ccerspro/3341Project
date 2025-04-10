public class HeapObject {
    public int refCount;
    public int[] value;

    public HeapObject(int initialValue) {
        this.refCount = 0;
        this.value = new int[] { initialValue };
    }
}
