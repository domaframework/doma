package org.seasar.doma.internal.jdbc.query;

import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.internal.jdbc.query.FunctionQuery;
import org.seasar.doma.internal.jdbc.sql.DomainResultParameter;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class AutoFunctionQueryTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testCompile() throws Exception {
        AutoFunctionQuery<IntegerDomain> query = new AutoFunctionQuery<IntegerDomain>();
        query.setConfig(runtimeConfig);
        query.setFunctionName("aaa");
        query.setResultParameter(new DomainResultParameter<IntegerDomain>(
                IntegerDomain.class));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.compile();

        FunctionQuery<IntegerDomain> functionQuery = query;
        assertNotNull(functionQuery.getSql());
    }
}
