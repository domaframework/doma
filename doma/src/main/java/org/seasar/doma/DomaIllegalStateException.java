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
package org.seasar.doma;

import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class DomaIllegalStateException extends DomaException {

    private static final long serialVersionUID = 1L;

    protected final Object object;

    protected final String memberVariableName;

    protected final Object value;

    public DomaIllegalStateException(Object object, String memberVariableName,
            Object value) {
        super(MessageCode.DOMA0006, object, memberVariableName, value);
        this.object = object;
        this.memberVariableName = memberVariableName;
        this.value = value;
    }

    public Object getObject() {
        return object;
    }

    public String getMemberVariableName() {
        return memberVariableName;
    }

    public Object getValue() {
        return value;
    }

}
