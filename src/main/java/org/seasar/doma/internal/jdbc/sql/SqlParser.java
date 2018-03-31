package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Pattern;
import org.seasar.doma.internal.jdbc.sql.node.AnonymousNode;
import org.seasar.doma.internal.jdbc.sql.node.AppendableSqlNode;
import org.seasar.doma.internal.jdbc.sql.node.BindVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.BlockNode;
import org.seasar.doma.internal.jdbc.sql.node.CommentNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseNode;
import org.seasar.doma.internal.jdbc.sql.node.ElseifNode;
import org.seasar.doma.internal.jdbc.sql.node.EmbeddedVariableNode;
import org.seasar.doma.internal.jdbc.sql.node.EndNode;
import org.seasar.doma.internal.jdbc.sql.node.EolNode;
import org.seasar.doma.internal.jdbc.sql.node.ExpandNode;
import org.seasar.doma.internal.jdbc.sql.node.ForBlockNode;
import org.seasar.doma.internal.jdbc.sql.node.ForNode;
import org.seasar.doma.internal.jdbc.sql.node.ForUpdateClauseNode;
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
import org.seasar.doma.internal.jdbc.sql.node.SelectClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SelectStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.SetClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.SqlLocation;
import org.seasar.doma.internal.jdbc.sql.node.UpdateClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.UpdateStatementNode;
import org.seasar.doma.internal.jdbc.sql.node.ValueNode;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseAwareNode;
import org.seasar.doma.internal.jdbc.sql.node.WhereClauseNode;
import org.seasar.doma.internal.jdbc.sql.node.WhitespaceNode;
import org.seasar.doma.internal.jdbc.sql.node.WordNode;
import org.seasar.doma.internal.util.StringUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.message.Message;

public class SqlParser {

  protected static final Pattern LITERAL_PATTERN =
      Pattern.compile("[-+'.0-9]|.*'|true|false|null", Pattern.CASE_INSENSITIVE);

  protected final Deque<AppendableSqlNode> nodeStack = new LinkedList<>();

  protected final String sql;

  protected final SqlTokenizer tokenizer;

  protected final AnonymousNode rootNode;

  protected SqlTokenType tokenType;

  protected String token;

  public SqlParser(String sql) {
    assertNotNull(sql);
    this.sql = sql;
    tokenizer = new SqlTokenizer(sql);
    rootNode = new AnonymousNode();
    nodeStack.push(rootNode);
  }

  public SqlNode parse() {
    outer:
    for (; ; ) {
      tokenType = tokenizer.next();
      token = tokenizer.getToken();
      switch (tokenType) {
        case WHITESPACE:
          {
            parseWhitespace();
            break;
          }
        case WORD:
        case QUOTE:
          {
            parseWord();
            break;
          }
        case SELECT_WORD:
          {
            parseSelectWord();
            break;
          }
        case FROM_WORD:
          {
            parseFromWord();
            break;
          }
        case WHERE_WORD:
          {
            parseWhereWord();
            break;
          }
        case GROUP_BY_WORD:
          {
            parseGroupByWord();
            break;
          }
        case HAVING_WORD:
          {
            parseHavingWord();
            break;
          }
        case ORDER_BY_WORD:
          {
            parseOrderByWord();
            break;
          }
        case FOR_UPDATE_WORD:
          {
            parseForUpdateWord();
            break;
          }
        case OPTION_WORD:
          {
            parseOptionWord();
            break;
          }
        case UPDATE_WORD:
          {
            parseUpdateWord();
            break;
          }
        case SET_WORD:
          {
            parseSetWord();
            break;
          }
        case AND_WORD:
        case OR_WORD:
          {
            parseLogicalWord();
            break;
          }
        case OPENED_PARENS:
          {
            parseOpenedParens();
            break;
          }
        case CLOSED_PARENS:
          {
            parseClosedParens();
            break;
          }
        case BIND_VARIABLE_BLOCK_COMMENT:
          {
            parseBindVariableBlockComment();
            break;
          }
        case LITERAL_VARIABLE_BLOCK_COMMENT:
          {
            parseLiteralVariableBlockComment();
            break;
          }
        case EMBEDDED_VARIABLE_BLOCK_COMMENT:
          {
            parseEmbeddedVariableBlockComment();
            break;
          }
        case IF_BLOCK_COMMENT:
          {
            parseIfBlockComment();
            break;
          }
        case ELSEIF_BLOCK_COMMENT:
          {
            parseElseifBlockComment();
            break;
          }
        case ELSE_BLOCK_COMMENT:
          {
            parseElseBlockComment();
            break;
          }
        case END_BLOCK_COMMENT:
          {
            parseEndBlockComment();
            break;
          }
        case FOR_BLOCK_COMMENT:
          {
            parseForBlockComment();
            break;
          }
        case EXPAND_BLOCK_COMMENT:
          {
            parseExpandBlockComment();
            break;
          }
        case POPULATE_BLOCK_COMMENT:
          {
            parsePopulateBlockComment();
            break;
          }
        case UNION_WORD:
        case EXCEPT_WORD:
        case MINUS_WORD:
        case INTERSECT_WORD:
          {
            parseSetOperatorWord();
            break;
          }
        case BLOCK_COMMENT:
        case LINE_COMMENT:
          {
            parseComment();
            break;
          }
        case OTHER:
          {
            parseOther();
            break;
          }
        case EOL:
          {
            parseEOL();
            break;
          }
        case DELIMITER:
        case EOF:
          {
            break outer;
          }
      }
    }
    validate();
    validateParensClosed();
    return rootNode;
  }

