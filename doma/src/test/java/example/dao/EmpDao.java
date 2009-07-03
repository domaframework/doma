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
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.domain.ArrayDomain;
import org.seasar.doma.jdbc.domain.BlobDomain;

import example.entity.Emp;

/**
 * 
 * @author taedium
 * 
 */
@Dao(config = ExampleConfig.class)
public interface EmpDao {

    @Select
    Emp selectById(IntegerDomain id, SelectOptions option);

    @Select
    List<Emp> selectByNameAndSalary(StringDomain name, BigDecimalDomain salary,
            SelectOptions option);

    @Select(iteration = true)
    Integer iterate(IterationCallback<Integer, Emp> callback);

    @Insert
    int insert(Emp entity);

    @Update
    int update(Emp entity);

    @Delete
    int delete(Emp entity);

    @Function
    IntegerDomain add(@In IntegerDomain arg1, @In IntegerDomain arg2);

    @Function
    List<Emp> getEmps(@In IntegerDomain arg1, @In IntegerDomain arg2);

    @Procedure
    void exec(@In IntegerDomain arg1, @In IntegerDomain arg2);

    @ArrayFactory(typeName = "varchar")
    ArrayDomain<String> createStringArrayDomain(String[] element);

    @BlobFactory
    BlobDomain createBlobDomain();
}
