package org.seasar.doma.internal.apt.entity;

import java.util.Optional;
import org.seasar.doma.Entity;

@Entity
public class WildcardOptionalEntity {

  public Optional<?> value;
}
