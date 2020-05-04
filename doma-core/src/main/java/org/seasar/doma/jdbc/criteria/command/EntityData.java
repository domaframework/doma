package org.seasar.doma.jdbc.criteria.command;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.jdbc.entity.Property;

public class EntityData {
  private final Map<String, Property<Object, ?>> states;

  public EntityData(Map<String, Property<Object, ?>> states) {
    Objects.requireNonNull(states);
    this.states = Collections.unmodifiableMap(states);
  }

  public Map<String, Property<Object, ?>> getStates() {
    return states;
  }
}
