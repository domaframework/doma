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

import java.math.BigDecimal;
import java.util.List;

import org.seasar.doma.internal.jdbc.dao.AbstractDao;
import org.seasar.doma.jdbc.SelectOptions;

import example.entity.Emp;

/**
 * @author taedium
 * 
 */
public class NoConfigEmpDaoImpl extends AbstractDao implements NoConfigEmpDao {

    /**
     * @param config
     *            the config
     */
    public NoConfigEmpDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public Emp selectById(Integer id, SelectOptions options) {
        return null;
    }

    @Override
    public List<Emp> selectByNameAndSalary(String name, BigDecimal salary,
            SelectOptions options) {
        return null;
    }

    @Override
    public int insert(Emp entity) {
        return 0;
    }

    @Override
    public int update(Emp entity) {
        return 0;
    }

    @Override
    public int delete(Emp entity) {
        return 0;
    }
}
