package org.seasar.doma.internal.apt.processor.entity;

import java.util.List;
import java.util.Map;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.entity.EmbeddableDesc;
import org.seasar.doma.jdbc.entity.EntityPropertyDesc;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;
import org.seasar.doma.wrapper.StringWrapper;

public class _UserAddress implements EmbeddableDesc<UserAddress> {

  private static _UserAddress singleton = new _UserAddress();

  @Override
  public <ENTITY> List<EntityPropertyDesc<ENTITY, ?>> getEmbeddablePropertyDescs(
      String embeddedPropertyName, Class<ENTITY> entityClass, NamingType namingType) {
    return java.util.Arrays.asList(
        new org.seasar.doma.jdbc.entity.DefaultPropertyDesc<>(
            entityClass,
            () -> new BasicScalar<>(new StringWrapper(), false),
            embeddedPropertyName + ".city",
            "",
            namingType,
            true,
            true,
            false),
        new org.seasar.doma.jdbc.entity.DefaultPropertyDesc<>(
            entityClass,
            () -> new BasicScalar<>(new StringWrapper(), false),
            embeddedPropertyName + ".street",
            "",
            namingType,
            true,
            true,
            false));
  }

  @Override
  public <ENTITY> UserAddress newEmbeddable(
      String embeddedPropertyName, Map<String, Property<ENTITY, ?>> __args) {
    return new UserAddress(
        (java.lang.String)
            (__args.get(embeddedPropertyName + ".city") != null
                ? __args.get(embeddedPropertyName + ".city").get()
                : null),
        (java.lang.String)
            (__args.get(embeddedPropertyName + ".street") != null
                ? __args.get(embeddedPropertyName + ".street").get()
                : null));
  }

  public static _UserAddress getSingletonInternal() {
    return singleton;
  }
}
