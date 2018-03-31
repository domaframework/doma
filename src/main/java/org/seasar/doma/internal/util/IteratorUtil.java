package org.seasar.doma.internal.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class IteratorUtil {

  public static <T> List<T> toList(Iterator<T> iterator) {
    List<T> list = new LinkedList<>();
    iterator.forEachRemaining(list::add);
    return list;
  }

  public static <T> Iterator<T> copy(Iterator<T> iterator) {
    var list = toList(iterator);
    return list.iterator();
  }
}
