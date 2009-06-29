package org.seasar.doma.internal.apt.meta;

import org.seasar.doma.internal.jdbc.command.BatchDeleteCommand;
import org.seasar.doma.internal.jdbc.command.BatchInsertCommand;
import org.seasar.doma.internal.jdbc.command.BatchUpdateCommand;
import org.seasar.doma.internal.jdbc.command.Command;
import org.seasar.doma.internal.jdbc.command.DeleteCommand;
import org.seasar.doma.internal.jdbc.command.FunctionCommand;
import org.seasar.doma.internal.jdbc.command.InsertCommand;
import org.seasar.doma.internal.jdbc.command.ProcedureCommand;
import org.seasar.doma.internal.jdbc.command.SelectCommand;
import org.seasar.doma.internal.jdbc.command.UpdateCommand;
import org.seasar.doma.internal.jdbc.query.AutoBatchDeleteQuery;
import org.seasar.doma.internal.jdbc.query.AutoBatchInsertQuery;
import org.seasar.doma.internal.jdbc.query.AutoBatchUpdateQuery;
import org.seasar.doma.internal.jdbc.query.AutoDeleteQuery;
import org.seasar.doma.internal.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.internal.jdbc.query.AutoInsertQuery;
import org.seasar.doma.internal.jdbc.query.AutoProcedureQuery;
import org.seasar.doma.internal.jdbc.query.AutoUpdateQuery;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.query.SqlFileBatchDeleteQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileBatchInsertQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileBatchUpdateQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileDeleteQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileInsertQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery;
import org.seasar.doma.internal.jdbc.query.SqlFileUpdateQuery;

/**
 * @author taedium
 * 
 */
public enum QueryKind {

    SQLFILE_SELECT {

        @Override
        public Class<? extends Query> getQueryClass() {
            return SqlFileSelectQuery.class;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<? extends Command> getCommandClass() {
            return SelectCommand.class;
        }

    },
    SQLFILE_INSERT {

        @Override
        public Class<? extends Query> getQueryClass() {
            return SqlFileInsertQuery.class;
        }

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
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

        @SuppressWarnings("unchecked")
        @Override
        public Class<? extends Command> getCommandClass() {
            return ProcedureCommand.class;
        }

    };

    public abstract Class<? extends Query> getQueryClass();

    @SuppressWarnings("unchecked")
    public abstract Class<? extends Command> getCommandClass();

}
