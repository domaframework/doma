package org.seasar.doma.jdbc.criteria.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.internal.jdbc.sql.SqlContext;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.entity.EntityPropertyType;

/**
 * Represents a user-defined expression. It implements the PropertyMetamodel interface.
 *
 * @param <PROPERTY> the type of the property
 */
public class UserDefinedExpression<PROPERTY> implements PropertyMetamodel<PROPERTY> {
  private final Class<PROPERTY> klass;
  private final EntityPropertyType<?, ?> type;
  private final Consumer<Declaration> block;

  public UserDefinedExpression(Class<PROPERTY> klass, Consumer<Declaration> block) {
    this.klass = Objects.requireNonNull(klass);
    this.type = getTypeByKlass();
    this.block = Objects.requireNonNull(block);
  }

  public UserDefinedExpression(
      Class<PROPERTY> klass, EntityPropertyType<?, ?> type, Consumer<Declaration> block) {
    this.klass = Objects.requireNonNull(klass);
    this.type = Objects.requireNonNull(type);
    this.block = Objects.requireNonNull(block);
  }

  /**
   * Returns the context of the user-defined expression. This is used to build a query.
   *
   * @param dialect the database dialect
   * @return the context of the user-defined expression
   */
  public UserDefinedExpressionContext getContext(Dialect dialect) {
    Declaration declaration = new Declaration(dialect);
    block.accept(declaration);
    return new UserDefinedExpressionContext(declaration.declarationItems, declaration.parameters);
  }

  @Override
  public Class<?> asClass() {
    return this.klass;
  }

  @Override
  public EntityPropertyType<?, ?> asType() {
    return type;
  }

  private EntityPropertyType<?, ?> getTypeByKlass() {
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
      throw new UnsupportedOperationException("Does not support for " + klass.getName() + " type.");
    }
  }

  @Override
  public String getName() {
    // un-used method
    return null;
  }

  @Override
  public void accept(PropertyMetamodel.Visitor visitor) {
    if (visitor instanceof Visitor) {
      Visitor v = (Visitor) visitor;
      v.visit(this);
    }
  }

  /** Result data of Declaration */
  public static class UserDefinedExpressionContext {
    public final List<DeclarationItem> declarationItems;
    public final List<Object> parameters;

    public UserDefinedExpressionContext(
        List<DeclarationItem> declarationItems, List<Object> parameters) {
      this.declarationItems = declarationItems;
      this.parameters = parameters;
    }
  }

  /** Declaration of UserDefinedExpression */
  public static class Declaration implements SqlContext {
    private final ArrayList<DeclarationItem> declarationItems = new ArrayList<>();
    private final ArrayList<Object> parameters = new ArrayList<>(1);

    /**
     * Represents the specific database dialect. This is utilized to modify user-defined expressions
     * based on the dialect in use.
     */
    public final Dialect dialect;

    public Declaration(Dialect dialect) {
      this.dialect = dialect;
    }

    /**
     * Appends a parameter to the declaration.
     *
     * @param parameter the parameter to be appended
     * @param <BASIC> the basic value type of the parameter
     */
    @Override
    public <BASIC> void appendParameter(InParameter<BASIC> parameter) {
      declarationItems.add(new DeclarationItem.Sql("?"));
      parameters.add(parameter);
    }

    /**
     * Appends SQL code to the declaration.
     *
     * @param sql the SQL code to be appended
     */
    @Override
    public void appendSql(String sql) {
      declarationItems.add(new DeclarationItem.Sql(sql));
    }

    /**
     * cutback SQL.
     *
     * @param length the length to cutback the SQL code
     */
    @Override
    public void cutBackSql(int length) {
      declarationItems.add(new DeclarationItem.CutbackSql(length));
    }

    /**
     * Append a column.
     *
     * @param propertyMetamodel the {@link PropertyMetamodel} to be added as a column in the
     *     declaration
     */
    public void visit(PropertyMetamodel<?> propertyMetamodel) {
      declarationItems.add(new DeclarationItem.Column(propertyMetamodel));
    }
  }

  public interface DeclarationItem {
    void accept(Visitor visitor);

    final class Sql implements DeclarationItem {
      private final String sql;

      public Sql(String sql) {
        this.sql = sql;
      }

      public String get() {
        return sql;
      }

      @Override
      public void accept(Visitor visitor) {
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
      public void accept(Visitor visitor) {
        visitor.visit(this);
      }
    }

    final class Column implements DeclarationItem {
      private final PropertyMetamodel<?> propertyMetamodel;

      public Column(PropertyMetamodel<?> propertyMetamodel) {
        this.propertyMetamodel = propertyMetamodel;
      }

      public PropertyMetamodel<?> get() {
        return propertyMetamodel;
      }

      @Override
      public void accept(Visitor visitor) {
        visitor.visit(this);
      }
    }

    interface Visitor {
      void visit(Sql sql);

      void visit(Column column);

      void visit(CutbackSql length);
    }
  }

  public interface Visitor {
    void visit(UserDefinedExpression<?> expression);
  }
}
