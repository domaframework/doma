package org.seasar.doma.internal.apt.entity;

import java.util.OptionalInt;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;

@Entity
public class OptionalIntEntity {

  @Id OptionalInt id;

  OptionalInt age;

  @Version OptionalInt version;
}
