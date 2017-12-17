package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.lombok.AccessLevel;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;

/**
 * @author nakamura-to
 *
 */
@Entity(immutable = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LombokAllArgsConstructorAccess_private {

}
