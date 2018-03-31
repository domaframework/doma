package org.seasar.doma.jdbc.builder;

import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.util.*;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlNode;

/** @author bakenezumi */
class BatchBuildingHelper {

  private static final String lineSeparator = System.getProperty("line.separator");

  private final LinkedList<Item> items = new LinkedList<>();
  private final Map<String, Integer> paramIndexMap = new HashMap<>();

  BatchBuildingHelper() {}

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

  void appendParam(BatchParam<?> param) {
    paramIndexMap.put(param.name, items.size());
    items.add(Item.param(param));
  }

  void modifyParam(BatchParam<?> param) {
    int index = paramIndexMap.get(param.name);
    items.set(index, Item.param(param));
  }

  void removeLast() {
    if (!items.isEmpty()) {
      items.removeLast();
    }
  }

  BatchParam<?> getParam(String paramName) {
    return items.get(paramIndexMap.get(paramName)).param;
  }

  Iterable<BatchParam<?>> getParams() {
    List<BatchParam<?>> results = new ArrayList<>();
    for (Item item : items) {
      if (item.kind == ItemKind.PARAM) {
        results.add(item.param);
      }
    }
    return results;
  }

  SqlNode getSqlNode() {
    StringBuilder buf = new StringBuilder(200);
    for (Item item : items) {
      switch (item.kind) {
        case SQL:
          buf.append(item.sql);
          break;
        case PARAM:
          buf.append("/*");
          if (item.param.literal) {
            buf.append("^");
          }
          buf.append(item.param.name);
          buf.append("*/0");
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

    private BatchParam<?> param;

    public static Item sql(String sql) {
      Item item = new Item();
      item.kind = ItemKind.SQL;
      item.sql = sql;
      return item;
    }

    public static Item param(BatchParam<?> param) {
      Item item = new Item();
      item.kind = ItemKind.PARAM;
      item.param = param;
      return item;
    }
  }

  private enum ItemKind {
    SQL,
    PARAM
  }
}
