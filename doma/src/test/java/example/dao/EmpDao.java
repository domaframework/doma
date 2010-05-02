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
package example.dao;

import java.math.BigDecimal;
import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
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
    Emp selectById(Integer id, SelectOptions option);

    @Select
    List<Emp> selectByNameAndSalary(String name, BigDecimal salary,
            SelectOptions option);

    @Select
    List<Emp> selectByExample(Emp emp);

    @Select(iterate = true)
    Integer iterate(IterationCallback<Integer, Emp> callback);

    @Insert
    int insert(Emp entity);

    @Update
    int update(Emp entity);

    @Delete
    int delete(Emp entity);

    @Script
    void execute();
}