  protected void parseSetOperatorWord() {
    validate();
    var node = new AnonymousNode();
    node.appendNode(new WordNode(token, true));
    if (isInSelectStatementNode()) {
      removeNodesTo(SelectStatementNode.class);
      pop();
    }
    appendNode(node);
    push(node);
  }

  protected void parseSelectWord() {
    validate();
    var selectStatementNode = new SelectStatementNode();
    appendNode(selectStatementNode);
    push(selectStatementNode);
    var selectClauseNode = new SelectClauseNode(token);
    selectStatementNode.setSelectClauseNode(selectClauseNode);
    push(selectClauseNode);
  }

  protected void parseFromWord() {
    validate();
    var node = new FromClauseNode(token);
    if (isInSelectStatementNode()) {
      removeNodesTo(SelectStatementNode.class);
      SelectStatementNode selectStatementNode = peek();
      selectStatementNode.setFromClauseNode(node);
    } else {
      appendNode(node);
    }
    push(node);
  }

  protected void parseWhereWord() {
    validate();
    var node = new WhereClauseNode(token);
    if (isInWhereClauseAwareNode()) {
      removeNodesTo(WhereClauseAwareNode.class);
      WhereClauseAwareNode whereClauseAwareNode = peek();
      whereClauseAwareNode.setWhereClauseNode(node);
    } else {
      appendNode(node);
    }
    push(node);
  }

  protected void parseGroupByWord() {
    validate();
    var node = new GroupByClauseNode(token);
    if (isInSelectStatementNode()) {
      removeNodesTo(SelectStatementNode.class);
      SelectStatementNode selectStatementNode = peek();
      selectStatementNode.setGroupByClauseNode(node);
    } else {
      appendNode(node);
    }
    push(node);
  }

  protected void parseHavingWord() {
    validate();
    var node = new HavingClauseNode(token);
    if (isInSelectStatementNode()) {
      removeNodesTo(SelectStatementNode.class);
      SelectStatementNode selectStatementNode = peek();
      selectStatementNode.setHavingClauseNode(node);
    } else {
      appendNode(node);
    }
    push(node);
  }

  protected void parseOrderByWord() {
    validate();
    var node = new OrderByClauseNode(token);
    if (isInSelectStatementNode()) {
      removeNodesTo(SelectStatementNode.class);
      SelectStatementNode selectStatementNode = peek();
      selectStatementNode.setOrderByClauseNode(node);
    } else {
      appendNode(node);
    }
    push(node);
  }

  protected void parseForUpdateWord() {
    validate();
    var node = new ForUpdateClauseNode(token);
    if (isInSelectStatementNode()) {
      removeNodesTo(SelectStatementNode.class);
      SelectStatementNode selectStatementNode = peek();
      selectStatementNode.setForUpdateClauseNode(node);
    } else {
      appendNode(node);
    }
    push(node);
  }

