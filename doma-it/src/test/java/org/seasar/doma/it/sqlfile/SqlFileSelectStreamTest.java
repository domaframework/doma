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
package org.seasar.doma.it.sqlfile;

import static junit.framework.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.doma.it.dao.EmployeeDao;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class SqlFileSelectStreamTest {

    @Test
    public void testStreamAll() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Long count = dao.streamAll(stream -> stream
                .filter(e -> e.getEmployeeName() != null)
                .filter(e -> e.getEmployeeName().startsWith("S")).count());
        assertEquals(new Long(2), count);
    }

    @Test
    public void testStreamBySalary() throws Exception {
        EmployeeDao dao = EmployeeDao.get();
        Long count = dao.streamBySalary(new BigDecimal(2000),
                stream -> stream.count());
        assertEquals(new Long(6), count);
    }
}
