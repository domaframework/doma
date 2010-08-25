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
import java.sql.Connection;
import java.util.List;

import javax.annotation.Generated;
import javax.sql.DataSource;

import org.seasar.doma.internal.jdbc.command.DeleteCommand;
import org.seasar.doma.internal.jdbc.command.EntityIterationHandler;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.InsertCommand;
import org.seasar.doma.internal.jdbc.command.ScriptCommand;
import org.seasar.doma.internal.jdbc.command.SelectCommand;
import org.seasar.doma.internal.jdbc.command.UpdateCommand;
import org.seasar.doma.internal.jdbc.dao.AbstractDao;
import org.seasar.doma.internal.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.internal.jdbc.query.AutoInsertQuery;
import org.seasar.doma.internal.jdbc.query.AutoUpdateQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileScriptQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;

import example.entity.Emp;
import example.entity._Emp;

/**
 * @author taedium
 * 
 */
@Generated("")
public class EmpDaoImpl extends AbstractDao implements EmpDao {

    public EmpDaoImpl() {
        super(new ExampleConfig());
    }

    public EmpDaoImpl(Connection connection) {
        super(new ExampleConfig(), connection);
    }

    public EmpDaoImpl(DataSource dataSource) {
        super(new ExampleConfig(), dataSource);
    }

    @Override
    public Emp selectById(Integer id, SelectOptions option) {
        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(config);
        query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao",
                "selectById"));
        query.addParameter("id", Integer.class, id);
        query.setOptions(option);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectById");
        query.prepare();
        SelectCommand<Emp> command = new SelectCommand<Emp>(query,
                new EntitySingleResultHandler<Emp>(_Emp.getSingletonInternal()));
        return command.execute();
    }

    @Override
    public List<Emp> selectByNameAndSalary(String name, BigDecimal salary,
            SelectOptions option) {
        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(config);
        query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao",
                "selectByNameAndSalary"));
        query.addParameter("name", String.class, name);
        query.addParameter("salary", BigDecimal.class, salary);
        query.setOptions(option);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectByNameAndSalary");
        query.prepare();
        SelectCommand<List<Emp>> command = new SelectCommand<List<Emp>>(query,
                new EntityResultListHandler<Emp>(_Emp.getSingletonInternal()));
        return command.execute();
    }

    @Override
    public List<Emp> selectByExample(Emp emp) {
        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(config);
        query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao",
                "selectByNameAndSalary"));
        query.addParameter("emp", Emp.class, emp);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectByNameAndSalary");
        query.prepare();
        SelectCommand<List<Emp>> command = new SelectCommand<List<Emp>>(query,
                new EntityResultListHandler<Emp>(_Emp.getSingletonInternal()));
        return command.execute();
    }

    @Override
    public int insert(Emp entity) {
        AutoInsertQuery<Emp> query = new AutoInsertQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setConfig(config);
        query.setEntity(entity);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("insert");
        query.prepare();
        InsertCommand command = new InsertCommand(query);
        return command.execute();
    }

    @Override
    public int update(Emp entity) {
        AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setConfig(config);
        query.setEntity(entity);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("update");
        query.prepare();
        UpdateCommand command = new UpdateCommand(query);
        return command.execute();
    }

    @Override
    public int delete(Emp entity) {
        AutoDeleteQuery<Emp> query = new AutoDeleteQuery<Emp>(
                _Emp.getSingletonInternal());
        query.setConfig(config);
        query.setEntity(entity);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("update");
        query.prepare();
        DeleteCommand command = new DeleteCommand(query);
        return command.execute();
    }

    @Override
    public Integer iterate(IterationCallback<Integer, Emp> callback) {
        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(config);
        query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao",
                "selectById"));
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectById");
        query.prepare();
        SelectCommand<Integer> command = new SelectCommand<Integer>(query,
                new EntityIterationHandler<Integer, Emp>(
                        _Emp.getSingletonInternal(), callback));
        return command.execute();
    }

    @Override
    public void execute() {
        SqlFileScriptQuery query = new SqlFileScriptQuery();
        query.setConfig(config);
        query.setScriptFilePath(SqlFileUtil.buildPath("example.dao.EmpDao",
                "selectById"));
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectById");
        query.prepare();
        ScriptCommand command = new ScriptCommand(query);
        command.execute();
    }

}
