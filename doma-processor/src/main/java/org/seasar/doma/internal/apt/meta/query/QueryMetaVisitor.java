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
package org.seasar.doma.internal.apt.meta.query;

public interface QueryMetaVisitor<R> {

  R visitSqlFileSelectQueryMeta(SqlFileSelectQueryMeta m);

  R visitSqlFileModifyQueryMeta(SqlFileModifyQueryMeta m);

  R visitSqlFileBatchModifyQueryMeta(SqlFileBatchModifyQueryMeta m);

  R visitSqlFileScriptQueryMeta(SqlFileScriptQueryMeta m);

  R visitAutoModifyQueryMeta(AutoModifyQueryMeta m);

  R visitAutoMultiInsertQueryMeta(AutoMultiInsertQueryMeta m);

  R visitAutoBatchModifyQueryMeta(AutoBatchModifyQueryMeta m);

  R visitAutoFunctionQueryMeta(AutoFunctionQueryMeta m);

  R visitAutoProcedureQueryMeta(AutoProcedureQueryMeta m);

  R visitArrayCreateQueryMeta(ArrayCreateQueryMeta m);

  R visitBlobCreateQueryMeta(BlobCreateQueryMeta m);

  R visitClobCreateQueryMeta(ClobCreateQueryMeta m);

  R visitNClobCreateQueryMeta(NClobCreateQueryMeta m);

  R visitSQLXMLCreateQueryMeta(SQLXMLCreateQueryMeta m);

  R visitDefaultQueryMeta(DefaultQueryMeta m);

  R visitSqlProcessorQueryMeta(SqlProcessorQueryMeta m);
}
