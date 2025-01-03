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
package org.seasar.doma.internal.apt;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;

public enum CompilerKind {
  // Java Compiler
  JAVAC {
    @Override
    public JavaCompiler createJavaCompiler() {
      return ToolProvider.getSystemJavaCompiler();
    }
  },
  // Eclipse Compiler for Java
  ECJ {
    @Override
    public JavaCompiler createJavaCompiler() {
      return new EclipseCompiler();
    }
  };

  public abstract JavaCompiler createJavaCompiler();
}
