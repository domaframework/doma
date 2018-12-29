package org.seasar.doma.jdbc;

import java.util.List;
import org.seasar.doma.DomaNullPointerException;

/**
 * An SQL node.
 *
 * <p>The implementation class is not required to be thread safe.
 *
 * <p>Treat this object for read only when you can not control its life cycle.
 */
public interface SqlNode {

  /**
   * Returns the children list.
   *
   * @return the children list
   */
  List<SqlNode> getChildren();

  /**
   * Accepts the visitor.
   *
   * @param <R> the result type
   * @param <P> the parameter type
   * @param visitor the visitor
   * @param p the parameter for the visitor
   * @return the result
   * @throws DomaNullPointerException if {@code visitor} is {@code null}
   */
  <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p);
}
