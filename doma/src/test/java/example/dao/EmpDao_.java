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

import javax.annotation.Generated;
import javax.sql.DataSource;

import org.seasar.doma.domain.BuiltinArrayDomain;
import org.seasar.doma.domain.BuiltinBigDecimalDomain;
import org.seasar.doma.domain.BuiltinBlobDomain;
import org.seasar.doma.domain.BuiltinIntegerDomain;
import org.seasar.doma.domain.BuiltinStringDomain;
import org.seasar.doma.internal.jdbc.command.CreateCommand;
import org.seasar.doma.internal.jdbc.command.DeleteCommand;
import org.seasar.doma.internal.jdbc.command.EntityIterationHandler;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.FunctionCommand;
import org.seasar.doma.internal.jdbc.command.InsertCommand;
import org.seasar.doma.internal.jdbc.command.ProcedureCommand;
import org.seasar.doma.internal.jdbc.command.SelectCommand;
import org.seasar.doma.internal.jdbc.command.UpdateCommand;
import org.seasar.doma.internal.jdbc.query.ArrayCreateQuery;
import org.seasar.doma.internal.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.internal.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.internal.jdbc.query.AutoInsertQuery;
import org.seasar.doma.internal.jdbc.query.AutoProcedureQuery;
import org.seasar.doma.internal.jdbc.query.AutoUpdateQuery;
import org.seasar.doma.internal.jdbc.query.BlobCreateQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.internal.jdbc.sql.DomainResultParameter;
import org.seasar.doma.internal.jdbc.sql.EntityListResultParameter;
import org.seasar.doma.internal.jdbc.sql.InParameter;
import org.seasar.doma.internal.jdbc.sql.SqlFileUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DomaAbstractDao;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;

import example.entity.Emp;
import example.entity.Emp_;

/**
 * @author taedium
 * 
 */
@Generated("")
public class EmpDao_ extends DomaAbstractDao implements EmpDao {

    public EmpDao_() {
        super(new ExampleConfig(), null);
    }

    public EmpDao_(DataSource dataSource) {
        super(new ExampleConfig(), dataSource);
    }

    protected EmpDao_(Config config) {
        super(config, config.dataSource());
    }

    @Override
    public Emp selectById(BuiltinIntegerDomain id, SelectOptions option) {
        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(config);
        query.setSqlFilePath(SqlFileUtil
                .buildPath("example.dao.EmpDao", "selectById"));
        query.addParameter("id", id);
        query.setOptions(option);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectById");
        query.compile();
        SelectCommand<Emp> command = new SelectCommand<Emp>(query,
                new EntitySingleResultHandler<Emp, Emp_>(Emp_.class));
        return command.execute();
    }

    @Override
    public List<Emp> selectByNameAndSalary(BuiltinStringDomain name,
            BuiltinBigDecimalDomain salary, SelectOptions option) {
        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(config);
        query.setSqlFilePath(SqlFileUtil
                .buildPath("example.dao.EmpDao", "selectByNameAndSalary"));
        query.addParameter("name", name);
        query.addParameter("salary", salary);
        query.setOptions(option);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectByNameAndSalary");
        query.compile();
        SelectCommand<List<Emp>> command = new SelectCommand<List<Emp>>(query,
                new EntityResultListHandler<Emp, Emp_>(Emp_.class));
        return command.execute();
    }

    @Override
    public int insert(Emp entity) {
        AutoInsertQuery<Emp, Emp_> query = new AutoInsertQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(config);
        query.setEntity(entity);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("insert");
        query.compile();
        InsertCommand command = new InsertCommand(query);
        return command.execute();
    }

    @Override
    public int update(Emp entity) {
        AutoUpdateQuery<Emp, Emp_> query = new AutoUpdateQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(config);
        query.setEntity(entity);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("update");
        query.compile();
        UpdateCommand command = new UpdateCommand(query);
        return command.execute();
    }

    @Override
    public int delete(Emp entity) {
        AutoDeleteQuery<Emp, Emp_> query = new AutoDeleteQuery<Emp, Emp_>(
                Emp_.class);
        query.setConfig(config);
        query.setEntity(entity);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("update");
        query.compile();
        DeleteCommand command = new DeleteCommand(query);
        return command.execute();
    }

    @Override
    public Integer iterate(IterationCallback<Integer, Emp> callback) {
        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(config);
        query.setSqlFilePath(SqlFileUtil
                .buildPath("example.dao.EmpDao", "selectById"));
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectById");
        query.compile();
        SelectCommand<Integer> command = new SelectCommand<Integer>(query,
                new EntityIterationHandler<Integer, Emp, Emp_>(Emp_.class,
                        callback));
        return command.execute();
    }

    @Override
    public BuiltinIntegerDomain add(BuiltinIntegerDomain arg1, BuiltinIntegerDomain arg2) {
        AutoFunctionQuery<BuiltinIntegerDomain> query = new AutoFunctionQuery<BuiltinIntegerDomain>();
        query.setConfig(config);
        query.setFunctionName("add");
        query.setResultParameter((new DomainResultParameter<BuiltinIntegerDomain>(
                BuiltinIntegerDomain.class)));
        query.addParameter(new InParameter(arg1));
        query.addParameter(new InParameter(arg2));
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("add");
        query.compile();
        FunctionCommand<BuiltinIntegerDomain> command = new FunctionCommand<BuiltinIntegerDomain>(
                query);
        return command.execute();
    }

    @Override
    public List<Emp> getEmps(BuiltinIntegerDomain arg1, BuiltinIntegerDomain arg2) {
        AutoFunctionQuery<List<Emp>> query = new AutoFunctionQuery<List<Emp>>();
        query.setConfig(config);
        query.setFunctionName("add");
        query.setResultParameter((new EntityListResultParameter<Emp, Emp_>(
                Emp_.class)));
        query.addParameter(new InParameter(arg1));
        query.addParameter(new InParameter(arg2));
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("add");
        query.compile();
        FunctionCommand<List<Emp>> command = new FunctionCommand<List<Emp>>(
                query);
        return command.execute();
    }

    @Override
    public void exec(BuiltinIntegerDomain arg1, BuiltinIntegerDomain arg2) {
        AutoProcedureQuery query = new AutoProcedureQuery();
        query.setConfig(config);
        query.setProcedureName("exec");
        query.addParameter(new InParameter(arg1));
        query.addParameter(new InParameter(arg2));
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("add");
        query.compile();
        ProcedureCommand command = new ProcedureCommand(query);
        command.execute();
    }

    @Override
    public BuiltinArrayDomain<String> createStringArrayDomain(String[] element) {
        ArrayCreateQuery<BuiltinArrayDomain<String>> query = new ArrayCreateQuery<BuiltinArrayDomain<String>>();
        query.setConfig(config);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("createStringArray");
        query.setResult(new BuiltinArrayDomain<String>());
        query.compile();
        CreateCommand<BuiltinArrayDomain<String>> command = new CreateCommand<BuiltinArrayDomain<String>>(
                query);
        return command.execute();
    }

    @Override
    public BuiltinBlobDomain createBlobDomain() {
        BlobCreateQuery<BuiltinBlobDomain> query = new BlobCreateQuery<BuiltinBlobDomain>();
        query.setConfig(config);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("createStringArray");
        query.setResult(new BuiltinBlobDomain());
        query.compile();
        CreateCommand<BuiltinBlobDomain> command = new CreateCommand<BuiltinBlobDomain>(query);
        return command.execute();
    }
}
