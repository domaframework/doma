package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

/**
 * Represents a set operand.
 *
 * @param <ELEMENT> the element type of the set operation result
 */
public interface SetOperand<ELEMENT> extends StreamMappable<ELEMENT> {

  /**
   * Returns the set operation context
   *
   * @return the context
   */
  SetOperationContext<ELEMENT> getContext();

  /**
   * Creates an UNION statement
   *
   * @param other the UNION operand
   * @return the UNION operator
   */
  SetOperator<ELEMENT> union(SetOperand<ELEMENT> other);

  /**
   * Creates an UNION ALL statement
   *
   * @param other the UNION ALL operand
   * @return the UNION ALL operator
   */
  SetOperator<ELEMENT> unionAll(SetOperand<ELEMENT> other);

  @Override
  SetOperand<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
