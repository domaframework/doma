package org.seasar.doma.jdbc.criteria.declaration;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.jdbc.criteria.context.InsertContext;
import org.seasar.doma.jdbc.criteria.context.SubSelectContext;

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

  public void duplicateKeyUpdate(Consumer<DuplicateKeyUpdateDeclaration> block) {
    Objects.requireNonNull(block);
    DuplicateKeyUpdateDeclaration declaration = new DuplicateKeyUpdateDeclaration(context);
    block.accept(declaration);
    context.validateUpsertFields();
  }

  public void duplicateKeyIgnore(Consumer<DuplicateKeyIgnoreDeclaration> block) {
    Objects.requireNonNull(block);
    DuplicateKeyIgnoreDeclaration declaration = new DuplicateKeyIgnoreDeclaration(context);
    block.accept(declaration);
    context.validateUpsertFields();
  }

  public void select(Function<InsertSelectDeclaration, SubSelectContext<?>> block) {
    InsertSelectDeclaration declaration = new InsertSelectDeclaration();
    SubSelectContext<?> subSelectContext = block.apply(declaration);
    context.selectContext = subSelectContext.get();
  }
}
