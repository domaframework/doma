package org.seasar.doma.internal.jdbc.entity;

import java.util.function.BiFunction;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.entity.NamingType;

public final class TableNames {

  public static BiFunction<NamingType, String, String> namingFunction = Naming.DEFAULT::apply;
}
