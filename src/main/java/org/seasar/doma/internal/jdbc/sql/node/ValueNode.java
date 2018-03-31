package org.seasar.doma.internal.jdbc.sql.node;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;

/** @author nakamura-to */
public abstract class ValueNode extends AbstractSqlNode {

  protected final SqlLocation location;

  protected final String variableName;

  protected final String text;

  protected WordNode wordNode;

  protected ParensNode parensNode;

  public ValueNode(SqlLocation location, String variableName, String text) {
    assertNotNull(location, variableName, text);
    this.location = location;
    this.variableName = variableName;
    this.text = text;
  }

  public SqlLocation getLocation() {
    return location;
  }

  public String getVariableName() {
    return variableName;
  }

  public String getText() {
    return text;
  }

  @Override
  public void appendNode(SqlNode child) {
    throw new JdbcUnsupportedOperationException(getClass().getName(), "addNode");
  }

  public WordNode getWordNode() {
    return wordNode;
  }

  public void setWordNode(WordNode wordNode) {
    this.wordNode = wordNode;
  }

  public ParensNode getParensNode() {
    return parensNode;
  }

  public void setParensNode(ParensNode parensNode) {
    this.parensNode = parensNode;
  }

  public boolean isWordNodeIgnored() {
    return wordNode != null;
  }

  public boolean isParensNodeIgnored() {
    return parensNode != null;
  }
}
