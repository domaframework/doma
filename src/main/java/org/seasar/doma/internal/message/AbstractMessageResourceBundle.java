/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.message;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.message.MessageResource;

/**
 * {@link Enum} で表現されたメッセージコードを扱うリソースバンドルです。
 * 
 * @author taedium
 * 
 */
public abstract class AbstractMessageResourceBundle<M extends Enum<M> & MessageResource>
        extends ResourceBundle {

    protected final Class<M> messageCodeClass;

    protected AbstractMessageResourceBundle(Class<M> messageCodeClass) {
        if (messageCodeClass == null) {
            throw new DomaNullPointerException("messageCodeClass");
        }
        this.messageCodeClass = messageCodeClass;
    }

    @Override
    public Enumeration<String> getKeys() {
        List<String> keys = new LinkedList<String>();
        for (M messageCode : EnumSet.allOf(messageCodeClass)) {
            keys.add(messageCode.getCode());
        }
        return Collections.enumeration(keys);
    }

    @Override
    protected Object handleGetObject(String key) {
        if (key == null) {
            throw new DomaNullPointerException("key");
        }
        M messageCode = Enum.valueOf(messageCodeClass, key);
        return messageCode.getMessagePattern();
    }
}