  protected void parseOptionWord() {
    validate();
    var node = new OptionClauseNode(token);
    if (isInSelectStatementNode()) {
      removeNodesTo(SelectStatementNode.class);
      SelectStatementNode selectStatementNode = peek();
      selectStatementNode.setOptionClauseNode(node);
    } else {
      appendNode(node);
    }
    push(node);
  }

  protected void parseUpdateWord() {
    validate();
    var updateStatementNode = new UpdateStatementNode();
    appendNode(updateStatementNode);
    push(updateStatementNode);
    var updateClauseNode = new UpdateClauseNode(token);
    updateStatementNode.setUpdateClauseNode(updateClauseNode);
    push(updateClauseNode);
  }

  protected void parseSetWord() {
    validate();
    var node = new SetClauseNode(token);
    if (isInUpdateStatementNode()) {
      removeNodesTo(UpdateStatementNode.class);
      UpdateStatementNode updateStatementNode = peek();
      updateStatementNode.setSetClauseNode(node);
    } else {
      appendNode(node);
    }
    push(node);
  }

  protected void parseLogicalWord() {
    var word = tokenType.extract(token);
    var node = new LogicalOperatorNode(word);
    appendNode(node);
    push(node);
  }

  protected void parseWord() {
    var node = new WordNode(token);
    appendNode(node);
  }

  protected void parseComment() {
    var node = new CommentNode(token);
    appendNode(node);
  }

  protected void parseOpenedParens() {
    var parensNode = new ParensNode(getLocation());
    appendNode(parensNode);
    push(parensNode);
  }

  protected void parseClosedParens() {
    if (!isInParensNode()) {
      throw new JdbcException(
          Message.DOMA2109, sql, tokenizer.getLineNumber(), tokenizer.getPosition());
    }
    validate();
    removeNodesTo(ParensNode.class);
    ParensNode parensNode = pop();
    for (var child : parensNode.getChildren()) {
      if (!(child instanceof WhitespaceNode) && !(child instanceof CommentNode)) {
        parensNode.setEmpty(false);
        break;
      }
    }
    parensNode.close();
  }

  protected void parseBindVariableBlockComment() {
    var variableName = tokenType.extract(token);
    if (variableName.isEmpty()) {
      throw new JdbcException(
          Message.DOMA2120, sql, tokenizer.getLineNumber(), tokenizer.getPosition(), token);
    }
    var node = new BindVariableNode(getLocation(), variableName, token);
    appendNode(node);
    push(node);
  }

  protected void parseLiteralVariableBlockComment() {
    var variableName = tokenType.extract(token);
    if (variableName.isEmpty()) {
      throw new JdbcException(
          Message.DOMA2228, sql, tokenizer.getLineNumber(), tokenizer.getPosition(), token);
    }
    var node = new LiteralVariableNode(getLocation(), variableName, token);
    appendNode(node);
    push(node);
  }

  protected void parseEmbeddedVariableBlockComment() {
    var variableName = tokenType.extract(token);
    if (variableName.isEmpty()) {
      throw new JdbcException(
          Message.DOMA2121, sql, tokenizer.getLineNumber(), tokenizer.getPosition(), token);
    }
    var node = new EmbeddedVariableNode(getLocation(), variableName, token);
    appendNode(node);
    push(node);
  }

  protected void parseIfBlockComment() {
    var ifBlockNode = new IfBlockNode();
    appendNode(ifBlockNode);
    push(ifBlockNode);
    var expression = tokenType.extract(token);
    var ifNode = new IfNode(getLocation(), expression, token);
    ifBlockNode.setIfNode(ifNode);
    push(ifNode);
  }

  protected void parseElseifBlockComment() {
    if (!isInIfBlockNode()) {
      throw new JdbcException(
          Message.DOMA2138, sql, tokenizer.getLineNumber(), tokenizer.getPosition());
    }
    removeNodesTo(IfBlockNode.class);
    IfBlockNode ifBlockNode = peek();
    if (ifBlockNode.isElseNodeExistent()) {
      throw new JdbcException(
          Message.DOMA2139, sql, tokenizer.getLineNumber(), tokenizer.getPosition());
    }
    var expression = tokenType.extract(token);
    var node = new ElseifNode(getLocation(), expression, token);
    ifBlockNode.addElseifNode(node);
    push(node);
  }

