package misc;

import java.util.function.BiConsumer;

/**
 * Creator: Patrick
 * Created: 08.01.2018
 * Purpose:
 */
// TODO: Move to Collections Framework
@FunctionalInterface
public interface MatrixCellConsumer extends BiConsumer<Integer, Integer> {

    void accept(int x, int y);

    @Override
    @Deprecated
    default void accept(Integer x, Integer y) {
        accept((int) x, (int) y);
    }
}
