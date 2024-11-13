package buffer;

public class PageFaultRateLRUBuffer extends LRUBuffer {
    private int fsCount = 0;
    private int sCount = 0;

    public PageFaultRateLRUBuffer(int capacity) {
        super(capacity);
    }

    public double getFSR() {
        // TODO
        if (fsCount == 0) {
            return 0.0;
        }
        return (double) fsCount/ sCount;
    }

    @Override
    protected Buffer.Slot fix(char c) throws IllegalStateException {
        // TODO
        sCount++;
        Buffer.Slot slot = lookUp(c);
        if (slot == null) {
            fsCount++;
        }
        return slot;
    }
}
