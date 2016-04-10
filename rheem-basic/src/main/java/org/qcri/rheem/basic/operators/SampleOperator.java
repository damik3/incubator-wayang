package org.qcri.rheem.basic.operators;

import org.apache.commons.lang3.Validate;
import org.qcri.rheem.core.api.Configuration;
import org.qcri.rheem.core.function.PredicateDescriptor;
import org.qcri.rheem.core.optimizer.cardinality.CardinalityEstimator;
import org.qcri.rheem.core.optimizer.cardinality.DefaultCardinalityEstimator;
import org.qcri.rheem.core.plan.rheemplan.UnaryToUnaryOperator;
import org.qcri.rheem.core.types.BasicDataUnitType;
import org.qcri.rheem.core.types.DataSetType;

import java.util.Optional;
import java.util.Random;

/**
 * A random sample operator randomly selects its inputs from the input slot and pushes that element to the output slot.
 */
public class SampleOperator<Type> extends UnaryToUnaryOperator<Type, Type> {

    protected final long sampleSize;
    protected Random rand;

    /**
     * Function that this operator applies to the input elements.
     */
    protected final PredicateDescriptor<Type> predicateDescriptor;

    /**
     * Creates a new instance.
     */
    public SampleOperator(long sampleSize, PredicateDescriptor.SerializablePredicate<Type> predicateDescriptor, Class<Type> typeClass) {
        this(sampleSize, new PredicateDescriptor<>(predicateDescriptor, BasicDataUnitType.createBasic(typeClass)));
    }

    /**
     * Creates a new instance.
     */
    public SampleOperator(long sampleSize, PredicateDescriptor<Type> predicateDescriptor) {
        super(DataSetType.createDefault(predicateDescriptor.getInputType()),
                DataSetType.createDefault(predicateDescriptor.getInputType()),
                true,
                null);
        this.predicateDescriptor = predicateDescriptor;
        this.sampleSize = sampleSize;
        rand = new Random();
    }

    /**
     * Creates a new instance.
     *
     * @param type type of the dataunit elements
     */
    public SampleOperator(long sampleSize, DataSetType<Type> type, PredicateDescriptor.SerializablePredicate<Type> predicateDescriptor) {
        this(sampleSize, new PredicateDescriptor<>(predicateDescriptor, (BasicDataUnitType) type.getDataUnitType()), type);
    }

    /**
     * Creates a new instance.
     *
     * @param type type of the dataunit elements
     */
    public SampleOperator(long sampleSize, PredicateDescriptor<Type> predicateDescriptor, DataSetType<Type> type) {
        super(type, type, true, null);
        this.predicateDescriptor = predicateDescriptor;
        this.sampleSize = sampleSize;
        rand = new Random();
    }

    public PredicateDescriptor<Type> getPredicateDescriptor() {
        return this.predicateDescriptor;
    }

    public DataSetType getType() { return this.getInputType(); }

    public long getSampleSize() { return this.sampleSize; }

    @Override
    public Optional<CardinalityEstimator> getCardinalityEstimator(
            final int outputIndex,
            final Configuration configuration) {
        Validate.inclusiveBetween(0, this.getNumOutputs() - 1, outputIndex);
        return Optional.of(new DefaultCardinalityEstimator(1d, 1, this.isSupportingBroadcastInputs(),
                inputCards -> inputCards[0]));
    }
}
