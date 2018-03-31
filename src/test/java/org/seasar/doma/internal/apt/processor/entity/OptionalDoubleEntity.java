package org.seasar.doma.internal.apt.processor.entity;

import java.util.OptionalDouble;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;

/** @author nakamura-to */
@Entity
public class OptionalDoubleEntity {

  @Id OptionalDouble id;

  OptionalDouble age;

  @Version OptionalDouble version;
}
