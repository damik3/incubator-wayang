package org.qcri.rheem.core.plan;

import java.util.LinkedList;
import java.util.List;

/**
 * An output slot declares an output of an {@link Operator}.
 *
 * @param <T> see {@link Slot}
 */
public class OutputSlot<T> extends Slot<T> {

    private final List<InputSlot<T>> occupiedSlots = new LinkedList<>();

    public OutputSlot(OutputSlot blueprint, Operator owner) {
        this(blueprint.getName(), owner, blueprint.getType());
    }

    public OutputSlot(String name, Operator owner, Class<T> type) {
        super(name, owner, type);
    }

    public OutputSlot copyFor(Operator owner) {
        return new OutputSlot(this, owner);
    }

    /**
     * Connect this output slot to an input slot. The input slot must not be occupied already.
     *
     * @param inputSlot the input slot to connect to
     */
    public void connectTo(InputSlot<T> inputSlot) {
        if (inputSlot.getOccupant() != null) {
            throw new IllegalStateException("Cannot connect: input slot is already occupied");
        }

        occupiedSlots.add(inputSlot);
        inputSlot.setOccupant(this);
    }

    public void disconnectFrom(InputSlot<T> inputSlot) {
        if (inputSlot.getOccupant() != this) {
            throw new IllegalStateException("Cannot disconnect: input slot is not occupied by this output slot");
        }

        occupiedSlots.remove(inputSlot);
        inputSlot.setOccupant(null);
    }

    public List<InputSlot<T>> getOccupiedSlots() {
        return occupiedSlots;
    }
}
