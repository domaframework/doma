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
package example.dao;

import java.util.List;

import org.seasar.doma.ArrayFactory;
import org.seasar.doma.BlobFactory;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Function;
import org.seasar.doma.In;
import org.seasar.doma.Insert;
import org.seasar.doma.Procedure;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.domain.BuiltinArrayDomain;
import org.seasar.doma.domain.BuiltinBigDecimalDomain;
import org.seasar.doma.domain.BuiltinBlobDomain;
import org.seasar.doma.domain.BuiltinIntegerDomain;
import org.seasar.doma.domain.BuiltinStringDomain;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;

import example.entity.Emp;

/**
 * 
 * @author taedium
 * 
 */
@Dao(config = ExampleConfig.class)
public interface EmpDao {

    @Select
    Emp selectById(BuiltinIntegerDomain id, SelectOptions option);

    @Select
    List<Emp> selectByNameAndSalary(BuiltinStringDomain name, BuiltinBigDecimalDomain salary,
            SelectOptions option);

    @Select(iterate = true)
    Integer iterate(IterationCallback<Integer, Emp> callback);

    @Insert
    int insert(Emp entity);

    @Update
    int update(Emp entity);

    @Delete
    int delete(Emp entity);

    @Function
    BuiltinIntegerDomain add(@In BuiltinIntegerDomain arg1, @In BuiltinIntegerDomain arg2);

    @Function
    List<Emp> getEmps(@In BuiltinIntegerDomain arg1, @In BuiltinIntegerDomain arg2);

    @Procedure
    void exec(@In BuiltinIntegerDomain arg1, @In BuiltinIntegerDomain arg2);

    @ArrayFactory(typeName = "varchar")
    BuiltinArrayDomain<String> createStringArrayDomain(String[] element);

    @BlobFactory
    BuiltinBlobDomain createBlobDomain();
}
