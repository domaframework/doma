package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertUnreachable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.seasar.doma.internal.expr.EvaluationResult;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.ExpressionException;
import org.seasar.doma.internal.expr.ExpressionParser;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.internal.jdbc.scalar.ScalarException;
import org.seasar.doma.internal.jdbc.scalar.Scalars;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.CommentNode;
import org.seasar.doma.internal.jdbc.sql.node.DistinctNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNode;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.EndNode;
import org.seasar.doma.internal.jdbc.sql.node.EolNode;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.ForBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.ForNode;
import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.FragmentNode;
import org.seasar.doma.internal.jdbc.sql.node.FromClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.GroupByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.HavingClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.IfBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.IfNode;
import org.seasar.doma.internal.jdbc.sql.node.LiteralVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.LogicalOperatorNode;
import org.seasar.doma.internal.jdbc.sql.node.OptionClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.OrderByClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.OtherNode;
import org.seasar.doma.internal.jdbc.sql.node.ParensNode;
import org.seasar.doma.internal.jdbc.sql.node.PopulateNode;
import org.seasar.doma.internal.jdbc.sql.node.RemovableClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.SetClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.internal.jdbc.sql.node.UpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.UpdateStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.ValueNode;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.WhitespaceNode;
import org.seasar.doma.internal.jdbc.sql.node.WordNode;
import org.seasar.doma.internal.util.SqlTokenUtil;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.SqlNodeVisitor;
import org.seasar.doma.message.Message;
import org.seasar.doma.wrapper.WrapperVisitor;

