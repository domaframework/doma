package org.seasar.doma.internal.apt.processor.embeddabledesc;

import java.util.List;
import java.util.Map;
import org.seasar.doma.internal.EmbeddableDesc;
import org.seasar.doma.jdbc.entity.EmbeddableType;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.jdbc.entity.Property;

@EmbeddableDesc(Address.class)
public class _Address implements EmbeddableType<Address> {
  @Override
  public <ENTITY> List<EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypes(
      String embeddedPropertyName, Class<ENTITY> entityClass, NamingType namingType) {
    return null;
  }

  @Override
  public <ENTITY> Address newEmbeddable(
      String embeddedPropertyName, Map<String, Property<ENTITY, ?>> __args) {
    return null;
  }
}
