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

import org.seasar.doma.internal.jdbc.command.ScalarProvider;
import org.seasar.doma.jdbc.holder.HolderDesc;
import org.seasar.doma.jdbc.query.Query;

/**
 * @author taedium
 * 
 */
public class HolderListParameter<BASIC, HOLDER> extends
        AbstractListParameter<HOLDER> {

    protected final HolderDesc<BASIC, HOLDER> holderDesc;

    public HolderListParameter(HolderDesc<BASIC, HOLDER> holderDesc,
            List<HOLDER> list, String name) {
        super(list, name);
        assertNotNull(holderDesc);
        this.holderDesc = holderDesc;
    }

    @Override
    public ScalarProvider<BASIC, HOLDER> createObjectProvider(Query query) {
        return new ScalarProvider<BASIC, HOLDER>(
                () -> holderDesc.createScalar(), query);
    }

}
