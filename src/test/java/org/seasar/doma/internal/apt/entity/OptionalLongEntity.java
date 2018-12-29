package org.seasar.doma.internal.apt.entity;

import java.util.OptionalLong;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;

@Entity
public class OptionalLongEntity {

  @Id OptionalLong id;

  OptionalLong age;

  @Version OptionalLong version;
}
