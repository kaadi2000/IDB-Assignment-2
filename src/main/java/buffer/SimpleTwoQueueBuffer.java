package buffer;

import java.util.ArrayDeque;

public class SimpleTwoQueueBuffer extends PageFaultRateBuffer {

    private final ArrayDeque<Slot> a1 = new ArrayDeque<>();
    private final ArrayDeque<Slot> am = new ArrayDeque<>();
    private int kin;

    public SimpleTwoQueueBuffer(int capacity) {
        super(capacity);
        // TODO
        this.kin= capacity/4;
    }

    @Override
    protected Buffer.Slot fix(char c) throws IllegalStateException {
        // TODO
        Slot slot = lookUp(c);

        if (slot != null) {
            if (a1.remove(slot)) {
                am.addFirst(slot);
            } else {
                am.remove(slot);
                am.addFirst(slot);
            }
        } else {
            if (a1.size() < kin) {
                slot = super.fix(c);
                a1.add(slot);
            } else {
                Slot victim = victim();
                if (victim != null) {
                    a1.remove(victim);
                }
                slot = super.fix(c);
                a1.add(slot);
            }
        }
        return slot;
    }

    protected Slot victim() {
        // TODO
        if (!a1.isEmpty()) {
            return a1.peek();
        } else if (!am.isEmpty()) {
            return am.peekLast();
        }
        return null;
    }
}
