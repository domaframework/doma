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

import java.util.HashMap;
import java.util.Map;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;

public class OtherNode extends AbstractSqlNode {

    protected static final Map<String, OtherNode> otherNodeMap = new HashMap<String, OtherNode>();
    static {
        otherNodeMap.put(",", new OtherNode(","));
        otherNodeMap.put("=", new OtherNode("="));
        otherNodeMap.put(">", new OtherNode(">"));
        otherNodeMap.put("<", new OtherNode("<"));
        otherNodeMap.put("-", new OtherNode("-"));
        otherNodeMap.put("+", new OtherNode("+"));
        otherNodeMap.put("*", new OtherNode("*"));
        otherNodeMap.put("/", new OtherNode("/"));
        otherNodeMap.put("(", new OtherNode("("));
        otherNodeMap.put(")", new OtherNode(")"));
        otherNodeMap.put(";", new OtherNode(";"));
    }

    protected final String other;

    private OtherNode(String other) {
        assertNotNull(other);
        this.other = other;
    }

    public String getOther() {
        return other;
    }

    @Override
    public void appendNode(SqlNode child) {
        throw new JdbcUnsupportedOperationException(getClass().getName(),
                "addNode");

    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitOtherNode(this, p);
    }

    public static OtherNode of(String other) {
        OtherNode otherNode = otherNodeMap.get(other);
        if (otherNode != null) {
            return otherNode;
        }
        return new OtherNode(other);
    }

}
