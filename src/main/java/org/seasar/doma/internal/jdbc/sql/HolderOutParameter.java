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

import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.holder.HolderDesc;

/**
 * @author taedium
 * @param <BASIC>
 *            基本型
 * @param <HOLDER>
 *            ドメイン型
 * 
 */
public class HolderOutParameter<BASIC, HOLDER> extends
        ScalarOutParameter<BASIC, HOLDER> {

    public HolderOutParameter(HolderDesc<BASIC, HOLDER> holderDesc,
            Reference<HOLDER> reference) {
        super(holderDesc.createScalar(), reference);
    }

}
