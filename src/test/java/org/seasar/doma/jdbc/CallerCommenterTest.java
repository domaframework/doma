package org.seasar.doma.jdbc;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;

public class CallerCommenterTest extends TestCase {

    private CallerCommenter commenter = new CallerCommenter();

    public void testComment() throws Exception {
        CommentContext context = new CommentContext("class", "method", new MockConfig(), null);
        String actual = commenter.comment("select * from emp", context);
        assertEquals("/** class.method */" + System.lineSeparator() + "select * from emp", actual);
    }

}
