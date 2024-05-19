package org.seasar.doma.it.criteria;

import static org.seasar.doma.jdbc.criteria.expression.Expressions.userDefined;

import org.seasar.doma.jdbc.criteria.expression.UserDefinedExpression;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;

public class CustomExpressions {
  static UserDefinedExpression<Salary> addSalaryUserDefined(Employee_ e) {
    return userDefined(
        e.salary,
        "addSalaryUserDefined",
        e.salary,
        c -> {
          c.appendSql("(");
          c.appendExpression(e.salary);
          c.appendSql(" + 100)");
        });
  }

  static UserDefinedExpression<String> concatWithUserDefined(
      PropertyMetamodel<?>... propertyMetamodels) {
    return userDefined(
        String.class,
        "concatWithUserDefined",
        propertyMetamodels,
        c -> {
          if (c.dialect.getName().equals("mysql")) {
            c.appendSql("concat(");
            for (PropertyMetamodel<?> propertyMetamodel : propertyMetamodels) {
              c.appendExpression(propertyMetamodel);
              c.appendSql(", '-' , ");
            }
            c.cutBackSql(8);
            c.appendSql(")");
          } else if (c.dialect.getName().equals("mssql")) {
            c.appendSql("(");
            for (PropertyMetamodel<?> propertyMetamodel : propertyMetamodels) {
              c.appendExpression(propertyMetamodel);
              c.appendSql(" + '-' + ");
            }
            c.cutBackSql(9);
            c.appendSql(")");
          } else {
            c.appendSql("(");
            for (PropertyMetamodel<?> propertyMetamodel : propertyMetamodels) {
              c.appendExpression(propertyMetamodel);
              c.appendSql(" || '-' || ");
            }
            c.cutBackSql(11);
            c.appendSql(")");
          }
        });
  }

  static UserDefinedExpression<String> tpStringWithUserDefined(
      PropertyMetamodel<?> propertyMetamodel) {
    return userDefined(
        String.class,
        "tpStringWithUserDefined",
        propertyMetamodel,
        c -> {
          if (c.dialect.getName().equals("mysql")) {
            c.appendSql("CAST(");
            c.appendExpression(propertyMetamodel);
            c.appendSql(" AS CHAR)");
          } else if (c.dialect.getName().equals("postgres")) {
            c.appendSql("CAST(");
            c.appendExpression(propertyMetamodel);
            c.appendSql(" AS TEXT)");
          } else if (c.dialect.getName().equals("mssql") || c.dialect.getName().equals("h2")) {
            c.appendSql("CAST(");
            c.appendExpression(propertyMetamodel);
            c.appendSql(" AS VARCHAR)");
          } else if (c.dialect.getName().equals("oracle")) {
            c.appendSql("TO_CHAR(");
            c.appendExpression(propertyMetamodel);
            c.appendSql(")");
          }
        });
  }
}
