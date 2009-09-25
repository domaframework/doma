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
package org.seasar.doma.it.entity;

import java.util.HashSet;
import java.util.Set;

import org.seasar.doma.Entity;
import org.seasar.doma.ModifiedProperties;
import org.seasar.doma.Table;

@Entity
@Table(name = "NO_ID")
public class NoId {

    Integer value1;

    Integer value2;

    @ModifiedProperties
    Set<String> modifiedProperties = new HashSet<String>();

    public Integer getValue1() {
        return value1;
    }

    public void setValue1(Integer value1) {
        modifiedProperties.add("value1");
        this.value1 = value1;
    }

    public Integer getValue2() {
        return value2;
    }

    public void setValue2(Integer value2) {
        modifiedProperties.add("value2");
        this.value2 = value2;
    }

}
