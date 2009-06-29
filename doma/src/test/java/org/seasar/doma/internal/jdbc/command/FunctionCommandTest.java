package org.seasar.doma.internal.jdbc.command;

import java.util.List;

import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.internal.jdbc.command.FunctionCommand;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.internal.jdbc.sql.DomainResultParameter;
import org.seasar.doma.internal.jdbc.sql.InOutParameter;
import org.seasar.doma.internal.jdbc.sql.InParameter;
import org.seasar.doma.internal.jdbc.sql.OutParameter;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class FunctionCommandTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testExecute() throws Exception {
        List<Object> outParameters = runtimeConfig.dataSource.connection.callableStatement.outParameters;
        outParameters.add(10);
        outParameters.add(null);
        outParameters.add(20);
        outParameters.add(30);

        IntegerDomain aaa = new IntegerDomain(40);
        IntegerDomain bbb = new IntegerDomain(50);
        IntegerDomain ccc = new IntegerDomain(60);

        AutoFunctionQuery<IntegerDomain> query = new AutoFunctionQuery<IntegerDomain>();
        query.setConfig(runtimeConfig);
        query.setFunctionName("aaa");
        query.setResultParameter(new DomainResultParameter<IntegerDomain>(
                IntegerDomain.class));
        query.addParameter(new InParameter(aaa));
        query.addParameter(new OutParameter(bbb));
        query.addParameter(new InOutParameter(ccc));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();
        IntegerDomain result = new FunctionCommand<IntegerDomain>(query)
                .execute();
        assertNotNull(result);
        assertEquals(new Integer(10), result.get());
        assertEquals(new Integer(40), aaa.get());
        assertEquals(new Integer(20), bbb.get());
        assertEquals(new Integer(30), ccc.get());

        String sql = runtimeConfig.dataSource.connection.callableStatement.sql;
        assertEquals("{? = call aaa(?, ?, ?)}", sql);
    }
}
