package misc;

import java.util.function.BiFunction;

/**
 * Creator: Patrick
 * Created: 09.12.2017
 * Purpose:
 */
@FunctionalInterface
// TODO: Move to Collection Framework
public interface MatrixConstructor<T> extends BiFunction<Integer, Integer, T[][]> {

    T[][] apply(int width, int height);

    @Deprecated
    default T[][] apply(Integer width, Integer height){
        return apply(width.intValue(), height.intValue());
    }
}
