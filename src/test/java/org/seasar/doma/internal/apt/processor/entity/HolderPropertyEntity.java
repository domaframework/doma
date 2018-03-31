package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.*;

@Entity
public class HolderPropertyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequence = "SEQ")
  Identifier id;

  Name name;

  @Version Ver ver;

  public Identifier getId() {
    return id;
  }

  public void setId(Identifier id) {
    this.id = id;
  }

  public Name getName() {
    return name;
  }

  public void setName(Name name) {
    this.name = name;
  }

  public Ver getVer() {
    return ver;
  }

  public void setVer(Ver ver) {
    this.ver = ver;
  }
}
