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
package org.seasar.doma.copy;

import org.seasar.doma.message.DomaMessageCode;

/**
 * プロパティの変換に失敗した場合にスローされる例外です。
 * 
 * @author taedium
 * 
 */
public class PropertyCopyException extends CopyException {

    private static final long serialVersionUID = 1L;

    /** コピー元のクラス名 */
    protected final String srcClassName;

    /** コピー元のプロパティ名 */
    protected final String srcPropertyName;

    /** コピー元の値 */
    protected final Object srcPropertyValue;

    /**
     * インスタンスを構築します。
     * 
     * @param srcClassName
     *            コピー元のクラス名
     * @param srcPropertyName
     *            コピー元のプロパティ名
     * @param srcPropertyValue
     *            コピー元の値
     * @param cause
     *            失敗した原因
     */
    public PropertyCopyException(String srcClassName, String srcPropertyName,
            Object srcPropertyValue, Throwable cause) {
        super(DomaMessageCode.DOMA7001, cause, srcClassName, srcPropertyName,
                srcPropertyValue, cause);
        this.srcClassName = srcClassName;
        this.srcPropertyName = srcPropertyName;
        this.srcPropertyValue = srcPropertyValue;
    }

    /**
     * コピー元のクラス名を返します。
     * 
     * @return コピー元のクラス名
     */
    public String getSrcClassName() {
        return srcClassName;
    }

    /**
     * コピー元のプロパティ名を返します。
     * 
     * @return コピー元のプロパティ名
     */
    public String getSrcPropertyName() {
        return srcPropertyName;
    }

    /**
     * コピー元の値を返します。
     * 
     * @return コピー元の値
     */
    public Object getSrcPropertyValue() {
        return srcPropertyValue;
    }

}
