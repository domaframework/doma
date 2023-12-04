package org.seasar.doma.jdbc.criteria.statement;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.declaration.JoinDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.OrderByNameDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.AssociationOption;
import org.seasar.doma.jdbc.criteria.option.DistinctOption;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;

public class EntityqlSelectStarting<ENTITY>
    extends AbstractStatement<EntityqlSelectStarting<ENTITY>, List<ENTITY>>
    implements Listable<ENTITY> {

  private final SelectFromDeclaration declaration;
  private final EntityMetamodel<ENTITY> entityMetamodel;
  private final Optional<SubSelectContext<?>> subSelectContext;

  public EntityqlSelectStarting(
      Config config,
      SelectFromDeclaration declaration,
      EntityMetamodel<ENTITY> entityMetamodel,
      Optional<SubSelectContext<?>> subSelectContext) {
    super(Objects.requireNonNull(config));
    this.declaration = Objects.requireNonNull(declaration);
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    this.subSelectContext = subSelectContext;
  }

  public EntityqlSelectStarting<ENTITY> distinct() {
    declaration.distinct(DistinctOption.basic());
    return this;
  }

  public EntityqlSelectStarting<ENTITY> distinct(DistinctOption distinctOption) {
    Objects.requireNonNull(distinctOption);
    declaration.distinct(distinctOption);
    return this;
  }

  public EntityqlSelectStarting<ENTITY> innerJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    declaration.innerJoin(entityMetamodel, block);
    return this;
  }

  public EntityqlSelectStarting<ENTITY> leftJoin(
      EntityMetamodel<?> entityMetamodel, Consumer<JoinDeclaration> block) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(block);
    declaration.leftJoin(entityMetamodel, block);
    return this;
  }

  public <ENTITY1, ENTITY2> EntityqlSelectStarting<ENTITY> associate(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiConsumer<ENTITY1, ENTITY2> associator) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    declaration.associate(first, second, associator, AssociationOption.mandatory());
    return this;
  }

  public <ENTITY1, ENTITY2> EntityqlSelectStarting<ENTITY> associate(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiConsumer<ENTITY1, ENTITY2> associator,
      AssociationOption option) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    Objects.requireNonNull(option);
    declaration.associate(first, second, associator, option);
    return this;
  }

  public <ENTITY1, ENTITY2> EntityqlSelectStarting<ENTITY> associateWith(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiFunction<ENTITY1, ENTITY2, ENTITY1> associator) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    declaration.associateWith(first, second, associator, AssociationOption.mandatory());
    return this;
  }

  public <ENTITY1, ENTITY2> EntityqlSelectStarting<ENTITY> associateWith(
      EntityMetamodel<ENTITY1> first,
      EntityMetamodel<ENTITY2> second,
      BiFunction<ENTITY1, ENTITY2, ENTITY1> associator,
      AssociationOption option) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);
    Objects.requireNonNull(associator);
    Objects.requireNonNull(option);
    declaration.associateWith(first, second, associator, option);
    return this;
  }

  public EntityqlSelectStarting<ENTITY> where(Consumer<WhereDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.where(block);
    return this;
  }

  public EntityqlSelectStarting<ENTITY> orderBy(Consumer<OrderByNameDeclaration> block) {
    Objects.requireNonNull(block);
    declaration.orderBy(block);
    return this;
  }

  public EntityqlSelectStarting<ENTITY> limit(Integer limit) {
    declaration.limit(limit);
    return this;
  }

  public EntityqlSelectStarting<ENTITY> offset(Integer offset) {
    declaration.offset(offset);
    return this;
  }

  public EntityqlSelectStarting<ENTITY> forUpdate() {
    declaration.forUpdate(ForUpdateOption.basic());
    return this;
  }

  public EntityqlSelectStarting<ENTITY> forUpdate(ForUpdateOption option) {
    Objects.requireNonNull(option);
    declaration.forUpdate(option);
    return this;
  }

  public <RESULT> EntityqlSelectTerminal<RESULT> select(EntityMetamodel<RESULT> entityMetamodel) {
    Objects.requireNonNull(entityMetamodel);
    declaration.select(entityMetamodel);
    return new EntityqlSelectTerminal<>(config, declaration, entityMetamodel, subSelectContext);
  }

  public <RESULT> EntityqlSelectTerminal<RESULT> selectTo(
      EntityMetamodel<RESULT> entityMetamodel, PropertyMetamodel<?>... propertyMetamodels) {
    Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(propertyMetamodels);
    declaration.selectTo(entityMetamodel, Arrays.asList(propertyMetamodels));
    return new EntityqlSelectTerminal<>(config, declaration, entityMetamodel, subSelectContext);
  }

  @Override
  protected Command<List<ENTITY>> createCommand() {
    EntityqlSelectTerminal<ENTITY> terminal =
        new EntityqlSelectTerminal<>(config, declaration, entityMetamodel, subSelectContext);
    return terminal.createCommand();
  }
}
