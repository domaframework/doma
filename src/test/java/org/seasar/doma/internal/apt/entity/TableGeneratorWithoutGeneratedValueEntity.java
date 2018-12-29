package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.TableGenerator;

/** @author taedium */
@Entity
public class TableGeneratorWithoutGeneratedValueEntity {

  @TableGenerator(pkColumnValue = "TableGeneratorWithoutGeneratedValueEntity")
  Integer id;
}
