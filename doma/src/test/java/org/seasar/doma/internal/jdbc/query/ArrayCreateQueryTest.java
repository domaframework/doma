package org.seasar.doma.internal.jdbc.query;

import junit.framework.TestCase;

import org.seasar.doma.domain.BuiltinArrayDomain;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

/**
 * @author taedium
 * 
 */
public class ArrayCreateQueryTest extends TestCase {

    private MockConfig config = new MockConfig();

    public void testCompile() throws Exception {
        ArrayCreateQuery<BuiltinArrayDomain<String>> query = new ArrayCreateQuery<BuiltinArrayDomain<String>>();
        query.setConfig(config);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setTypeName("varchar");
        query.setElements(new String[] {});
        query.setResult(new BuiltinArrayDomain<String>());
        query.compile();
    }
}
