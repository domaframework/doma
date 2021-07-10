package org.seasar.doma.internal.apt.processor.entity;

import java.util.Arrays;
import java.util.Iterator;
import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class Names implements Iterable<String> {
  private final String value;

  public Names(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public Iterator<String> iterator() {
    return Arrays.asList(value.split(",")).iterator();
  }
}
