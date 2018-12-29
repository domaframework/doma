package org.seasar.doma.internal.apt.entity;

import java.util.Optional;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;

@Entity
public class OptionalEntity {

  @Id Optional<Integer> id;

  Optional<Name> name;

  Optional<String> city;

  Optional<Weight<Integer>> weight;

  Optional<Kind> kind;

  @Version Optional<Long> version;
}
