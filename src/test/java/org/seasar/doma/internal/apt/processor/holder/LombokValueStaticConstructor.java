package org.seasar.doma.internal.apt.processor.holder;

import org.seasar.doma.Holder;
import org.seasar.doma.internal.apt.lombok.Value;

/**
 * @author nakamura-to
 *
 */
@Holder(valueType = String.class)
@Value(staticConstructor = "of")
public class LombokValueStaticConstructor {

}
