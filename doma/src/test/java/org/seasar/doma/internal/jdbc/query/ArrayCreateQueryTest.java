package org.seasar.doma.internal.jdbc.query;

import junit.framework.TestCase;

import org.seasar.doma.domain.ArrayDomain;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

/**
 * @author taedium
 * 
 */
public class ArrayCreateQueryTest extends TestCase {

    private MockConfig config = new MockConfig();

    public void testCompile() throws Exception {
        ArrayCreateQuery<ArrayDomain<String>> query = new ArrayCreateQuery<ArrayDomain<String>>();
        query.setConfig(config);
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.setTypeName("varchar");
        query.setElements(new String[] {});
        query.setResult(new ArrayDomain<String>());
        query.compile();
    }
}
