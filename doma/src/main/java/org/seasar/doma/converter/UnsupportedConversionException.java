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
package org.seasar.doma.converter;

import org.seasar.doma.message.DomaMessageCode;

/**
 * あるクラスから別のクラスへの変換がサポートされていない場合にスローされる例外です。
 * 
 * @author taedium
 * 
 */
public class UnsupportedConversionException extends ConversionException {

    private static final long serialVersionUID = 1L;

    /** 変換元のクラス */
    protected final String srcClassName;

    /** 変換先のクラス */
    protected final String destClassName;

    /** 変換元の値 */
    protected final Object value;

    /**
     * インスタンスを構築します。
     * 
     * @param srcClassName
     *            変換元のクラス
     * @param destClassName
     *            変換先のクラス
     * @param value
     *            変換元の値
     */
    public UnsupportedConversionException(String srcClassName,
            String destClassName, Object value) {
        super(DomaMessageCode.DOMA5001, srcClassName, value, destClassName);
        this.srcClassName = srcClassName;
        this.destClassName = destClassName;
        this.value = value;
    }

    /**
     * 変換元のクラスを返します。
     * 
     * @return 変換元のクラス
     */
    public String getSrcClassName() {
        return srcClassName;
    }

    /**
     * 変換先のクラスを返します。
     * 
     * @return 変換先のクラス
     */
    public String getDestClassName() {
        return destClassName;
    }

    /**
     * 変換元の値を返します。
     * 
     * @return 変換元の値
     */
    public Object getValue() {
        return value;
    }

}
