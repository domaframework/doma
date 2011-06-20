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
public class SelectClauseNode extends AbstractClauseNode {

    public SelectClauseNode(String word) {
        super(word);
    }

    public SelectClauseNode(WordNode wordNode) {
        super(wordNode);
    }

    @SuppressWarnings("deprecation")
    @Override
    public SelectClauseNode copy() {
        SelectClauseNode clone = new SelectClauseNode(wordNode.copy());
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
        if (visitor instanceof SelectClauseNodeVisitor<?, ?>) {
            SelectClauseNodeVisitor<R, P> v = (SelectClauseNodeVisitor<R, P>) visitor;
            return v.visitSelectClauseNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }
}