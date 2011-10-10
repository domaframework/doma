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
package org.seasar.doma.jdbc.builder;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlNode;

/**
 * @author taedium
 * 
 */
class BuildingHelper {

    private static final String lineSeparator = System
            .getProperty("line.separator");

    private final LinkedList<Item> items = new LinkedList<Item>();

    BuildingHelper() {
    }

    void appendSql(String sql) {
        items.add(Item.sql(sql));
    }

    void appendSqlWithLineSeparator(String sql) {
        if (items.isEmpty()) {
            items.add(Item.sql(sql));
        } else {
            items.add(Item.sql(lineSeparator + sql));
        }
    }

    void appendParam(Param param) {
        items.add(Item.param(param));
    }

    void removeLast() {
        if (!items.isEmpty()) {
            items.removeLast();
        }
    }

    List<Param> getParams() {
        List<Param> results = new ArrayList<Param>();
        for (Item item : items) {
            if (item.kind == ItemKind.PARAM) {
                results.add(item.param);
            }
        }
        return results;
    }

    SqlNode getSqlNode() {
        StringBuilder buf = new StringBuilder(200);
        @SuppressWarnings("unused")
        int index = 1;
        for (Item item : items) {
            switch (item.kind) {
            case SQL:
                buf.append(item.sql);
                break;
            case PARAM:
                buf.append("/*");
                buf.append(item.param.name);
                buf.append("*/0");
                index++;
                break;
            default:
                assertUnreachable();
                break;
            }
        }
        SqlParser parser = new SqlParser(buf.toString());
        return parser.parse();
    }

    private static class Item {

        private ItemKind kind;

        private String sql;

        private Param param;

        public static Item sql(String sql) {
            Item item = new Item();
            item.kind = ItemKind.SQL;
            item.sql = sql;
            return item;
        }

        public static Item param(Param param) {
            Item item = new Item();
            item.kind = ItemKind.PARAM;
            item.param = param;
            return item;
        }
    }

    private enum ItemKind {
        SQL, PARAM
    }

}
