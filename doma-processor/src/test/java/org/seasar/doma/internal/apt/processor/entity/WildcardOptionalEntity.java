package org.seasar.doma.internal.apt.processor.entity;

import java.util.Optional;
import org.seasar.doma.Entity;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Entity
public class WildcardOptionalEntity {

  public Optional<?> value;
}
