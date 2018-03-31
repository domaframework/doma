package org.seasar.doma.jdbc.entity;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Entity;
import org.seasar.doma.internal.Conventions;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.internal.util.MethodUtil;
import org.seasar.doma.jdbc.ClassHelper;
import org.seasar.doma.message.Message;

/** A factory for entity descriptions. */
public final class EntityDescFactory {

  /**
   * Returns the entity description.
   *
   * @param <E> the entity type
   * @param entityClass the entity class
   * @param classHelper the class helper
   * @return the entity description
   * @throws DomaNullPointerException if any arguments are {@code null}
   * @throws DomaIllegalArgumentException if the entity class is not annotated with the {@link
   *     Entity} annotation
   * @throws EntityDescNotFoundException if the entity description is not found
   */
  public static <E> EntityDesc<E> getEntityDesc(Class<E> entityClass, ClassHelper classHelper) {
    if (entityClass == null) {
      throw new DomaNullPointerException("entityClass");
    }
    if (classHelper == null) {
      throw new DomaNullPointerException("classHelper");
    }
    if (!entityClass.isAnnotationPresent(Entity.class)) {
      throw new DomaIllegalArgumentException(
          "entityClass", Message.DOMA2206.getMessage("entityClass"));
    }
    var entityDescClassName = Conventions.createDescClassName(entityClass.getName());
    try {
      Class<E> clazz = classHelper.forName(entityDescClassName);
      var method = ClassUtil.getMethod(clazz, "getSingletonInternal");
      return MethodUtil.invoke(method, null);
    } catch (Exception e) {
      throw new EntityDescNotFoundException(
          e.getCause(), entityClass.getName(), entityDescClassName);
    }
  }
}
