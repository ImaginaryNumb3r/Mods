package lib;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Creator: Patrick
 * Created: 29.05.2019
 * Purpose:
 */
public class SingleCollector<T> implements Collector<T, SingleCollector.Box<T>, T> {

    @Override
    public Supplier<Box<T>> supplier() {
        return Box::new;
    }

    @Override
    public BiConsumer<Box<T>, T> accumulator() {
        return (box, instance) -> {
            if (instance != null) {
                box.value = instance;
            }
        };
    }

    @Override
    public BinaryOperator<Box<T>> combiner() {
        return (left, right) -> left;
    }

    @Override
    public Function<Box<T>, T> finisher() {
        return box -> box.value;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

    static class Box<T> {
        private T value;
    }
}
