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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.HashMap;
import java.util.Map;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class WhitespaceNode extends AbstractSqlNode {

    protected static final Map<String, WhitespaceNode> nodeMap = new HashMap<String, WhitespaceNode>();
    static {
        nodeMap.put(String.valueOf('\u0009'), new WhitespaceNode('\u0009'));
        nodeMap.put(String.valueOf('\u000B'), new WhitespaceNode('\u000B'));
        nodeMap.put(String.valueOf('\u000C'), new WhitespaceNode('\u000C'));
        nodeMap.put(String.valueOf('\u001C'), new WhitespaceNode('\u001C'));
        nodeMap.put(String.valueOf('\u001D'), new WhitespaceNode('\u001D'));
        nodeMap.put(String.valueOf('\u001E'), new WhitespaceNode('\u001E'));
        nodeMap.put(String.valueOf('\u001F'), new WhitespaceNode('\u001F'));
        nodeMap.put(String.valueOf('\u0020'), new WhitespaceNode('\u0020'));
    }

    protected final String whitespace;

    private WhitespaceNode(char whitespace) {
        this(String.valueOf(whitespace));
    }

    private WhitespaceNode(String whitespace) {
        assertNotNull(whitespace);
        this.whitespace = whitespace;
    }

    public String getWhitespace() {
        return whitespace;
    }

    @Override
    public void addNode(SqlNode child) {
        throw new JdbcUnsupportedOperationException(getClass().getName(),
                "addNode");

    }

    @SuppressWarnings("deprecation")
    @Override
    public WhitespaceNode copy() {
        return this;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof WhitespaceNodeVisitor<?, ?>) {
            WhitespaceNodeVisitor<R, P> v = (WhitespaceNodeVisitor<R, P>) visitor;
            return v.visitWhitespaceNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        buf.append(getClass().getSimpleName());
        buf.append("]");
        return buf.toString();
    }

    public static WhitespaceNode of(String whitespace) {
        WhitespaceNode whitespaceNode = nodeMap.get(whitespace);
        if (whitespaceNode != null) {
            return whitespaceNode;
        }
        return new WhitespaceNode(whitespace);
    }

}
