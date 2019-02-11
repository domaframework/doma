package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.def.TypeParametersDef;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyNameCollector;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.message.Message;

abstract class AbstractQueryMetaFactory<M extends AbstractQueryMeta> implements QueryMetaFactory {

  final Context ctx;

  AbstractQueryMetaFactory(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  void doTypeParameters(M queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    TypeParametersDef typeParametersDef = ctx.getElements().getTypeParametersDef(method);
    queryMeta.setTypeParametersDef(typeParametersDef);
  }

  protected abstract void doReturnType(M queryMeta, ExecutableElement method, DaoMeta daoMeta);

  protected abstract void doParameters(M queryMeta, ExecutableElement method, DaoMeta daoMeta);

  void doThrowTypes(M queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    method.getThrownTypes().forEach(queryMeta::addThrownType);
  }

  void validateEntityPropertyNames(
      TypeMirror entityType,
      ExecutableElement method,
      AnnotationMirror annotationMirror,
      AnnotationValue includeValue,
      AnnotationValue excludeValue) {
    List<String> includedPropertyNames = AnnotationValueUtil.toStringList(includeValue);
    if (includedPropertyNames == null) {
      includedPropertyNames = Collections.emptyList();
    }
    List<String> excludedPropertyNames = AnnotationValueUtil.toStringList(excludeValue);
    if (excludedPropertyNames == null) {
      excludedPropertyNames = Collections.emptyList();
    }
    if (!includedPropertyNames.isEmpty() || !excludedPropertyNames.isEmpty()) {
      EntityPropertyNameCollector collector = new EntityPropertyNameCollector(ctx);
      Set<String> names = collector.collect(entityType);
      for (String included : includedPropertyNames) {
        if (!names.contains(included)) {
          throw new AptException(
              Message.DOMA4084,
              method,
              annotationMirror,
              includeValue,
              new Object[] {included, entityType});
        }
      }
      for (String excluded : excludedPropertyNames) {
        if (!names.contains(excluded)) {
          throw new AptException(
              Message.DOMA4085,
              method,
              annotationMirror,
              excludeValue,
              new Object[] {excluded, entityType});
        }
      }
    }
  }

  QueryReturnMeta createReturnMeta(QueryMeta queryMeta) {
    QueryReturnMetaFactory factory = new QueryReturnMetaFactory(ctx, queryMeta);
    return factory.createQueryReturnMeta();
  }

  QueryParameterMeta createParameterMeta(VariableElement parameter, QueryMeta queryMeta) {
    QueryParameterMetaFactory factory = new QueryParameterMetaFactory(ctx, parameter, queryMeta);
    return factory.createQueryParameterMeta();
  }
}
