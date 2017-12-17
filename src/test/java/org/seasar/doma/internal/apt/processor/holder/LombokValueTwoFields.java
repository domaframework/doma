package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;
import org.seasar.doma.internal.apt.lombok.Value;

/**
 * @author nakamura-to
 *
 */
@Holder(valueType = String.class)
@Value
public class LombokValueTwoFields {

    @SuppressWarnings("unused")
    private String value1;

    @SuppressWarnings("unused")
    private String value2;
}
