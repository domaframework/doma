package org.seasar.doma.jdbc.criteria.expression;

import java.util.Objects;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.declaration.SubSelectFromDeclaration;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

public class SelectExpression<PROPERTY> implements PropertyMetamodel<PROPERTY> {

  private final SubSelectContext.Single<PROPERTY> subSelectContext;
  public final SelectContext context;
  public final PropertyMetamodel<?> propertyMetamodel;

  public SelectExpression(SubSelectContext.Single<PROPERTY> subSelectContext) {
    this.subSelectContext = Objects.requireNonNull(subSelectContext);
    this.context = subSelectContext.get();
    this.propertyMetamodel = subSelectContext.getPropertyMetamodel();
  }

  @Override
  public Class<?> asClass() {
    return propertyMetamodel.asClass();
  }

  @Override
  public EntityPropertyType<?, ?> asType() {
    return propertyMetamodel.asType();
  }

  @Override
  public String getName() {
    return propertyMetamodel.getName();
  }

  @Override
  public void accept(PropertyMetamodel.Visitor visitor) {
    if (visitor instanceof SelectExpression.Visitor) {
      SelectExpression.Visitor v = (SelectExpression.Visitor) visitor;
      v.visit(this);
    }
  }

  public static class Declaration {
    public <ENTITY> SubSelectFromDeclaration<ENTITY> from(EntityMetamodel<ENTITY> entityMetamodel) {
      return new SubSelectFromDeclaration<>(entityMetamodel);
    }
  }

  public interface Visitor {
    void visit(SelectExpression<?> expression);
  }
}
