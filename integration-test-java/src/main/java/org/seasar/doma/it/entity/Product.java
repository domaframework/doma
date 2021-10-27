package org.seasar.doma.it.entity;

import java.sql.SQLXML;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class Product {

  @Id public Integer id;

  public SQLXML value;
}