  protected void parseElseBlockComment() {
    if (!isInIfBlockNode()) {
      throw new JdbcException(
          Message.DOMA2140, sql, tokenizer.getLineNumber(), tokenizer.getPosition());
    }
    removeNodesTo(IfBlockNode.class);
    IfBlockNode ifBlockNode = peek();
    if (ifBlockNode.isElseNodeExistent()) {
      throw new JdbcException(
          Message.DOMA2141, sql, tokenizer.getLineNumber(), tokenizer.getPosition());
    }
    var node = new ElseNode(token);
    ifBlockNode.setElseNode(node);
    push(node);
  }

  protected void parseEndBlockComment() {
    if (!isInBlockNode()) {
      throw new JdbcException(
          Message.DOMA2104, sql, tokenizer.getLineNumber(), tokenizer.getPosition());
    }
    removeNodesTo(BlockNode.class);
    BlockNode blockNode = pop();
    var node = new EndNode(token);
    blockNode.setEndNode(node);
    push(node);
  }

  protected void parseForBlockComment() {
    var forBlockNode = new ForBlockNode();
    appendNode(forBlockNode);
    push(forBlockNode);
    var expr = tokenType.extract(token);
    var pos = expr.indexOf(":");
    if (pos == -1) {
      throw new JdbcException(
          Message.DOMA2124, sql, tokenizer.getLineNumber(), tokenizer.getPosition());
    }
    var identifier = expr.substring(0, pos).trim();
    if (identifier.isEmpty()) {
      throw new JdbcException(
          Message.DOMA2125, sql, tokenizer.getLineNumber(), tokenizer.getPosition());
    }
    var expression = expr.substring(pos + 1).trim();
    if (expression.isEmpty()) {
      throw new JdbcException(
          Message.DOMA2126, sql, tokenizer.getLineNumber(), tokenizer.getPosition());
    }
    var forNode = new ForNode(getLocation(), identifier, expression, token);
    forBlockNode.setForNode(forNode);
    push(forNode);
  }

  protected void parseExpandBlockComment() {
    var alias = tokenType.extract(token);
    if (alias.isEmpty() || StringUtil.isWhitespace(alias)) {
      alias = "\"\"";
    }
    var node = new ExpandNode(getLocation(), alias, token);
    appendNode(node);
    push(node);
  }

  protected void parsePopulateBlockComment() {
    var node = new PopulateNode(getLocation(), token);
    appendNode(node);
    push(node);
  }

  protected void parseOther() {
    appendNode(OtherNode.of(token));
  }

  protected void parseEOL() {
    var node = new EolNode(token);
    appendNode(node);
  }

  protected boolean containsOnlyWhitespaces(SqlNode node) {
    for (var child : node.getChildren()) {
      if (!(child instanceof WhitespaceNode)) {
        return false;
      }
    }
    return true;
  }

  protected void parseWhitespace() {
    appendNode(WhitespaceNode.of(token));
  }

  protected void removeNodesTo(Class<? extends SqlNode> clazz) {
    for (var it = nodeStack.iterator(); it.hasNext(); ) {
      SqlNode node = it.next();
      if (clazz.isInstance(node)) {
        break;
      }
      it.remove();
    }
  }

  protected boolean isInSelectStatementNode() {
    for (SqlNode node : nodeStack) {
      if (node instanceof ParensNode) {
        return false;
      }
      if (node instanceof SelectStatementNode) {
        return true;
      }
    }
    return false;
  }

  protected boolean isInWhereClauseAwareNode() {
    for (SqlNode node : nodeStack) {
      if (node instanceof ParensNode) {
        return false;
      }
      if (node instanceof WhereClauseAwareNode) {
        return true;
      }
    }
    return false;
  }

  protected boolean isInUpdateStatementNode() {
    for (SqlNode node : nodeStack) {
      if (node instanceof ParensNode) {
        return false;
      }
      if (node instanceof UpdateStatementNode) {
        return true;
      }
    }
    return false;
  }

  protected boolean isInIfBlockNode() {
    for (SqlNode node : nodeStack) {
      if (node instanceof ParensNode) {
        return false;
      }
      if (node instanceof IfBlockNode) {
        return true;
      }
    }
    return false;
  }

