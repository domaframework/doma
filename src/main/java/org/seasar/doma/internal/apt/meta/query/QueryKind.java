package org.seasar.doma.internal.apt.meta.query;

import org.seasar.doma.jdbc.command.*;
import org.seasar.doma.jdbc.query.*;

/** @author taedium */
public enum QueryKind {
  SQLFILE_SELECT {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SqlFileSelectQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return SelectCommand.class;
    }
  },
  SQLFILE_SCRIPT {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SqlFileScriptQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return ScriptCommand.class;
    }
  },
  SQLFILE_INSERT {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SqlFileInsertQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return InsertCommand.class;
    }
  },
  SQLFILE_UPDATE {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SqlFileUpdateQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return UpdateCommand.class;
    }
  },
  SQLFILE_DELETE {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SqlFileDeleteQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return DeleteCommand.class;
    }
  },
  SQLFILE_BATCH_INSERT {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SqlFileBatchInsertQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return BatchInsertCommand.class;
    }
  },
  SQLFILE_BATCH_UPDATE {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SqlFileBatchUpdateQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return BatchUpdateCommand.class;
    }
  },
  SQLFILE_BATCH_DELETE {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SqlFileBatchDeleteQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return BatchDeleteCommand.class;
    }
  },
  AUTO_INSERT {

    @Override
    public Class<? extends Query> getQueryClass() {
      return AutoInsertQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return InsertCommand.class;
    }
  },
  AUTO_UPDATE {

    @Override
    public Class<? extends Query> getQueryClass() {
      return AutoUpdateQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return UpdateCommand.class;
    }
  },
  AUTO_DELETE {

    @Override
    public Class<? extends Query> getQueryClass() {
      return AutoDeleteQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return DeleteCommand.class;
    }
  },
  AUTO_BATCH_INSERT {

    @Override
    public Class<? extends Query> getQueryClass() {
      return AutoBatchInsertQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return BatchInsertCommand.class;
    }
  },
  AUTO_BATCH_UPDATE {

    @Override
    public Class<? extends Query> getQueryClass() {
      return AutoBatchUpdateQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return BatchUpdateCommand.class;
    }
  },
  AUTO_BATCH_DELETE {

    @Override
    public Class<? extends Query> getQueryClass() {
      return AutoBatchDeleteQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return BatchDeleteCommand.class;
    }
  },
  AUTO_FUNCTION {

    @Override
    public Class<? extends Query> getQueryClass() {
      return AutoFunctionQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return FunctionCommand.class;
    }
  },
  AUTO_PROCEDURE {

    @Override
    public Class<? extends Query> getQueryClass() {
      return AutoProcedureQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return ProcedureCommand.class;
    }
  },
  ARRAY_FACTORY {

    @Override
    public Class<? extends Query> getQueryClass() {
      return ArrayCreateQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return CreateCommand.class;
    }
  },
  BLOB_FACTORY {

    @Override
    public Class<? extends Query> getQueryClass() {
      return BlobCreateQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return CreateCommand.class;
    }
  },
  CLOB_FACTORY {

    @Override
    public Class<? extends Query> getQueryClass() {
      return ClobCreateQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return CreateCommand.class;
    }
  },
  NCLOB_FACTORY {

    @Override
    public Class<? extends Query> getQueryClass() {
      return NClobCreateQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return CreateCommand.class;
    }
  },
  SQLXML_FACTORY {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SQLXMLCreateQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return CreateCommand.class;
    }
  },

  DEFAULT {

    @Override
    public Class<? extends Query> getQueryClass() {
      return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return null;
    }
  },

  SQL_PROCESSOR {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SqlProcessorQuery.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Command> getCommandClass() {
      return SqlProcessorCommand.class;
    }
  },
  ;

  public abstract Class<? extends Query> getQueryClass();

  @SuppressWarnings("rawtypes")
  public abstract Class<? extends Command> getCommandClass();
}
