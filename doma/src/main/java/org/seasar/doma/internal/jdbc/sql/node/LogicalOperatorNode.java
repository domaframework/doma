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

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class LogicalOperatorNode extends AbstractSqlNode {

    protected final WordNode wordNode;

    public LogicalOperatorNode(String word) {
        this(new WordNode(word));
    }

    protected LogicalOperatorNode(WordNode wordNode) {
        this.wordNode = wordNode;
    }

    public WordNode getWordNode() {
        return wordNode;
    }

    @SuppressWarnings("deprecation")
    @Override
    public LogicalOperatorNode copy() {
        LogicalOperatorNode clone = new LogicalOperatorNode(wordNode.copy());
        for (SqlNode child : children) {
            clone.addNode(child.copy());
        }
        return clone;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof LogicalOperatorNodeVisitor<?, ?>) {
            LogicalOperatorNodeVisitor<R, P> v = (LogicalOperatorNodeVisitor<R, P>) visitor;
            return v.visitLogicalOperatorNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        buf.append(getClass().getSimpleName());
        buf.append(" ");
        buf.append(wordNode);
        for (SqlNode child : children) {
            buf.append(child);
        }
        buf.append("]");
        return buf.toString();
    }
}