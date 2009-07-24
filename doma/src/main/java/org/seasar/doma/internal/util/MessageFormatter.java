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
package org.seasar.doma.internal.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.seasar.doma.MessageCode;

/**
 * @author taedium
 * 
 */
public final class MessageFormatter {

    public static String getMessage(MessageCode messageCode,
            ResourceBundle bundle, Object... args) {
        String pattern = bundle.getString(messageCode.getKey());
        return MessageFormat
                .format("[" + messageCode.getKey() + "] " + pattern, args);
    }
}
