package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.lombok.AllArgsConstructor;

/**
 * @author nakamura-to
 *
 */
@Entity(immutable = true)
@AllArgsConstructor(staticName = "of")
public class LombokAllArgsConstructorStaticName {

}
