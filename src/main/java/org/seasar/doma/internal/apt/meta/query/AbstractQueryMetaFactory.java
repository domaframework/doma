package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor8;
import org.seasar.doma.Domain;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.meta.dao.DaoMeta;
import org.seasar.doma.internal.apt.meta.entity.EntityPropertyNameCollector;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.message.Message;

public abstract class AbstractQueryMetaFactory<M extends AbstractQueryMeta>
    implements QueryMetaFactory {

  protected final Context ctx;

  protected AbstractQueryMetaFactory(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  protected void doTypeParameters(M queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    for (TypeParameterElement element : method.getTypeParameters()) {
      String name = ctx.getTypes().getTypeParameterName(element.asType());
      queryMeta.addTypeParameterName(name);
    }
  }

  protected abstract void doReturnType(M queryMeta, ExecutableElement method, DaoMeta daoMeta);

  protected abstract void doParameters(M queryMeta, ExecutableElement method, DaoMeta daoMeta);

  protected void doThrowTypes(M queryMeta, ExecutableElement method, DaoMeta daoMeta) {
    for (TypeMirror thrownType : method.getThrownTypes()) {
      queryMeta.addThrownTypeName(ctx.getTypes().getTypeName(thrownType));
    }
  }

  protected boolean isPrimitiveInt(TypeMirror typeMirror) {
    return typeMirror.getKind() == TypeKind.INT;
  }

  protected boolean isPrimitiveIntArray(TypeMirror typeMirror) {
    return typeMirror.accept(
        new TypeKindVisitor8<Boolean, Void>(false) {

          @Override
          public Boolean visitArray(ArrayType t, Void p) {
            return t.getComponentType().getKind() == TypeKind.INT;
          }
        },
        null);
  }

  protected boolean isPrimitiveVoid(TypeMirror typeMirror) {
    return typeMirror.getKind() == TypeKind.VOID;
  }

  protected boolean isEntity(TypeMirror typeMirror) {
    TypeElement typeElement = ctx.getTypes().toTypeElement(typeMirror);
    return typeElement != null && typeElement.getAnnotation(Entity.class) != null;
  }

  protected boolean isDomain(TypeMirror typeMirror) {
    TypeElement typeElement = ctx.getTypes().toTypeElement(typeMirror);
    return typeElement != null && typeElement.getAnnotation(Domain.class) != null;
  }

  protected boolean isConfig(TypeMirror typeMirror) {
    return ctx.getTypes().isSameType(typeMirror, Config.class);
  }

  protected boolean isCollection(TypeMirror typeMirror) {
    return ctx.getTypes().isAssignable(typeMirror, Collection.class);
  }

  protected boolean isSelectOptions(TypeMirror typeMirror) {
    return ctx.getTypes().isAssignable(typeMirror, SelectOptions.class);
  }

  protected boolean isIterationCallback(TypeMirror typeMirror) {
    return ctx.getTypes().isAssignable(typeMirror, IterationCallback.class);
  }

  protected void validateEntityPropertyNames(
      TypeMirror entityType,
      ExecutableElement method,
      AnnotationMirror annotationMirror,
      AnnotationValue includeValue,
      AnnotationValue excludeValue) {
    List<String> includedPropertyNames = AnnotationValueUtil.toStringList(includeValue);
    List<String> excludedPropertyNames = AnnotationValueUtil.toStringList(excludeValue);
    if (includedPropertyNames != null && !includedPropertyNames.isEmpty()
        || excludedPropertyNames != null && !excludedPropertyNames.isEmpty()) {
      EntityPropertyNameCollector collector = new EntityPropertyNameCollector(ctx);
      Set<String> names = collector.collect(entityType);
      for (String included : includedPropertyNames) {
        if (!names.contains(included)) {
          throw new AptException(
              Message.DOMA4084,
              ctx.getEnv(),
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
              ctx.getEnv(),
              method,
              annotationMirror,
              excludeValue,
              new Object[] {excluded, entityType});
        }
      }
    }
  }

  protected QueryReturnMeta createReturnMeta(QueryMeta queryMeta) {
    return new QueryReturnMeta(queryMeta, ctx);
  }

  protected QueryParameterMeta createParameterMeta(VariableElement parameter, QueryMeta queryMeta) {
    QueryParameterMeta queryParameterMeta = new QueryParameterMeta(parameter, queryMeta, ctx);
    return queryParameterMeta;
  }
}
