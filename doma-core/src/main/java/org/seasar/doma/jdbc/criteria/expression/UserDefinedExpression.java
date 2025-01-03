/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.criteria.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

/**
 * Represents a user-defined expression. It implements the PropertyMetamodel interface.
 *
 * @param <PROPERTY> the type of the property
 */
public class UserDefinedExpression<PROPERTY> implements PropertyMetamodel<PROPERTY> {
  private final String name;
  private final List<? extends PropertyMetamodel<?>> operands;
  private final Class<?> klass;
  private final EntityPropertyType<?, ?> type;
  private final Consumer<Declaration> block;

  UserDefinedExpression(
      Class<PROPERTY> klass,
      String name,
      List<? extends PropertyMetamodel<?>> operands,
      Consumer<Declaration> block) {
    this.klass = Objects.requireNonNull(klass);
    this.type = getTypeByKlass(this.klass);
    this.name = Objects.requireNonNull(name);
    this.operands = Objects.requireNonNull(operands);
    this.block = Objects.requireNonNull(block);
  }

  UserDefinedExpression(
      PropertyMetamodel<PROPERTY> resultPropertyMetamodel,
      String name,
      List<? extends PropertyMetamodel<?>> operands,
      Consumer<Declaration> block) {
    Objects.requireNonNull(resultPropertyMetamodel);
    this.klass = resultPropertyMetamodel.asClass();
    this.type = resultPropertyMetamodel.asType();
    this.name = Objects.requireNonNull(name);
    this.operands = Objects.requireNonNull(operands);
    this.block = Objects.requireNonNull(block);
  }

  /**
   * Returns the declarationItem the user-defined expression. This is used to build a query.
   *
   * @param dialect the database dialect
   * @return declarationItem of the user-defined expression
   */
  public List<DeclarationItem> getDeclarationItems(Dialect dialect) {
    Declaration declaration = new Declaration(dialect);
    block.accept(declaration);
    return declaration.declarationItems;
  }

  @Override
  public Class<?> asClass() {
    return this.klass;
  }

  @Override
  public EntityPropertyType<?, ?> asType() {
    return type;
  }

  private static EntityPropertyType<?, ?> getTypeByKlass(Class<?> klass) {
    if (klass == java.math.BigDecimal.class) {
      return new BigDecimalPropertyType(null);
    } else if (klass == java.math.BigInteger.class) {
      return new BigIntegerPropertyType(null);
    } else if (klass == java.lang.Boolean.class || klass == boolean.class) {
      return new BooleanPropertyType(null);
    } else if (klass == java.lang.Byte.class || klass == byte.class) {
      return new BytePropertyType(null);
    } else if (klass == java.lang.Double.class || klass == double.class) {
      return new DoublePropertyType(null);
    } else if (klass == java.lang.Float.class || klass == float.class) {
      return new FloatPropertyType(null);
    } else if (klass == java.lang.Integer.class || klass == int.class) {
      return new IntegerPropertyType(null);
    } else if (klass == java.time.LocalDate.class) {
      return new LocalDatePropertyType(null);
    } else if (klass == java.time.LocalDateTime.class) {
      return new LocalDateTimePropertyType(null);
    } else if (klass == java.time.LocalTime.class) {
      return new LocalTimePropertyType(null);
    } else if (klass == java.lang.Long.class || klass == long.class) {
      return new LongPropertyType(null);
    } else if (klass == java.lang.Short.class || klass == short.class) {
      return new ShortPropertyType(null);
    } else if (klass == java.lang.String.class) {
      return new StringPropertyType(null);
    } else {
      throw new UnsupportedOperationException(
          "Does not support for "
              + klass.getName()
              + " type. if "
              + klass.getName()
              + " is a domain class, please use another constructor of UserDefinedExpression.");
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void accept(PropertyMetamodel.Visitor visitor) {
    if (visitor instanceof UserDefinedExpression.Visitor) {
      UserDefinedExpression.Visitor v = (UserDefinedExpression.Visitor) visitor;
      v.visit(this);
    }
  }

  /** Declaration of UserDefinedExpression */
  public static class Declaration {
    private final ArrayList<DeclarationItem> declarationItems = new ArrayList<>();

    /**
     * Represents the specific database dialect. This is utilized to modify user-defined expressions
     * based on the dialect in use.
     */
    public final Dialect dialect;

    public Declaration(Dialect dialect) {
      Objects.requireNonNull(dialect);
      this.dialect = dialect;
    }

    /**
     * Appends SQL code to the declaration.
     *
     * @param sql the SQL code to be appended
     */
    public void appendSql(String sql) {
      Objects.requireNonNull(sql);
      declarationItems.add(new DeclarationItem.Sql(sql));
    }

    /**
     * cutback SQL.
     *
     * @param length the length to cutback the SQL code
     */
    public void cutBackSql(int length) {
      declarationItems.add(new DeclarationItem.CutbackSql(length));
    }

    /**
     * Append a expression.
     *
     * @param propertyMetamodel the {@link PropertyMetamodel} to be added as a expression in the
     *     declaration
     */
    public void appendExpression(PropertyMetamodel<?> propertyMetamodel) {
      Objects.requireNonNull(propertyMetamodel);
      declarationItems.add(new DeclarationItem.Expression(propertyMetamodel));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDefinedExpression<?> that = (UserDefinedExpression<?>) o;
    return Objects.equals(name, that.name)
        && Objects.equals(operands, that.operands)
        && Objects.equals(klass, that.klass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, operands, klass);
  }

  public interface DeclarationItem {
    void accept(UserDefinedExpression.DeclarationItem.Visitor visitor);

    final class Sql implements DeclarationItem {
      private final String sql;

      public Sql(String sql) {
        this.sql = sql;
      }

      public String get() {
        return sql;
      }

      @Override
      public void accept(UserDefinedExpression.DeclarationItem.Visitor visitor) {
        visitor.visit(this);
      }
    }

    final class CutbackSql implements DeclarationItem {
      private final int length;

      public CutbackSql(int length) {
        this.length = length;
      }

      public int get() {
        return length;
      }

      @Override
      public void accept(UserDefinedExpression.DeclarationItem.Visitor visitor) {
        visitor.visit(this);
      }
    }

    final class Expression implements DeclarationItem {
      private final PropertyMetamodel<?> propertyMetamodel;

      public Expression(PropertyMetamodel<?> propertyMetamodel) {
        this.propertyMetamodel = propertyMetamodel;
      }

      public PropertyMetamodel<?> get() {
        return propertyMetamodel;
      }

      @Override
      public void accept(UserDefinedExpression.DeclarationItem.Visitor visitor) {
        visitor.visit(this);
      }
    }

    interface Visitor {
      void visit(Sql sql);

      void visit(Expression column);

      void visit(CutbackSql length);
    }
  }

  public interface Visitor {
    void visit(UserDefinedExpression<?> expression);
  }
}
