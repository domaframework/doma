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

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Generated;
import javax.sql.DataSource;

import org.seasar.doma.domain.BigDecimalWrapper;
import org.seasar.doma.domain.IntegerWrapper;
import org.seasar.doma.domain.StringWrapper;
import org.seasar.doma.internal.jdbc.command.DeleteCommand;
import org.seasar.doma.internal.jdbc.command.EntityIterationHandler;
import org.seasar.doma.internal.jdbc.command.EntityResultListHandler;
import org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler;
import org.seasar.doma.internal.jdbc.command.InsertCommand;
import org.seasar.doma.internal.jdbc.command.SelectCommand;
import org.seasar.doma.internal.jdbc.command.UpdateCommand;
import org.seasar.doma.internal.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.internal.jdbc.query.AutoInsertQuery;
import org.seasar.doma.internal.jdbc.query.AutoUpdateQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.internal.jdbc.sql.SqlFileUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.DomaAbstractDao;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.entity.EntityMeta;

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
    public Emp selectById(Integer id, SelectOptions option) {
        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(config);
        query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao",
                "selectById"));
        query.addParameter("id", new IntegerWrapper(id));
        query.setOptions(option);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectById");
        query.prepare();
        SelectCommand<Emp> command = new SelectCommand<Emp>(query,
                new EntitySingleResultHandler<Emp>(new Emp_()));
        return command.execute();
    }

    @Override
    public List<Emp> selectByNameAndSalary(String name, BigDecimal salary,
            SelectOptions option) {
        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(config);
        query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao",
                "selectByNameAndSalary"));
        query.addParameter("name", new StringWrapper(name));
        query.addParameter("salary", new BigDecimalWrapper(salary));
        query.setOptions(option);
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectByNameAndSalary");
        query.prepare();
        SelectCommand<List<Emp>> command = new SelectCommand<List<Emp>>(query,
                new EntityResultListHandler<Emp>(new Emp_()));
        return command.execute();
    }

    @Override
    public List<Emp> selectByExample(Emp emp) {
        EntityMeta<Emp> empMeta = new Emp_().createEntityMeta(emp);

        SqlFileSelectQuery query = new SqlFileSelectQuery();
        query.setConfig(config);
        query.setSqlFilePath(SqlFileUtil.buildPath("example.dao.EmpDao",
                "selectByNameAndSalary"));
        query.addParameter("emp", empMeta.getPropertyWrappers());
        query.setCallerClassName("example.dao.EmpDao");
        query.setCallerMethodName("selectByNameAndSalary");
        query.prepare();
        SelectCommand<List<Emp>> command = new SelectCommand<List<Emp>>(query,
                new EntityResultListHandler<Emp>(new Emp_()));
        return command.execute();
    }

    @Override
    public int insert(Emp entity) {
        AutoInsertQuery<Emp> query = new AutoInsertQuery<Emp>(new Emp_());
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
        AutoUpdateQuery<Emp> query = new AutoUpdateQuery<Emp>(new Emp_());
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
        AutoDeleteQuery<Emp> query = new AutoDeleteQuery<Emp>(new Emp_());
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
                new EntityIterationHandler<Integer, Emp>(new Emp_(), callback));
        return command.execute();
    }

    // @Override
    // public Integer add(Integer arg1, Integer arg2) {
    // AutoFunctionQuery<Integer> query = new AutoFunctionQuery<Integer>();
    // query.setConfig(config);
    // query.setFunctionName("add");
    // query
    // .setResultParameter((new DomainResultParameter<BuiltinIntegerDomain>(
    // BuiltinIntegerDomain.class)));
    // query.addParameter(new InParameter(arg1));
    // query.addParameter(new InParameter(arg2));
    // query.setCallerClassName("example.dao.EmpDao");
    // query.setCallerMethodName("add");
    // query.compile();
    // FunctionCommand<BuiltinIntegerDomain> command = new
    // FunctionCommand<BuiltinIntegerDomain>(
    // query);
    // return command.execute();
    // }
    //
    // @Override
    // public List<Emp> getEmps(BuiltinIntegerDomain arg1,
    // BuiltinIntegerDomain arg2) {
    // AutoFunctionQuery<List<Emp>> query = new AutoFunctionQuery<List<Emp>>();
    // query.setConfig(config);
    // query.setFunctionName("add");
    // query.setResultParameter((new EntityListResultParameter<Emp, Emp_>(
    // Emp_.class)));
    // query.addParameter(new InParameter(arg1));
    // query.addParameter(new InParameter(arg2));
    // query.setCallerClassName("example.dao.EmpDao");
    // query.setCallerMethodName("add");
    // query.compile();
    // FunctionCommand<List<Emp>> command = new FunctionCommand<List<Emp>>(
    // query);
    // return command.execute();
    // }
    //
    // @Override
    // public void exec(BuiltinIntegerDomain arg1, BuiltinIntegerDomain arg2) {
    // AutoProcedureQuery query = new AutoProcedureQuery();
    // query.setConfig(config);
    // query.setProcedureName("exec");
    // query.addParameter(new InParameter(arg1));
    // query.addParameter(new InParameter(arg2));
    // query.setCallerClassName("example.dao.EmpDao");
    // query.setCallerMethodName("add");
    // query.compile();
    // ProcedureCommand command = new ProcedureCommand(query);
    // command.execute();
    // }
    //
    // @Override
    // public BuiltinArrayDomain<String> createStringArrayDomain(String[]
    // element) {
    // ArrayCreateQuery<BuiltinArrayDomain<String>> query = new
    // ArrayCreateQuery<BuiltinArrayDomain<String>>();
    // query.setConfig(config);
    // query.setCallerClassName("example.dao.EmpDao");
    // query.setCallerMethodName("createStringArray");
    // query.setResult(new BuiltinArrayDomain<String>());
    // query.compile();
    // CreateCommand<BuiltinArrayDomain<String>> command = new
    // CreateCommand<BuiltinArrayDomain<String>>(
    // query);
    // return command.execute();
    // }
    //
    // @Override
    // public BuiltinBlobDomain createBlobDomain() {
    // BlobCreateQuery<BuiltinBlobDomain> query = new
    // BlobCreateQuery<BuiltinBlobDomain>();
    // query.setConfig(config);
    // query.setCallerClassName("example.dao.EmpDao");
    // query.setCallerMethodName("createStringArray");
    // query.setResult(new BuiltinBlobDomain());
    // query.compile();
    // CreateCommand<BuiltinBlobDomain> command = new
    // CreateCommand<BuiltinBlobDomain>(
    // query);
    // return command.execute();
    // }
}
