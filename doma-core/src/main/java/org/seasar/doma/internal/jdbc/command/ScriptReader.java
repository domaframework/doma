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
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.seasar.doma.internal.util.IOUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.ScriptBlockContext;
import org.seasar.doma.jdbc.query.ScriptQuery;
import org.seasar.doma.message.Message;

public class ScriptReader {

  protected final ScriptQuery query;

  protected final ScriptTokenizer tokenizer;

  protected BufferedReader reader;

  protected int lineCount;

  protected int lineNumber;

  protected boolean endOfFile;

  protected boolean endOfLine = true;

  public ScriptReader(ScriptQuery query) {
    assertNotNull(query);
    this.query = query;
    this.tokenizer = new ScriptTokenizer(query.getBlockDelimiter());
  }

  public String readSql() {
    if (endOfFile) {
      return null;
    }
    try {
      if (reader == null) {
        reader = createBufferedReader();
      }
      SqlBuilder builder = new SqlBuilder();
      readLineLoop:
      for (; ; ) {
        if (endOfLine) {
          lineCount++;
          tokenizer.addLine(reader.readLine());
          builder.notifyLineChanged();
        }
        for (; ; ) {
          builder.build(tokenizer.nextToken(), tokenizer.getToken());
          if (builder.isTokenRequired()) {
            continue;
          } else if (builder.isLineRequired()) {
            continue readLineLoop;
          } else if (builder.isCompleted()) {
            return builder.getSql();
          }
          assertUnreachable();
        }
      }
    } catch (IOException e) {
      throw new JdbcException(Message.DOMA2078, e, query.getScriptFilePath(), e);
    }
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void close() {
    IOUtil.close(reader);
  }

  protected BufferedReader createBufferedReader() throws IOException {
    Supplier<Reader> supplier = query.getReaderSupplier();
    try {
      return new BufferedReader(supplier.get());
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
  }

  protected class SqlBuilder {

    protected boolean tokenRequired;

    protected boolean lineRequired;

    protected boolean completed;

    protected final StringBuilder buf = new StringBuilder(300);

    protected List<String> wordList = new ArrayList<>();

    protected final ScriptBlockContext sqlBlockContext;

    protected boolean lineChanged;

    protected SqlBuilder() {
      sqlBlockContext = query.getConfig().getDialect().createScriptBlockContext();
    }

    protected void build(ScriptTokenType tokenType, String token) {
      reset();
      if (buf.length() == 0) {
        lineNumber = lineCount;
      }
      switch (tokenType) {
        case WORD:
          appendWord(token);
        case QUOTE:
        case OTHER:
          appendToken(token);
          requireToken();
          break;
        case END_OF_LINE:
          endOfLine = true;
          requireLine();
          break;
        case STATEMENT_DELIMITER:
          if (isInBlock()) {
            appendToken(token);
            requireToken();
          } else {
            complete();
          }
          break;
        case BLOCK_DELIMITER:
          if (isSqlEmpty()) {
            requireToken();
          } else {
            complete();
          }
          break;
        case END_OF_FILE:
          endOfFile = true;
          complete();
          break;
        default:
          requireToken();
          break;
      }
    }

    protected void reset() {
      endOfLine = false;
      requireToken();
    }

    protected boolean isTokenRequired() {
      return tokenRequired;
    }

    protected void requireToken() {
      tokenRequired = true;
      lineRequired = false;
      completed = false;
    }

    protected boolean isLineRequired() {
      return lineRequired;
    }

    protected void requireLine() {
      lineRequired = true;
      tokenRequired = false;
      completed = false;
    }

    protected boolean isCompleted() {
      return completed;
    }

    protected void complete() {
      completed = true;
      tokenRequired = false;
      lineRequired = false;
    }

    protected void appendWord(String word) {
      sqlBlockContext.addKeyword(word);
    }

    protected void appendToken(String token) {
      appendWhitespaceIfNecessary();
      buf.append(token);
    }

    protected void appendWhitespaceIfNecessary() {
      if (!lineChanged) {
        return;
      }
      if (buf.length() > 0) {
        char lastChar = buf.charAt(buf.length() - 1);
        if (!Character.isWhitespace(lastChar)) {
          buf.append(' ');
        }
      }
      lineChanged = false;
    }

    protected void notifyLineChanged() {
      lineChanged = true;
    }

    protected boolean isInBlock() {
      return sqlBlockContext.isInBlock();
    }

    protected boolean isSqlEmpty() {
      return buf.toString().trim().length() == 0;
    }

    protected String getSql() {
      if (!completed) {
        assertUnreachable();
      }

      String sql = buf.toString().trim();
      return endOfFile && sql.length() == 0 ? null : sql;
    }
  }
}
