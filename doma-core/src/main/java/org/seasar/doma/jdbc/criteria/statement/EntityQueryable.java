package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.declaration.JoinDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.OrderByNameDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.AssociationOption;
import org.seasar.doma.jdbc.criteria.option.DistinctOption;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;

/**
 * Represents a queryable statement for entities.
 *
 * <p>Note: {@link #project(EntityMetamodel)} and {@link #projectTo(EntityMetamodel,
 * PropertyMetamodel[])} will remove duplicate entities from the results.
 *
 * @param <ENTITY> the type of the entity
 */
public interface EntityQueryable<ENTITY> extends Listable<ENTITY> {

  /**
   * Specifies the distinct keyword.
   *
   * @return the select statement
   */
  EntityQueryable<ENTITY> distinct();

  /**
   * Specifies the distinct keyword with the specified option.
   *
   * @param distinctOption the distinct option
   * @return the select statement
   */
  EntityQueryable<ENTITY> distinct(DistinctOption distinctOption);

  /**
   * Specifies the inner join clause.
   *
   * @param entityMetamodel the entity metamodel
   * @param block the block that describes the join condition
   * @return the select statement
   */
  EntityQueryable<ENTITY> innerJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block);

  /**
   * Specifies the left join clause.
   *
   * @param entityMetamodel the entity metamodel
   * @param block the block that describes the join condition
   * @return the select statement
   */
  EntityQueryable<ENTITY> leftJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block);

  /**
   * Specifies the where clause.
   *
   * @param block the block that describes the where condition
   * @return the select statement
   */
  EntityQueryable<ENTITY> where(Consumer<WhereDeclaration> block);

  /**
   * Specifies the order by clause.
   *
   * @param block the block that describes the order by condition
   * @return the select statement
   */
  EntityQueryable<ENTITY> orderBy(Consumer<OrderByNameDeclaration> block);

  /**
   * Specifies the limit clause.
   *
   * @param limit the limit
   * @return the select statement
   */
  EntityQueryable<ENTITY> limit(Integer limit);

  /**
   * Specifies the offset clause.
   *
   * @param offset the offset
   * @return the select statement
   */
  EntityQueryable<ENTITY> offset(Integer offset);

  /**
   * Specifies the for update clause.
   *
   * @return the select statement
   */
  EntityQueryable<ENTITY> forUpdate();

  /**
   * Specifies the for update clause with the specified option.
   *
   * @param option the for update option
   * @return the select statement
   */
  EntityQueryable<ENTITY> forUpdate(ForUpdateOption option);

  /**
   * Associates the entities.
   *
   * @param first the first entity metamodel
   * @param second the second entity metamodel
   * @param associator the associator
   * @return the select statement
   */
  <ENTITY1, ENTITY2> EntityQueryable<ENTITY> associate(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiConsumer<ENTITY1, ENTITY2> associator);

  /**
   * Associates the entities with the option.
   *
   * @param first the first entity metamodel
   * @param second the second entity metamodel
   * @param associator the associator
   * @param option the association option
   * @return the select statement
   */
  <ENTITY1, ENTITY2> EntityQueryable<ENTITY> associate(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiConsumer<ENTITY1, ENTITY2> associator,
      AssociationOption option);

  /**
   * Associates the immutable entities.
   *
   * @param first the first entity metamodel
   * @param second the second entity metamodel
   * @param associator the associator
   * @return the select statement
   */
  <ENTITY1, ENTITY2> EntityQueryable<ENTITY> associateWith(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiFunction<ENTITY1, ENTITY2, ENTITY1> associator);

  /**
   * Associates the immutable entities with the option.
   *
   * @param first the first entity metamodel
   * @param second the second entity metamodel
   * @param associator the associator
   * @return the select statement
   */
  <ENTITY1, ENTITY2> EntityQueryable<ENTITY> associateWith(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiFunction<ENTITY1, ENTITY2, ENTITY1> associator,
      AssociationOption option);

  /**
   * Projects the result to the entity. The duplicated entities are removed from the result.
   *
   * @param entityMetamodel the entity metamodel
   * @return the select statement
   */
  <RESULT> Listable<RESULT> project(EntityMetamodel<RESULT> entityMetamodel);

  /**
   * Projects the result to the entity with the specified properties. The duplicated entities are
   * removed from the result.
   *
   * @param entityMetamodel the entity metamodel
   * @param propertyMetamodels the property metamodels to project
   * @return the select statement
   */
  <RESULT> Listable<RESULT> projectTo(
      EntityMetamodel<RESULT> entityMetamodel, PropertyMetamodel<?>... propertyMetamodels);
}
