package misc;

/**
 * Creator: Patrick
 * Created: 09.12.2017
 * Purpose:
 */

// TODO: Move to Collection Framework
@FunctionalInterface
public interface ArrayConstructor<T> {

    @Deprecated
    default T[] make(Integer para1) {
        return make(para1.intValue());
    }

    T[] make(int para1);
}