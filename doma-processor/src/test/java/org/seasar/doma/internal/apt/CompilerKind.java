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
