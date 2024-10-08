module org.seasar.doma.processor {
  requires java.compiler;
  requires org.seasar.doma.core;

  provides javax.annotation.processing.Processor with
      org.seasar.doma.internal.apt.processor.DomainProcessor,
      org.seasar.doma.internal.apt.processor.DataTypeProcessor,
      org.seasar.doma.internal.apt.processor.ExternalDomainProcessor,
      org.seasar.doma.internal.apt.processor.DomainConvertersProcessor,
      org.seasar.doma.internal.apt.processor.EmbeddableProcessor,
      org.seasar.doma.internal.apt.processor.EntityProcessor,
      org.seasar.doma.internal.apt.processor.DaoProcessor,
      org.seasar.doma.internal.apt.processor.ScopeProcessor;
}
