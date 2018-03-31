package org.seasar.doma.internal.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/** @author nakamura-to */
public final class IteratorUtil {

  public static <T> List<T> toList(Iterator<T> iterator) {
    List<T> list = new LinkedList<>();
    iterator.forEachRemaining(list::add);
    return list;
  }

  public static <T> Iterator<T> copy(Iterator<T> iterator) {
    List<T> list = toList(iterator);
    return list.iterator();
  }
}