public class NodePreparedSqlBuilder
    implements SqlNodeVisitor<Void, NodePreparedSqlBuilder.Context> {

  protected static final Pattern clauseKeywordPattern =
      Pattern.compile(
          "(select|from|where|group by|having|order by|for update)", Pattern.CASE_INSENSITIVE);

  protected final Config config;

  protected final SqlKind kind;

  protected final String sqlFilePath;

  protected final ExpressionEvaluator evaluator;

  protected final SqlLogType sqlLogType;

  protected final Function<ExpandNode, List<String>> columnsExpander;

  protected final BiConsumer<PopulateNode, SqlContext> valuesPopulater;

  public NodePreparedSqlBuilder(Config config, SqlKind kind, String sqlFilePath) {
    this(
        config,
        kind,
        sqlFilePath,
        new ExpressionEvaluator(
            config.getDialect().getExpressionFunctions(), config.getClassHelper()),
        SqlLogType.FORMATTED);
  }

  public NodePreparedSqlBuilder(
      Config config,
      SqlKind kind,
      String sqlFilePath,
      ExpressionEvaluator evaluator,
      SqlLogType sqlLogType) {
    this(
        config,
        kind,
        sqlFilePath,
        evaluator,
        sqlLogType,
        node -> {
          throw new UnsupportedOperationException("The '%expand' directive is not supported.");
        });
  }

  public NodePreparedSqlBuilder(
      Config config,
      SqlKind kind,
      String sqlFilePath,
      ExpressionEvaluator evaluator,
      SqlLogType sqlLogType,
      Function<ExpandNode, List<String>> columnsExpander) {
    this(
        config,
        kind,
        sqlFilePath,
        evaluator,
        sqlLogType,
        columnsExpander,
        (node, context) -> {
          throw new UnsupportedOperationException("The '%populate' directive is not supported.");
        });
  }

  public NodePreparedSqlBuilder(
      Config config,
      SqlKind kind,
      String sqlFilePath,
      ExpressionEvaluator evaluator,
      SqlLogType sqlLogType,
      Function<ExpandNode, List<String>> columnsExpander,
      BiConsumer<PopulateNode, SqlContext> valuesPopulater) {
    assertNotNull(config, kind, evaluator, columnsExpander, valuesPopulater);
    this.config = config;
    this.kind = kind;
    this.sqlFilePath = sqlFilePath;
    this.evaluator = evaluator;
    this.sqlLogType = sqlLogType;
    this.columnsExpander = columnsExpander;
    this.valuesPopulater = valuesPopulater;
  }

  public PreparedSql build(SqlNode sqlNode, Function<String, String> commenter) {
    assertNotNull(sqlNode, commenter);
    Context context = new Context(config, evaluator);
    sqlNode.accept(this, context);
    return new PreparedSql(
        kind,
        context.getSqlBuf(),
        context.getFormattedSqlBuf(),
        sqlFilePath,
        context.getParameters(),
        sqlLogType,
        commenter);
  }

  @Override
  public Void visitAnonymousNode(AnonymousNode node, Context p) {
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitOtherNode(OtherNode node, Context p) {
    p.setAvailable(true);
    String other = node.getOther();
    p.appendRawSql(other);
    p.appendFormattedSql(other);
    return null;
  }

  @Override
  public Void visitWhitespaceNode(WhitespaceNode node, Context p) {
    String whitespace = node.getWhitespace();
    p.appendRawSql(whitespace);
    p.appendFormattedSql(whitespace);
    return null;
  }

  @Override
  public Void visitCommentNode(CommentNode node, Context p) {
    String comment = node.getComment();
    p.appendRawSql(comment);
    p.appendFormattedSql(comment);
    return null;
  }

  @Override
  public Void visitBindVariableNode(BindVariableNode node, Context p) {
    return visitValueNode(node, p, p::addBindValue);
  }

  @Override
  public Void visitLiteralVariableNode(final LiteralVariableNode node, Context p) {
    Consumer<Scalar<?, ?>> validator =
        (scalar) -> {
          Object value = scalar.get();
          if (value == null) {
            return;
          }
          String text = value.toString();
          if (text.indexOf('\'') > -1) {
            SqlLocation location = node.getLocation();
            throw new JdbcException(
                Message.DOMA2224,
                location.getSql(),
                location.getLineNumber(),
                location.getPosition(),
                node.getVariableName());
          }
        };
    return visitValueNode(node, p, validator.andThen(p::addLiteralValue));
  }

  @SuppressWarnings("SameReturnValue")
  protected Void visitValueNode(ValueNode node, Context p, Consumer<Scalar<?, ?>> valueHandler) {
    SqlLocation location = node.getLocation();
    String name = node.getVariableName();
    EvaluationResult result = p.evaluate(location, name);
    Object value = result.getValue();
    Class<?> valueClass = result.getValueClass();
    p.setAvailable(true);
    if (node.isWordNodeIgnored()) {
      handleSingleValueNode(node, p, value, valueClass, valueHandler);
    } else if (node.isParensNodeIgnored()) {
      ParensNode parensNode = node.getParensNode();
      OtherNode openedFragmentNode = parensNode.getOpenedFragmentNode();
      openedFragmentNode.accept(this, p);
      if (Iterable.class.isAssignableFrom(valueClass)) {
        handleIterableValueNode(node, p, (Iterable<?>) value, valueClass, valueHandler);
      } else if (valueClass.isArray()) {
        handleIterableValueNode(node, p, Arrays.asList((Object[]) value), valueClass, valueHandler);
      } else {
        throw new JdbcException(
            Message.DOMA2112,
            location.getSql(),
            location.getLineNumber(),
            location.getPosition(),
            node.getVariableName(),
            valueClass);
      }
      OtherNode closedFragmentNode = parensNode.getClosedFragmentNode();
      closedFragmentNode.accept(this, p);
    } else {
      assertUnreachable();
    }
    return null;
  }

  @Override
  public Void visitEmbeddedVariableNode(EmbeddedVariableNode node, Context p) {
    SqlLocation location = node.getLocation();
    String name = node.getVariableName();
    EvaluationResult result = p.evaluate(location, name);
    Object value = result.getValue();
    if (value != null) {
      String fragment = value.toString();
      if (fragment.indexOf('\'') > -1) {
        throw new JdbcException(
            Message.DOMA2116,
            location.getSql(),
            location.getLineNumber(),
            location.getPosition(),
            node.getVariableName());
      }
      if (fragment.indexOf(';') > -1) {
        throw new JdbcException(
            Message.DOMA2117,
            location.getSql(),
            location.getLineNumber(),
            location.getPosition(),
            node.getVariableName());
      }
      if (fragment.contains("--")) {
        throw new JdbcException(
            Message.DOMA2122,
            location.getSql(),
            location.getLineNumber(),
            location.getPosition(),
            node.getVariableName());
      }
      if (fragment.contains("/*")) {
        throw new JdbcException(
            Message.DOMA2123,
            location.getSql(),
            location.getLineNumber(),
            location.getPosition(),
            node.getVariableName());
      }
      if (!startsWithClauseKeyword(fragment)) {
        p.setAvailable(true);
      }
      p.appendRawSql(fragment);
      p.appendFormattedSql(fragment);
    }
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  protected boolean startsWithClauseKeyword(String fragment) {
    Matcher matcher = clauseKeywordPattern.matcher(StringUtil.trimWhitespace(fragment));
    return matcher.lookingAt();
  }

  @SuppressWarnings("SameReturnValue")
  protected Void handleSingleValueNode(
      ValueNode node,
      Context p,
      Object value,
      Class<?> valueClass,
      Consumer<Scalar<?, ?>> consumer) {
    Supplier<Scalar<?, ?>> supplier =
        wrap(node.getLocation(), node.getVariableName(), value, valueClass);
    consumer.accept(supplier.get());
    return null;
  }

  protected void handleIterableValueNode(
      ValueNode node,
      Context p,
      Iterable<?> values,
      Class<?> valueClass,
      Consumer<Scalar<?, ?>> consumer) {
    int index = 0;
    for (Object v : values) {
      if (v == null) {
        SqlLocation location = node.getLocation();
        throw new JdbcException(
            Message.DOMA2115,
            location.getSql(),
            location.getLineNumber(),
            location.getPosition(),
            node.getVariableName(),
            index);
      }
      Supplier<Scalar<?, ?>> supplier =
          wrap(node.getLocation(), node.getVariableName(), v, v.getClass());
      consumer.accept(supplier.get());
      p.appendRawSql(", ");
      p.appendFormattedSql(", ");
      index++;
    }
    if (index == 0) {
      p.appendRawSql("null");
      p.appendFormattedSql("null");
    } else {
      p.cutBackSqlBuf(2);
      p.cutBackFormattedSqlBuf(2);
    }
  }

  @Override
  public Void visitIfBlockNode(IfBlockNode node, Context p) {
    if (!handleIfNode(node, p)) {
      if (!handleElseifNode(node, p)) {
        handleElseNode(node, p);
      }
    }
    EndNode endNode = node.getEndNode();
    endNode.accept(this, p);
    return null;
  }

  protected boolean handleIfNode(IfBlockNode node, Context p) {
    IfNode ifNode = node.getIfNode();
    SqlLocation location = ifNode.getLocation();
    String expression = ifNode.getExpression();
    EvaluationResult ifResult = p.evaluate(location, expression);
    if (ifResult.getBooleanValue()) {
      ifNode.accept(this, p);
      return true;
    }
    return false;
  }

  protected boolean handleElseifNode(IfBlockNode node, Context p) {
    for (ElseifNode elseifNode : node.getElseifNodes()) {
      SqlLocation location = elseifNode.getLocation();
      String expression = elseifNode.getExpression();
      EvaluationResult elseifResult = p.evaluate(location, expression);
      if (elseifResult.getBooleanValue()) {
        elseifNode.accept(this, p);
        return true;
      }
    }
    return false;
  }

  protected void handleElseNode(IfBlockNode node, Context p) {
    ElseNode elseNode = node.getElseNode();
    if (elseNode != null) {
      elseNode.accept(this, p);
    }
  }

  @Override
  public Void visitIfNode(IfNode node, Context p) {
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitElseifNode(ElseifNode node, Context p) {
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitElseNode(ElseNode node, Context p) {
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitEndNode(EndNode node, Context p) {
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitForBlockNode(ForBlockNode node, Context p) {
    ForNode forNode = node.getForNode();
    SqlLocation location = forNode.getLocation();
    EvaluationResult expressionResult = p.evaluate(location, forNode.getExpression());
    Object expressionValue = expressionResult.getValue();
    Class<?> expressionValueClass = expressionResult.getValueClass();
    Iterable<?> iterable;
    if (Iterable.class.isAssignableFrom(expressionValueClass)) {
      iterable = (Iterable<?>) expressionValue;
    } else if (expressionValueClass.isArray()) {
      iterable = Arrays.asList((Object[]) expressionValue);
    } else {
      throw new JdbcException(
          Message.DOMA2129,
          location.getSql(),
          location.getLineNumber(),
          location.getPosition(),
          forNode.getExpression(),
          expressionValueClass);
    }

    String identifier = forNode.getIdentifier();
    Value originalIdentifierValue = p.removeValue(identifier);
    String hasNextVariable = identifier + ForBlockNode.HAS_NEXT_SUFFIX;
    Value originalHasNextValue = p.removeValue(hasNextVariable);
    String indexVariable = identifier + ForBlockNode.INDEX_SUFFIX;
    Value originalIndexValue = p.removeValue(indexVariable);
    int index = 0;
    for (Iterator<?> it = iterable.iterator(); it.hasNext(); ) {
      Object each = it.next();
      Value value = each == null ? new Value(void.class, null) : new Value(each.getClass(), each);
      p.putValue(identifier, value);
      p.putValue(hasNextVariable, new Value(boolean.class, it.hasNext()));
      p.putValue(indexVariable, new Value(int.class, index));
      for (SqlNode child : forNode.getChildren()) {
        child.accept(this, p);
      }
      index++;
    }
    if (originalIdentifierValue == null) {
      p.removeValue(identifier);
    } else {
      p.putValue(identifier, originalIdentifierValue);
    }
    if (originalHasNextValue == null) {
      p.removeValue(hasNextVariable);
    } else {
      p.putValue(hasNextVariable, originalHasNextValue);
    }
    if (originalIndexValue == null) {
      p.removeValue(indexVariable);
    } else {
      p.putValue(indexVariable, originalIndexValue);
    }

    EndNode endNode = node.getEndNode();
    endNode.accept(this, p);
    return null;
  }

  @Override
  public Void visitForNode(ForNode node, Context p) {
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitSelectStatementNode(SelectStatementNode node, Context p) {
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitSelectClauseNode(SelectClauseNode node, Context p) {
    WordNode wordNode = node.getWordNode();
    wordNode.accept(this, p);
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitFromClauseNode(FromClauseNode node, Context p) {
    WordNode wordNode = node.getWordNode();
    wordNode.accept(this, p);
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitWhereClauseNode(WhereClauseNode node, Context p) {
    handleRemovableClauseNode(node, p);
    return null;
  }

  @Override
  public Void visitGroupByClauseNode(GroupByClauseNode node, Context p) {
    handleRemovableClauseNode(node, p);
    return null;
  }

  @Override
  public Void visitHavingClauseNode(HavingClauseNode node, Context p) {
    handleRemovableClauseNode(node, p);
    return null;
  }

  @Override
  public Void visitOptionClauseNode(OptionClauseNode node, Context p) {
    WordNode wordNode = node.getWordNode();
    wordNode.accept(this, p);
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitOrderByClauseNode(OrderByClauseNode node, Context p) {
    handleRemovableClauseNode(node, p);
    return null;
  }

  @Override
  public Void visitForUpdateClauseNode(ForUpdateClauseNode node, Context p) {
    WordNode wordNode = node.getWordNode();
    wordNode.accept(this, p);
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  protected void handleRemovableClauseNode(RemovableClauseNode node, Context p) {
    Context context = new Context(p);
    for (SqlNode child : node.getChildren()) {
      child.accept(this, context);
    }
    if (context.isAvailable()) {
      node.getWordNode().accept(this, p);
      p.setAvailable(true);
      p.appendRawSql(context.getSqlBuf());
      p.appendFormattedSql(context.getFormattedSqlBuf());
      p.addAllParameters(context.getParameters());
    } else {
      String fragment = context.getSqlBuf().toString();
      if (startsWithClauseKeyword(fragment)) {
        p.setAvailable(true);
        p.appendRawSql(context.getSqlBuf());
        p.appendFormattedSql(context.getFormattedSqlBuf());
        p.addAllParameters(context.getParameters());
      }
    }
  }

  @Override
  public Void visitLogicalOperatorNode(LogicalOperatorNode node, Context p) {
    if (p.isAvailable()) {
      WordNode wordNode = node.getWordNode();
      wordNode.accept(this, p);
    }
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitUpdateStatementNode(UpdateStatementNode node, Context p) {
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitUpdateClauseNode(UpdateClauseNode node, Context p) {
    WordNode wordNode = node.getWordNode();
    wordNode.accept(this, p);
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitSetClauseNode(SetClauseNode node, Context p) {
    WordNode wordNode = node.getWordNode();
    wordNode.accept(this, p);
    for (SqlNode child : node.getChildren()) {
      child.accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitPopulateNode(PopulateNode node, Context p) {
    valuesPopulater.accept(
        node,
        new SqlContext() {

          @Override
          public void cutBackSql(int length) {
            p.cutBackSqlBuf(length);
            p.cutBackFormattedSqlBuf(length);
          }

          @Override
          public void appendSql(String sql) {
            p.appendRawSql(sql);
            p.appendFormattedSql(sql);
          }

          @Override
          public <BASIC> void appendParameter(InParameter<BASIC> parameter) {
            p.appendParameter(parameter);
          }
        });
    return null;
  }

  @Override
  public Void visitWordNode(WordNode node, Context p) {
    p.setAvailable(true);
    String word = node.getWord();
    if (node.isReserved()) {
      p.appendWhitespaceIfNecessary();
    }
    p.appendRawSql(word);
    p.appendFormattedSql(word);
    return null;
  }

  @Override
  public Void visitDistinctNode(DistinctNode node, Context context) {
    WordNode wordNode = node.getWordNode();
    return wordNode.accept(this, context);
  }

  @Override
  public Void visitFragmentNode(FragmentNode node, Context p) {
    p.setAvailable(true);
    String fragment = node.getFragment();
    p.appendRawSql(fragment);
    p.appendFormattedSql(fragment);
    return null;
  }

  @Override
  public Void visitParensNode(ParensNode node, Context p) {
    if (node.isAttachedWithValue()) {
      return null;
    }
    Context context = new Context(p);
    if (node.isEmpty()) {
      context.setAvailable(true);
    }
    for (SqlNode child : node.getChildren()) {
      child.accept(this, context);
    }
    if (context.isAvailable()) {
      node.getOpenedFragmentNode().accept(this, p);
      p.setAvailable(true);
      p.appendRawSql(context.getSqlBuf());
      p.appendFormattedSql(context.getFormattedSqlBuf());
      p.addAllParameters(context.getParameters());
      node.getClosedFragmentNode().accept(this, p);
    }
    return null;
  }

  @Override
  public Void visitEolNode(EolNode node, Context p) {
    String eol = node.getEol();
    p.appendRawSql(eol);
    p.appendFormattedSql(eol);
    return null;
  }

  @Override
  public Void visitExpandNode(ExpandNode node, Context p) {
    EvaluationResult evalResult = p.evaluate(node.getLocation(), node.getAlias());
    String alias = evalResult.getValue().toString();
    String prefix = alias.isEmpty() ? "" : alias + ".";
    StringJoiner joiner = new StringJoiner(", ");
    for (String column : columnsExpander.apply(node)) {
      joiner.add(prefix + column);
    }
    String joined = joiner.toString();
    p.appendRawSql(joined);
    p.appendFormattedSql(joined);
    return null;
  }

  protected Supplier<Scalar<?, ?>> wrap(
      SqlLocation location, String bindVariableText, Object value, Class<?> valueClass) {
    try {
      return Scalars.wrap(value, valueClass, false, config.getClassHelper());
    } catch (ScalarException e) {
      throw new JdbcException(
          Message.DOMA2118,
          e,
          location.getSql(),
          location.getLineNumber(),
          location.getPosition(),
          bindVariableText,
          e);
    }
  }

  protected static class Context {

    private static final char WHITESPACE = ' ';

    private final Config config;

    private final ExpressionEvaluator evaluator;

    private final SqlLogFormattingFunction formattingFunction = new ConvertToLogFormatFunction();

    private final StringBuilder rawSqlBuf = new StringBuilder(200);

    private final StringBuilder formattedSqlBuf = new StringBuilder(200);

    private final List<InParameter<?>> parameters = new ArrayList<>();

    private boolean available;

    @SuppressWarnings("CopyConstructorMissesField")
    protected Context(Context context) {
      this(context.config, context.evaluator);
    }

    protected Context(Config config, ExpressionEvaluator evaluator) {
      this.config = config;
      this.evaluator = evaluator;
    }

    protected void appendWhitespaceIfNecessary() {
      if (endsWithWordPart()) {
        rawSqlBuf.append(WHITESPACE);
        formattedSqlBuf.append(WHITESPACE);
      }
    }

    protected boolean endsWithWordPart() {
      if (rawSqlBuf.length() == 0) {
        return false;
      }
      char c = rawSqlBuf.charAt(rawSqlBuf.length() - 1);
      return SqlTokenUtil.isWordPart(c);
    }

    protected void appendRawSql(CharSequence sql) {
      rawSqlBuf.append(sql);
    }

    protected void appendFormattedSql(CharSequence sql) {
      formattedSqlBuf.append(sql);
    }

    protected void cutBackSqlBuf(int size) {
      rawSqlBuf.setLength(rawSqlBuf.length() - size);
    }

    protected void cutBackFormattedSqlBuf(int size) {
      formattedSqlBuf.setLength(formattedSqlBuf.length() - size);
    }

    protected CharSequence getSqlBuf() {
      return rawSqlBuf;
    }

    protected CharSequence getFormattedSqlBuf() {
      return formattedSqlBuf;
    }

    protected <BASIC, CONTAINER> void addLiteralValue(Scalar<BASIC, CONTAINER> scalar) {
      String literal =
          scalar
              .getWrapper()
              .accept(config.getDialect().getSqlLogFormattingVisitor(), formattingFunction, null);
      rawSqlBuf.append(literal);
      formattedSqlBuf.append(literal);
    }

    protected <BASIC, CONTAINER> void addBindValue(Scalar<BASIC, CONTAINER> scalar) {
      appendParameterInternal(new ScalarInParameter<>(scalar));
    }

    protected <BASIC> void appendParameter(InParameter<BASIC> parameter) {
      appendParameterInternal(parameter);
    }

    protected <BASIC> void appendParameterInternal(InParameter<BASIC> parameter) {
      parameters.add(parameter);
      rawSqlBuf.append("?");
      String formatted =
          parameter
              .getWrapper()
              .accept(config.getDialect().getSqlLogFormattingVisitor(), formattingFunction, null);
      formattedSqlBuf.append(formatted);
    }

    protected void addAllParameters(List<InParameter<?>> values) {
      parameters.addAll(values);
    }

    protected List<InParameter<?>> getParameters() {
      return parameters;
    }

    void setAvailable(@SuppressWarnings("SameParameterValue") boolean available) {
      this.available = available;
    }

    boolean isAvailable() {
      return available;
    }

    public void putValue(String variableName, Value value) {
      evaluator.putValue(variableName, value);
    }

    public Value removeValue(String variableName) {
      return evaluator.removeValue(variableName);
    }

    protected EvaluationResult evaluate(SqlLocation location, String expression) {
      try {
        ExpressionParser parser = new ExpressionParser(expression);
        ExpressionNode expressionNode = parser.parse();
        return evaluator.evaluate(expressionNode);
      } catch (ExpressionException e) {
        throw new JdbcException(
            Message.DOMA2111,
            e,
            location.getSql(),
            location.getLineNumber(),
            location.getPosition(),
            e);
      }
    }

    @Override
    public String toString() {
      return rawSqlBuf.toString();
    }
  }

  protected static class LiteralValueVisitor
      implements WrapperVisitor<String, Void, Void, RuntimeException> {}
}
