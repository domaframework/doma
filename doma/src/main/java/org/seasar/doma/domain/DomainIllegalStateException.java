/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.domain;

import org.seasar.doma.message.DomaMessageCode;

/**
 * {@link Domain} の状態が不正な場合にスローされる例外です。
 * 
 * @author taedium
 * 
 */
public class DomainIllegalStateException extends DomainException {

    private static final long serialVersionUID = 1L;

    /**
     * 不正な状態の説明を指定してインスタンスを構築します。
     * 
     * @param description
     *            不正な状態の説明
     */
    public DomainIllegalStateException(String description) {
        super(DomaMessageCode.DOMA1005, description);
    }

    /**
     * 不正な状態の原因を指定してインスタンスを構築します。
     * 
     * @param cause
     *            不正な状態の原因
     */
    public DomainIllegalStateException(Throwable cause) {
        super(DomaMessageCode.DOMA1005, cause, cause);
    }

}
