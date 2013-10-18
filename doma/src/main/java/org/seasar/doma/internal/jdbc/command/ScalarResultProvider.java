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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.query.Query;

/**
 * @author nakamura-to
 * 
 */
public class ScalarResultProvider<BASIC, CONTAINER> implements
        ResultProvider<CONTAINER> {

    protected final Supplier<Scalar<BASIC, CONTAINER>> supplier;

    protected final ScalarFetcher fetcher;

    /**
     * 
     * @param supplier
     * @param query
     */
    public ScalarResultProvider(Supplier<Scalar<BASIC, CONTAINER>> supplier,
            Query query) {
        assertNotNull(supplier, query);
        this.supplier = supplier;
        this.fetcher = new ScalarFetcher(query);
    }

    @Override
    public CONTAINER get(ResultSet resultSet) throws SQLException {
        Scalar<BASIC, CONTAINER> scalar = supplier.get();
        fetcher.fetch(resultSet, scalar);
        return scalar.get();
    }

    @Override
    public CONTAINER getDefault() {
        Scalar<BASIC, CONTAINER> scalar = supplier.get();
        return scalar.getDefault();
    }

}
