package org.seasar.doma.internal.apt.entity;

import java.util.Optional;
import org.seasar.doma.Entity;

/** @author nakamura-to */
@Entity
public class RawtypeOptionalEntity {

  @SuppressWarnings("rawtypes")
  public Optional value;
}
