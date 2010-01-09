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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.SqlNodeVisitor;

/**
 * @author taedium
 * 
 */
public class EolNode extends AbstractSqlNode {

    protected final String eol;

    public EolNode(String eol) {
        assertNotNull(eol);
        this.eol = eol;
    }

    public String getEol() {
        return eol;
    }

    public void clearChildren() {
        children.clear();
    }

    @Override
    public EolNode copy() {
        return this;
    }

    @Override
    public <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p) {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof EolNodeVisitor<?, ?>) {
            @SuppressWarnings("unchecked")
            EolNodeVisitor<R, P> v = (EolNodeVisitor) visitor;
            return v.visitEolNode(this, p);
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
}
