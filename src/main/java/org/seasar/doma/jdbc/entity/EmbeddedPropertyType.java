package org.seasar.doma.jdbc.entity;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.jdbc.entity.PropertyField;

/**
 * @author nakamura-to
 * @since 2.10.0
 * @param <ENTITY> エンティティクラスの型
 * @param <EMBEDDABLE> エンベッダブルクラスの型
 */
public class EmbeddedPropertyType<ENTITY, EMBEDDABLE> {

  protected final String name;

  protected final List<EntityPropertyType<ENTITY, ?>> embeddablePropertyTypes;

  protected final Map<String, EntityPropertyType<ENTITY, ?>> embeddablePropertyTypeMap;

  protected final PropertyField<ENTITY> field;

  public EmbeddedPropertyType(
      String name,
      Class<ENTITY> entityClass,
      List<EntityPropertyType<ENTITY, ?>> embeddablePropertyType) {
    if (name == null) {
      throw new DomaNullPointerException("name");
    }
    if (entityClass == null) {
      throw new DomaNullPointerException("entityClass");
    }
    if (embeddablePropertyType == null) {
      throw new DomaNullPointerException("embeddablePropertyType");
    }
    this.name = name;
    this.embeddablePropertyTypes = embeddablePropertyType;
    this.embeddablePropertyTypeMap =
        this.embeddablePropertyTypes
            .stream()
            .collect(toMap(EntityPropertyType::getName, Function.identity()));
    this.field = new PropertyField<>(name, entityClass);
  }

  public List<EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypes() {
    return embeddablePropertyTypes;
  }

  public Map<String, EntityPropertyType<ENTITY, ?>> getEmbeddablePropertyTypeMap() {
    return embeddablePropertyTypeMap;
  }

  public void save(ENTITY entity, EMBEDDABLE value) {
    field.setValue(entity, value);
  }
}
