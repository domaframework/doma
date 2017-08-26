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

import org.seasar.doma.internal.jdbc.command.EntityProvider;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.query.Query;

/**
 * @author taedium
 * 
 */
public class EntityListParameter<ENTITY> extends AbstractListParameter<ENTITY> {

    protected final EntityDesc<ENTITY> entityDesc;

    protected final boolean resultMappingEnsured;

    public EntityListParameter(EntityDesc<ENTITY> entityDesc, List<ENTITY> list, String name,
            boolean resultMappingEnsured) {
        super(list, name);
        assertNotNull(entityDesc);
        this.entityDesc = entityDesc;
        this.resultMappingEnsured = resultMappingEnsured;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EntityProvider<ENTITY> createObjectProvider(Query query) {
        return new EntityProvider<>(entityDesc, query, resultMappingEnsured);
    }

}
