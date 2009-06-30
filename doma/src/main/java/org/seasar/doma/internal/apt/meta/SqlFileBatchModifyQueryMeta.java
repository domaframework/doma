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
package org.seasar.doma.internal.apt.meta;


/**
 * @author taedium
 * 
 */
public class SqlFileBatchModifyQueryMeta extends AbstractSqlFileQueryMeta {

    protected String elementTypeName;

    protected String entityListTypeName;

    protected String entityListName;

    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
    }

    public String getEntityListTypeName() {
        return entityListTypeName;
    }

    public void setEntityListTypeName(String entityListTypeName) {
        this.entityListTypeName = entityListTypeName;
    }

    public String getEntityListName() {
        return entityListName;
    }

    public void setEntityListName(String entityListName) {
        this.entityListName = entityListName;
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitSqlFileBatchModifyQueryMeta(this, p);
    }

}
