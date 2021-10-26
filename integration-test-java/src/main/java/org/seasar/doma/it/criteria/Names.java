package org.seasar.doma.it.criteria;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.seasar.doma.Domain;

@Domain(valueType = String.class)
public class Names implements Iterable<String> {
  private final List<String> value;

  public Names(String value) {
    this.value = Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
  }

  public String getValue() {
    return String.join(",", value);
  }

  @Override
  public Iterator<String> iterator() {
    return value.iterator();
  }
}
