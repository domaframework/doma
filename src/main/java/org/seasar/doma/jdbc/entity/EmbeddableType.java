package org.seasar.doma.jdbc.entity;

import java.util.List;
import java.util.Map;

/** @author nakamura-to */
public interface EmbeddableType<EMBEDDABLE> {

  <ENTITY> List<EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypes(
      String embeddedPropertyName, Class<ENTITY> entityClass, NamingType namingType);

  <ENTITY> EMBEDDABLE newEmbeddable(
      String embeddedPropertyName, Map<String, Property<ENTITY, ?>> __args);
}
