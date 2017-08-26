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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;

import org.seasar.doma.jdbc.ResultListParameter;
import org.seasar.doma.jdbc.SqlParameterVisitor;

/**
 * @author taedium
 * 
 */
public abstract class AbstractResultListParameter<ELEMENT> implements ResultListParameter<ELEMENT> {

    protected final List<ELEMENT> list;

    public AbstractResultListParameter(List<ELEMENT> list) {
        assertNotNull(list);
        this.list = list;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void add(ELEMENT element) {
        list.add(element);
    }

    @Override
    public Object getValue() {
        return list;
    }

    @Override
    public List<ELEMENT> getResult() {
        return list;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(SqlParameterVisitor<R, P, TH> visitor, P p)
            throws TH {
        return visitor.visitResultListParameter(this, p);
    }
}
