package buffer;

import java.util.ArrayDeque;

public class TwoQueueBuffer extends PageFaultRateBuffer {

    private final ArrayDeque<Slot> a1in = new ArrayDeque<>();
    private final ArrayDeque<Character> a1out = new ArrayDeque<>();
    private final ArrayDeque<Slot> am = new ArrayDeque<>();
    private final int kin;
    private final int kout;

    public TwoQueueBuffer(int capacity) {
        super(capacity);
        // TODO
        int kin1;
        kin1 = capacity / 4;
        if (kin1 == 0) kin1 = 1;
        this.kin = kin1;
        this.kout = capacity;
    }

    @Override
    protected Slot fix(char c) throws IllegalStateException {
        // TODO
        Slot slot = lookUp(c);
        if (slot != null) {
            if (am.contains(slot)) {
                am.remove(slot);
                am.addFirst(slot);
            } else if (a1in.contains(slot)) {
            }
            slot.fix();
            return slot;
        } else {
            if (fixedSlots == capacity()) {
                throw new IllegalStateException("Buffer overflow. Too many slots fixed.");
            }
            if (a1out.contains(c)) {
                a1out.remove(c);
                if (size() == capacity()) {
                    Slot victim = victim();
                    victim.remove();
                }
                slot = slots.get(size());
                slot.insert(c);
                slot.fix();
                am.addFirst(slot);
                return slot;
            } else {
                if (size() == capacity()) {
                    Slot victim = victim();
                    victim.remove();
                }
                slot = slots.get(size());
                slot.insert(c);
                slot.fix();
                a1in.addLast(slot);
                if (a1in.size() > kin) {
                    Slot oldest = a1in.removeFirst();
                    oldest.remove();
                    a1out.addLast(oldest.c);
                    if (a1out.size() > kout) {
                        a1out.removeFirst();
                    }
                }
                return slot;
            }
        }
    }

    protected Slot victim() {
        // TODO
        if (!a1in.isEmpty()) {
            Slot oldest = a1in.removeFirst();
            a1out.addLast(oldest.c);
            if (a1out.size() > kout) {
                a1out.removeFirst();
            }
            return oldest;
        } else if (!am.isEmpty()) {
            return am.removeLast();
        } else {
            throw new IllegalStateException("No victim to evict");
        }
    }
}
