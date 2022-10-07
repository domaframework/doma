package org.seasar.doma.it.criteria;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Transient;

@Entity(metamodel = @Metamodel)
public class Team {
  @Id private Integer id;
  private String name;

  public Team() {}

  public Team(Integer id, String name) {
    this();
    this.id = id;
    this.name = name;
  }

  @Transient private final List<Player> players = new ArrayList<>();

  @Transient private final List<Coach> coaches = new ArrayList<>();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public List<Coach> getCoaches() {
    return coaches;
  }
}
