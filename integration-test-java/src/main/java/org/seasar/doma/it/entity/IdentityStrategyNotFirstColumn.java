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
package org.seasar.doma.it.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;

@Entity(listener = IdentityStrategyNotFirstColumnListener.class)
@Table(name = "IDENTITY_STRATEGY_NOT_FIRST_COLUMN")
public class IdentityStrategyNotFirstColumn {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "TOKEN_ID")
  Long tokenId;

  @Column(name = "TOKEN")
  String token;

  @Column(name = "USER_ID")
  Long userId;

  @Transient Long log;

  public Long getTokenId() {
    return tokenId;
  }

  public void setTokenId(Long tokenId) {
    this.tokenId = tokenId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getLog() {
    return log;
  }

  public void setLog(Long log) {
    this.log = log;
  }
}
