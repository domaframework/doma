/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaUnsupportedOperationException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;


/**
 * @author taedium
 * 
 */
public class IfBlockNode extends AbstractSqlNode {

    protected IfNode ifNode;

    protected List<ElseifNode> elseifNodes = new ArrayList<ElseifNode>();

    protected ElseNode elseNode;

    protected EndNode endNode;

    public IfBlockNode() {
    }

    public void setIfNode(IfNode ifNode) {
        this.ifNode = ifNode;
    }

    public IfNode getIfNode() {
        return ifNode;
    }

    public void addElseifNode(ElseifNode elseIfNode) {
        elseifNodes.add(elseIfNode);
    }

    public List<ElseifNode> getElseifNodes() {
        return elseifNodes;
    }

    public void setElseNode(ElseNode elseNode) {
        this.elseNode = elseNode;
    }

    public ElseNode getElseNode() {
        return elseNode;
    }

    public void setEndNode(EndNode endNode) {
        this.endNode = endNode;
    }

    public EndNode getEndNode() {
        return endNode;
    }

    public boolean isElseNodeExistent() {
        return elseNode != null;
    }

    @Override
    public void addNode(SqlNode child) {
        throw new DomaUnsupportedOperationException(getClass().getName(),
                "addNode");

    }

    @Override
    public IfBlockNode copy() {
        IfBlockNode clone = new IfBlockNode();
        clone.ifNode = ifNode.copy();
        for (ElseifNode elseifNode : elseifNodes) {
            clone.elseifNodes.add(elseifNode.copy());
        }
        if (elseNode != null) {
            clone.elseNode = elseNode.copy();
        }
        clone.endNode = endNode.copy();
        for (SqlNode child : children) {
            clone.addNode(child.copy());
        }
        return clone;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (IfBlockNodeVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            IfBlockNodeVisitor<R, P> v = IfBlockNodeVisitor.class.cast(visitor);
            return v.visitIfBlockNode(this, p);
        }
        return visitor.visitUnknownNode(this, p);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(ifNode);
        for (ElseifNode elseIfNode : elseifNodes) {
            buf.append(elseIfNode);
        }
        if (elseNode != null) {
            buf.append(elseNode);
        }
        buf.append(endNode);
        return buf.toString();
    }

}
