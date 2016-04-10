package org.qcri.rheem.spark.mapping;

import org.qcri.rheem.basic.operators.BernoulliSampleOperator;
import org.qcri.rheem.core.function.PredicateDescriptor;
import org.qcri.rheem.core.mapping.*;
import org.qcri.rheem.spark.operators.SparkBernoulliSampleOperator;
import org.qcri.rheem.spark.platform.SparkPlatform;

import java.util.Collection;
import java.util.Collections;

/**
 * Mapping from {@link BernoulliSampleOperator} to {@link SparkBernoulliSampleOperator}.
 */
@SuppressWarnings("unchecked")
public class BernoulliSampleToSparkBernoulliSampleMapping implements Mapping {

    @Override
    public Collection<PlanTransformation> getTransformations() {
        return Collections.singleton(
                new PlanTransformation(
                        this.createSubplanPattern(),
                        this.createReplacementSubplanFactory(),
                        SparkPlatform.getInstance()
                )
        );
    }

    private SubplanPattern createSubplanPattern() {
        final OperatorPattern operatorPattern = new OperatorPattern(
                "bernoulliSample", new BernoulliSampleOperator<>((double) 0, (PredicateDescriptor) null, null), false); //TODO: check if the zero here affects execution
        return SubplanPattern.createSingleton(operatorPattern);
    }

    private ReplacementSubplanFactory createReplacementSubplanFactory() {
        return new ReplacementSubplanFactory.OfSingleOperators<BernoulliSampleOperator>(
                (matchedOperator, epoch) -> new SparkBernoulliSampleOperator<>(
                        matchedOperator.getFraction(),
                        matchedOperator.getType(),
                        matchedOperator.getPredicateDescriptor()
                ).at(epoch)
        );
    }
}
