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

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author nakamura-to
 * 
 */
public class BinaryOperatorNode extends AbstractSqlNode {

    protected final String op;

    protected final OpKind opKind;

    public BinaryOperatorNode(String op, OpKind opKind) {
        assertNotNull(op, opKind);
        this.op = op;
        this.opKind = opKind;
    }

    public String getOp() {
        return op;
    }

    public OpKind getOpKind() {
        return opKind;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitBinaryOperatorNode(this, p);
    }

    public enum OpKind {
        EQ, NE
    }
}