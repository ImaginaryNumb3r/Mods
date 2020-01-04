package misc;

import java.util.function.BiFunction;

/**
 * Creator: Patrick
 * Created: 08.01.2018
 * Purpose:
 */
// TODO: Move to Collections Framework
@FunctionalInterface
public interface MatrixCellSupplier<T> extends BiFunction<Integer, Integer, T> {

    T make(int x, int y);

    @Override
    default T apply(Integer int1, Integer int2){
        return make(int1, int2);
    }
}
