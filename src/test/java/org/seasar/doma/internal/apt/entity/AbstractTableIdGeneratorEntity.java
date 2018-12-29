package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.TableGenerator;

/** @author taedium */
@Entity
public class AbstractTableIdGeneratorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @TableGenerator(pkColumnValue = "aaa", implementer = AbstractTableIdGenerator.class)
  Integer id;
}
