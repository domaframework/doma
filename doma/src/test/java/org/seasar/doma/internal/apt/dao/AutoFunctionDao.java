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
package org.seasar.doma.internal.apt.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.InOut;
import org.seasar.doma.Out;
import org.seasar.doma.ResultSet;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.jdbc.Reference;

import example.domain.PhoneNumber;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface AutoFunctionDao {

    @Function
    String executeFunction(@In Integer arg1, @InOut Reference<Integer> arg2,
            @Out Reference<Integer> arg3);

    @Function
    PhoneNumber executeFunction2(@In PhoneNumber arg1,
            @InOut Reference<PhoneNumber> arg2, @Out Reference<PhoneNumber> arg3);

    @Function
    List<String> executeFunction3(@ResultSet List<String> arg1);

    @Function
    List<PhoneNumber> executeFunction4(@ResultSet List<PhoneNumber> arg1);

    @Function
    List<Emp> executeFunction5(@ResultSet List<Emp> arg1);

    @Function
    MyEnum executeFunction6(@In MyEnum arg1, @InOut Reference<MyEnum> arg2,
            @Out Reference<MyEnum> arg3);

    public enum MyEnum {
    }
}
