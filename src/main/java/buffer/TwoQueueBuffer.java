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
        this.kin = capacity/4;
        this.kout = capacity/4;
    }

    @Override
    protected Slot fix(char c) throws IllegalStateException {
        // TODO
        Slot slot = lookUp(c);

        if (slot != null) {
            if (a1in.remove(slot)) {
                am.addFirst(slot);
            } else if (a1out.remove(c)) {
                slot = super.fix(c);
                am.addFirst(slot);
            } else {
                am.remove(slot);
                am.addFirst(slot);
            }
        } else {
            if (a1in.size() < kin) {
                slot = super.fix(c);
                a1in.add(slot);
            } else {
                Slot victim = victim();
                if (victim != null) {
                    victim.remove();
                    a1out.add(victim.c);
                    if (a1out.size() > kout) {
                        a1out.poll();
                    }
                    slot = super.fix(c);
                    a1in.add(slot);
                }
            }
        }
        return slot;
    }

    protected Slot victim() {
        // TODO
        if (!a1out.isEmpty()) {
            char c = a1out.poll();
            Slot slot = lookUp(c);
            if (slot != null) {
                return slot;
            }
        }
        if (!am.isEmpty()) {
            return am.pollLast();
        }
        return null;
    }
}
