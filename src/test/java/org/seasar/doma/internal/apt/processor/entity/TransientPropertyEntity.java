package org.seasar.doma.internal.apt.processor.entity;

import java.util.List;
import org.seasar.doma.Entity;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

@Entity
public class TransientPropertyEntity {

  @Version Integer id;

  @Transient List<Integer> list;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<Integer> getList() {
    return list;
  }

  public void setList(List<Integer> list) {
    this.list = list;
  }
}
