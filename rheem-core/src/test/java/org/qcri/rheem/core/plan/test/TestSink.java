package org.qcri.rheem.core.plan.test;

import org.qcri.rheem.core.plan.InputSlot;
import org.qcri.rheem.core.plan.Sink;

/**
 * Dummy sink for testing purposes.
 */
public class TestSink<T> implements Sink {

    private final InputSlot[] inputSlots;

    public TestSink(Class<T> inputType) {
        this.inputSlots =  new InputSlot[]{new InputSlot<>("input", this, inputType)};
    }

    @Override
    public InputSlot[] getAllInputs() {
        return this.inputSlots;
    }

    public Class<T> getType() {
        return this.inputSlots[0].getType();
    }
}
