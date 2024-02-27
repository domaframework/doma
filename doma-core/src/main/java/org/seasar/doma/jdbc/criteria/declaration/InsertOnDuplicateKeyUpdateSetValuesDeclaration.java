package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class InsertOnDuplicateKeyUpdateSetValuesDeclaration {

  private final InsertContext context;

  public InsertOnDuplicateKeyUpdateSetValuesDeclaration(InsertContext context) {
    this.context = Objects.requireNonNull(context);
  }

  /**
   * Adds a value to the upsert set clause.
   *
   * @param left The left property metamodel.
   * @param right The right value.
   * @param <PROPERTY> The type of property.
   */
  public <PROPERTY> void value(PropertyMetamodel<PROPERTY> left, PROPERTY right) {
    Objects.requireNonNull(left);
    this.context.onDuplicateContext.setValues.put(
        new Operand.Prop(left), new Operand.Param(left, right));
  }

  /**
   * Adds a value to the upsert set clause.
   *
   * @param left The left property metamodel.
   * @param right The right operand.
   * @param <PROPERTY> The type of property.
   */
  public <PROPERTY> void value(PropertyMetamodel<PROPERTY> left, Operand.Prop right) {
    Objects.requireNonNull(left);
    this.context.onDuplicateContext.setValues.put(new Operand.Prop(left), right);
  }

  /**
   * References the records that excluded from insertion into a table.
   *
   * @param excludedPropertyMeta Specifies columns excluded from the insert record.specify what is
   *     in PropertyMetamodel set in `values`.
   * @param <PROPERTY> The type of property.
   * @return set clause operand
   */
  public <PROPERTY> Operand.Prop excluded(PropertyMetamodel<PROPERTY> excludedPropertyMeta) {
    Objects.requireNonNull(excludedPropertyMeta);
    return new Operand.Prop(excludedPropertyMeta);
  }
}
