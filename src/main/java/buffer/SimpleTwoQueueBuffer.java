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
        if (this.kin == 0) {
            this.kin = 1;
        }
    }

    @Override
    protected Buffer.Slot fix(char c) throws IllegalStateException {
        // TODO
        Slot slot = lookUp(c);
        if (slot != null) {
            if (a1.contains(slot)) {
                a1.remove(slot);
                am.addFirst(slot);
            } else if (am.contains(slot)) {
                am.remove(slot);
                am.addFirst(slot);
            }
            slot.fix();
            return slot;
        } else {
            if (fixedSlots == capacity()) {
                throw new IllegalStateException("Buffer overflow. Too many slots fixed.");
            }
            if (size() == capacity()) {
                Slot victim = victim();
                victim.remove();
            }
            slot = slots.get(size());
            slot.insert(c);
            slot.fix();
            a1.addLast(slot);
            if (a1.size() > kin) {
                Slot oldest = a1.removeFirst();
                oldest.remove();
            }
            return slot;
        }
    }

    protected Slot victim() {
        // TODO
        if (!a1.isEmpty()) {
            Slot victim = a1.removeFirst();
            return victim;
        } else if (!am.isEmpty()) {
            Slot victim = am.removeLast();
            return victim;
        } else {
            throw new IllegalStateException("No victim to evict");
        }
    }
}
