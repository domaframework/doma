package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.Operand;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;

public class InsertDeclaration {

  private final InsertContext context;

  public InsertDeclaration(InsertContext context) {
    this.context = Objects.requireNonNull(context);
  }

  public InsertContext getContext() {
    return context;
  }

  public void values(Consumer<ValuesDeclaration> block) {
    Objects.requireNonNull(block);
    ValuesDeclaration declaration = new ValuesDeclaration(context);
    block.accept(declaration);
  }

  public void upsertSetValues(Consumer<UpsertSetValuesDeclaration> block) {
    Objects.requireNonNull(block);
    UpsertSetValuesDeclaration declaration = new UpsertSetValuesDeclaration(context);
    block.accept(declaration);
    if (context.upsertSetValues.isEmpty()) {
      for (Map.Entry<Operand.Prop, Operand.Param> entry : context.values.entrySet()) {
        context.upsertSetValues.add(new Tuple2<>(entry.getKey(), entry.getValue()));
      }
    }
  }

  public void select(Function<InsertSelectDeclaration, SubSelectContext<?>> block) {
    InsertSelectDeclaration declaration = new InsertSelectDeclaration();
    SubSelectContext<?> subSelectContext = block.apply(declaration);
    context.selectContext = subSelectContext.get();
  }
}
