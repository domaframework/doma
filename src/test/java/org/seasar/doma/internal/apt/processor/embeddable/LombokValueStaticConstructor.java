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
package org.seasar.doma.internal.apt.processor.embeddable;

import org.seasar.doma.Embeddable;
import org.seasar.doma.internal.apt.lombok.Value;

/**
 * @author nakamura-to
 *
 */
@Embeddable
@Value(staticConstructor = "of")
public class LombokValueStaticConstructor {

    @SuppressWarnings("unused")
    private String street;

    @SuppressWarnings("unused")
    private String city;

    public LombokValueStaticConstructor(String street, String city) {
    }

}
