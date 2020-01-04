package misc;

import java.lang.reflect.Type;

/**
 * Creator: Patrick
 * Created: 13.05.2019
 * Purpose:
 */
public interface FieldTransformer {
    FieldTransformer identity = (targetType, currentValue, newValue) -> newValue;

    public Object transform(Class<?> targetType, Object currentValue, Object newValue);


}
