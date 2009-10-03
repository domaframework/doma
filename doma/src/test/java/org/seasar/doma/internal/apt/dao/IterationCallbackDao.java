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
package org.seasar.doma.internal.apt.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.entity.Emp;
import org.seasar.doma.jdbc.IterationCallback;

import example.domain.PhoneNumber;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface IterationCallbackDao {

    @Select(iterate = true)
    Integer iterateByIdAndName(Integer id, String name,
            IterationCallback<Integer, Emp> callback);

    @Select(iterate = true)
    <R> R iterateById(Integer id, IterationCallback<R, PhoneNumber> callback);

    @Select(iterate = true)
    <R extends Number> R iterate(IterationCallback<R, String> callback);

    @Select(iterate = true)
    String iterateWithHogeIterationCallback(HogeIterationCallback callback);
}
