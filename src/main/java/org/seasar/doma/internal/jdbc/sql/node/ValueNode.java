/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
