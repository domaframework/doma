package org.seasar.doma.internal.apt.meta.query;

import org.seasar.doma.jdbc.command.BatchDeleteCommand;
import org.seasar.doma.jdbc.command.BatchInsertCommand;
import org.seasar.doma.jdbc.command.BatchUpdateCommand;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.CreateCommand;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.command.FunctionCommand;
import org.seasar.doma.jdbc.command.InsertCommand;
import org.seasar.doma.jdbc.command.ProcedureCommand;
import org.seasar.doma.jdbc.command.ScriptCommand;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.command.SqlProcessorCommand;
import org.seasar.doma.jdbc.command.UpdateCommand;
import org.seasar.doma.jdbc.query.ArrayCreateQuery;
import org.seasar.doma.jdbc.query.AutoBatchDeleteQuery;
import org.seasar.doma.jdbc.query.AutoBatchInsertQuery;
import org.seasar.doma.jdbc.query.AutoBatchUpdateQuery;
import org.seasar.doma.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.jdbc.query.AutoInsertQuery;
import org.seasar.doma.jdbc.query.AutoProcedureQuery;
import org.seasar.doma.jdbc.query.AutoUpdateQuery;
import org.seasar.doma.jdbc.query.BlobCreateQuery;
import org.seasar.doma.jdbc.query.ClobCreateQuery;
import org.seasar.doma.jdbc.query.NClobCreateQuery;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.jdbc.query.SQLXMLCreateQuery;
import org.seasar.doma.jdbc.query.SqlProcessorQuery;
import org.seasar.doma.jdbc.query.SqlTemplateBatchDeleteQuery;
import org.seasar.doma.jdbc.query.SqlTemplateBatchInsertQuery;
import org.seasar.doma.jdbc.query.SqlTemplateBatchUpdateQuery;
import org.seasar.doma.jdbc.query.SqlTemplateDeleteQuery;
import org.seasar.doma.jdbc.query.SqlTemplateInsertQuery;
import org.seasar.doma.jdbc.query.SqlTemplateSelectQuery;
import org.seasar.doma.jdbc.query.SqlTemplateUpdateQuery;
import org.seasar.doma.jdbc.query.StaticScriptQuery;

public enum QueryKind {
  SQLFILE_SELECT {

    @Override
    public Class<? extends Query> getQueryClass() {
      return SqlTemplateSelectQuery.class;
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
      return StaticScriptQuery.class;
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
      return SqlTemplateInsertQuery.class;
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
      return SqlTemplateUpdateQuery.class;
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
      return SqlTemplateDeleteQuery.class;
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
      return SqlTemplateBatchInsertQuery.class;
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
      return SqlTemplateBatchUpdateQuery.class;
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
      return SqlTemplateBatchDeleteQuery.class;
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

  NON_ABSTRACT {

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
  ;

  public abstract Class<? extends Query> getQueryClass();

  @SuppressWarnings("rawtypes")
  public abstract Class<? extends Command> getCommandClass();
}
