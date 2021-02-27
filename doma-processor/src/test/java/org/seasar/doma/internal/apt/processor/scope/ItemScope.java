package org.seasar.doma.internal.apt.processor.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.seasar.doma.Scope;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

public class ItemScope {
  @Scope
  public Consumer<WhereDeclaration> startWithHoge(Item_ e) {
    return w -> w.like(e.name, "hoge%");
  }

  @Scope
  public Consumer<WhereDeclaration> ids(Item_ e, long[] ids) {
    return w -> w.in(e.id, Arrays.stream(ids).boxed().collect(Collectors.toList()));
  }

  @Scope
  public Consumer<WhereDeclaration> names(Item_ e, String... names) {
    return w -> w.in(e.name, Arrays.asList(names));
  }

  @Scope
  public Consumer<WhereDeclaration> idAndName(Item_ e, long id, String name) {
    return w -> {
      w.eq(e.id, id);
      w.eq(e.name, name);
    };
  }

  @Scope
  public Consumer<WhereDeclaration> stringList(Item_ e, List<String> names) {
    return w -> w.in(e.name, names);
  }

  @Scope
  public Consumer<WhereDeclaration> numberList(Item_ e, List<? extends Number> numbers) {
    return w -> w.in(e.id, numbers.stream().map(Number::longValue).collect(Collectors.toList()));
  }

  @Scope
  public <T> T genericMethod(Item_ e, T t) {
    return t;
  }

  @Scope
  public <T extends Number> T boundedGenericMethod(Item_ e, T t) {
    return t;
  }

  @Scope
  public <A extends Number, B> List<A> multipleGenericTypeMethod(Item_ e, A a, B b) {
    return new ArrayList<>(Collections.singletonList(a));
  }

  @Scope
  public <A extends Number, B extends List<A> & Serializable> List<A> boundedGenericTypeMethod(
      Item_ e, A a, B b) {
    return new ArrayList<>(Collections.singletonList(a));
  }
}
