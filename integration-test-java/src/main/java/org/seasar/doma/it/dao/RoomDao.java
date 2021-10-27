package org.seasar.doma.it.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.it.entity.Room;

@Dao
public interface RoomDao {

  @Select
  Room selectById(Integer id);

  @Select
  Room selectByColors(String[] colors);

  @Insert
  int insert(Room room);
}