  protected boolean isInForBlockNode() {
    for (SqlNode node : nodeStack) {
      if (node instanceof ParensNode) {
        return false;
      }
      if (node instanceof ForBlockNode) {
        return true;
      }
    }
    return false;
  }

  protected boolean isInParensNode() {
    for (SqlNode node : nodeStack) {
      if (node instanceof ParensNode) {
        return true;
      }
    }
    return false;
  }

  protected boolean isInBlockNode() {
    for (SqlNode node : nodeStack) {
      if (node instanceof ParensNode) {
        return false;
      }
      if (node instanceof BlockNode) {
        return true;
      }
    }
    return false;
  }

  protected boolean isAfterValueNode() {
    return peek() instanceof ValueNode;
  }

  protected boolean isAfterExpandNode() {
    return peek() instanceof ExpandNode;
  }

  protected boolean isAfterOrderByClauseNode() {
    return peek() instanceof OrderByClauseNode;
  }

  protected void appendNode(SqlNode node) {
    if (isAfterValueNode()) {
      ValueNode valueNode = pop();
      if (node instanceof WordNode) {
        var wordNode = (WordNode) node;
        var word = wordNode.getWord();
        var matcher = LITERAL_PATTERN.matcher(word);
        if (matcher.lookingAt()) {
          valueNode.setWordNode(wordNode);
        } else {
          throw new JdbcException(
              Message.DOMA2142,
              sql,
              tokenizer.getLineNumber(),
              tokenizer.getPosition(),
              valueNode.getText(),
              word);
        }
      } else if (node instanceof ParensNode) {
        var parensNode = (ParensNode) node;
        parensNode.setAttachedWithValue(true);
        valueNode.setParensNode(parensNode);
      } else {
        throw new JdbcException(
            Message.DOMA2110,
            sql,
            tokenizer.getLineNumber(),
            tokenizer.getPosition(),
            valueNode.getText());
      }
    } else if (isAfterExpandNode()) {
      ExpandNode expandNode = pop();
      if (node instanceof OtherNode) {
        var otherNode = (OtherNode) node;
        if (!otherNode.getOther().equals("*")) {
          throw new JdbcException(
              Message.DOMA2143,
              sql,
              tokenizer.getLineNumber(),
              tokenizer.getPosition(),
              expandNode.getText());
        }
      } else {
        throw new JdbcException(
            Message.DOMA2143,
            sql,
            tokenizer.getLineNumber(),
            tokenizer.getPosition(),
            expandNode.getText());
      }
    } else {
      peek().appendNode(node);
    }
  }

  protected void push(AppendableSqlNode node) {
    nodeStack.push(node);
  }

  @SuppressWarnings("unchecked")
  protected <T extends AppendableSqlNode> T peek() {
    return (T) nodeStack.peek();
  }

  @SuppressWarnings("unchecked")
  protected <T extends SqlNode> T pop() {
    return (T) nodeStack.pop();
  }

  protected SqlLocation getLocation() {
    return new SqlLocation(sql, tokenizer.getLineNumber(), tokenizer.getPosition());
  }

  protected void validate() {
    if (isAfterValueNode()) {
      ValueNode valueNode = pop();
      throw new JdbcException(
          Message.DOMA2110,
          sql,
          tokenizer.getLineNumber(),
          tokenizer.getPosition(),
          valueNode.getText());
    }
    if (isInIfBlockNode()) {
      removeNodesTo(IfBlockNode.class);
      IfBlockNode ifBlockNode = pop();
      var location = ifBlockNode.getIfNode().getLocation();
      throw new JdbcException(
          Message.DOMA2133, sql, location.getLineNumber(), location.getPosition());
    }
    if (isInForBlockNode()) {
      removeNodesTo(ForBlockNode.class);
      ForBlockNode forBlockNode = pop();
      var location = forBlockNode.getForNode().getLocation();
      throw new JdbcException(
          Message.DOMA2134, sql, location.getLineNumber(), location.getPosition());
    }
  }

  protected void validateParensClosed() {
    if (isInParensNode()) {
      removeNodesTo(ParensNode.class);
      ParensNode parensNode = pop();
      var location = parensNode.getLocation();
      throw new JdbcException(
          Message.DOMA2135, sql, location.getLineNumber(), location.getPosition());
    }
  }
}
