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
package example.client;

import org.seasar.doma.domain.IntegerDomain;

import example.dao.EmpDao;
import example.dao.EmpDao_;

/**
 * @author taedium
 * 
 */
public class Client {

    public static void main(String[] args) {
        EmpDao dao = new EmpDao_();
        dao.selectById(new IntegerDomain(1), null);
    }
}
