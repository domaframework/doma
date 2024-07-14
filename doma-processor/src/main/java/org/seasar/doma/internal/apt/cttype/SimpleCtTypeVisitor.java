package org.seasar.doma.internal.apt.cttype;

public class SimpleCtTypeVisitor<R, P, TH extends Throwable> implements CtTypeVisitor<R, P, TH> {

  protected R defaultValue;

  public SimpleCtTypeVisitor() {}

  public SimpleCtTypeVisitor(R defaultValue) {
    this.defaultValue = defaultValue;
  }

  protected R defaultAction(CtType ctType, P p) throws TH {
    return defaultValue;
  }

  @Override
  public R visitAnyCtType(AnyCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitBatchResultCtType(BatchResultCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitBasicCtType(BasicCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitDomainCtType(DomainCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitEmbeddableCtType(EmbeddableCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitEntityCtType(EntityCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitIterableCtType(IterableCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitArrayCtType(ArrayCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitCollectorCtType(CollectorCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitReferenceCtType(ReferenceCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitResultCtType(ResultCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitSelectOptionsCtType(SelectOptionsCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitMapCtType(MapCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitNoneCtType(NoneCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitOptionalCtType(OptionalCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitOptionalIntCtType(OptionalIntCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitOptionalLongCtType(OptionalLongCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitOptionalDoubleCtType(OptionalDoubleCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitFunctionCtType(FunctionCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitStreamCtType(StreamCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitBiFunctionCtType(BiFunctionCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitConfigCtType(ConfigCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitPreparedSqlCtType(PreparedSqlCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }

  @Override
  public R visitMultiResultCtType(MultiResultCtType ctType, P p) throws TH {
    return defaultAction(ctType, p);
  }
}
