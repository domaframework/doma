package org.seasar.doma.internal.apt.processor.metamodel;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.seasar.doma.jdbc.criteria.metamodel.DefaultPropertyMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EmbeddableType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;

public class _EmpInfo implements EmbeddableType<EmpInfo> {
  @Override
  public <ENTITY> List<EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypes(
      String embeddedPropertyName, Class<ENTITY> entityClass, NamingType namingType) {
    return null;
  }

  @Override
  public <ENTITY> EmpInfo newEmbeddable(
      String embeddedPropertyName, Map<String, Property<ENTITY, ?>> __args) {
    return null;
  }

  public static _EmpInfo getSingletonInternal() {
    return null;
  }

  public static final class Metamodel {

    public final PropertyMetamodel<LocalDate> hiredate;

    public Metamodel(EntityType<?> entityType, String name) {
      hiredate = new DefaultPropertyMetamodel<>(LocalDate.class, entityType, name + ".hiredate");
    }

    public List<PropertyMetamodel<?>> allPropertyMetamodels() {
      return Arrays.asList(hiredate);
    }
  }
}
