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
package org.seasar.doma.jdbc;

/**
 * A visitor interface for processing different types of SQL parameters.
 *
 * <p>This interface implements the visitor design pattern, providing methods to process each type
 * of SQL parameter. Implementations can define specific behavior for handling different parameter
 * types during SQL execution.
 *
 * @param <R> the result type returned by visit methods
 * @param <P> the parameter type passed to visit methods
 * @param <TH> the throwable type that may be thrown by visit methods
 * @see SqlParameter#accept(SqlParameterVisitor, Object)
 */
@SuppressWarnings("SameReturnValue")
public interface SqlParameterVisitor<R, P, TH extends Throwable> {

  <BASIC> R visitInParameter(InParameter<BASIC> parameter, P p) throws TH;

  <BASIC> R visitOutParameter(OutParameter<BASIC> parameter, P p) throws TH;

  <BASIC, INOUT extends InParameter<BASIC> & OutParameter<BASIC>> R visitInOutParameter(
      INOUT parameter, P p) throws TH;

  <ELEMENT> R visitListParameter(ListParameter<ELEMENT> parameter, P p) throws TH;

  <BASIC, RESULT> R visitSingleResultParameter(SingleResultParameter<BASIC, RESULT> parameter, P p)
      throws TH;

  <ELEMENT> R visitResultListParameter(ResultListParameter<ELEMENT> parameter, P p) throws TH;
}
