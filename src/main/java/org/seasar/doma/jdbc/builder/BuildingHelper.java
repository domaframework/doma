package org.seasar.doma.jdbc.builder;

import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.SqlNode;

class BuildingHelper {

  private static final String lineSeparator = System.getProperty("line.separator");

  private final LinkedList<Item> items = new LinkedList<>();

  BuildingHelper() {}

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
    List<Param> results = new ArrayList<>();
    for (var item : items) {
      if (item.kind == ItemKind.PARAM) {
        results.add(item.param);
      }
    }
    return results;
  }

  SqlNode getSqlNode() {
    var buf = new StringBuilder(200);
    for (var item : items) {
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
    var parser = new SqlParser(buf.toString());
    return parser.parse();
  }

  private static class Item {

    private ItemKind kind;

    private String sql;

    private Param param;

    public static Item sql(String sql) {
      var item = new Item();
      item.kind = ItemKind.SQL;
      item.sql = sql;
      return item;
    }

    public static Item param(Param param) {
      var item = new Item();
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
