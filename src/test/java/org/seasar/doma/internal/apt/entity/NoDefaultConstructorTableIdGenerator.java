package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.jdbc.id.BuiltinTableIdGenerator;

/** @author taedium */
public class NoDefaultConstructorTableIdGenerator extends BuiltinTableIdGenerator {

  private NoDefaultConstructorTableIdGenerator() {}
}
