package buffer;

public abstract class PageFaultRateBuffer extends Buffer {
    private int fsCount = 0;
    private int sCount = 0;

    public PageFaultRateBuffer(int capacity) {
        super(capacity);
    }

    public double getFSR() {
        // TODO
        if (sCount==0) {
            return 0.0;
        }
        return (double)fsCount/(double)sCount;
    }

    @Override
    protected Slot fix(char c) throws IllegalStateException {
        // TODO
        sCount++;
        Slot slot = lookUp(c);
        if (slot==null) {
            fsCount++;
            slot= super.fix(c);
        }
        else{
            slot.fix();
        }
        return slot;
    }
}
