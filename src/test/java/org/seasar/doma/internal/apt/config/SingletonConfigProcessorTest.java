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
package org.seasar.doma.internal.apt.config;

import org.seasar.doma.internal.apt.AptTestCase;
import org.seasar.doma.internal.apt.SingletonConfigProcessor;
import org.seasar.doma.message.Message;

/** @author taedium */
public class SingletonConfigProcessorTest extends AptTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addOption("-Adoma.test=true");
  }

  public void testNoConfig() throws Exception {
    Class<?> target = NoConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4253);
  }

  public void testMethodNotFoundConfig() throws Exception {
    Class<?> target = MethodNotFoundConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4254);
  }

  public void testPublicConstructorConfig() throws Exception {
    Class<?> target = PublicConstructorConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertFalse(getCompiledResult());
    assertMessage(Message.DOMA4256);
  }

  public void testValidConfig() throws Exception {
    Class<?> target = ValidConfig.class;
    SingletonConfigProcessor processor = new SingletonConfigProcessor();
    addProcessor(processor);
    addCompilationUnit(target);
    compile();
    assertTrue(getCompiledResult());
  }
}
