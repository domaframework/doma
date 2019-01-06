package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Version;

@Entity
public class VersionDuplicatedEntity {

  @Id Integer id;

  @Version Integer version;

  @Version Integer version2;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Integer getVersion2() {
    return version2;
  }

  public void setVersion2(Integer version2) {
    this.version2 = version2;
  }
}
