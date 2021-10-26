package org.seasar.doma.it.dao;

import org.seasar.doma.Column;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Domain;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Table;
import org.seasar.doma.Update;
import org.seasar.doma.Version;

@Dao
public interface BranchDao {

  @Select
  Branch selectById(Integer branchId);

  @Insert
  int insert(Branch entity);

  @Update
  int update(Branch entity);

  @Delete
  int delete(Branch entity);

  @Entity
  @Table(name = "DEPARTMENT")
  public class Branch {

    @Id
    @Column(name = "DEPARTMENT_ID")
    public Integer branchId;

    public BranchDetail branchDetail;

    @Version public Integer version;
  }

  @Embeddable
  public class BranchDetail {

    @Column(name = "DEPARTMENT_NO")
    public Integer branchNo;

    @Column(name = "DEPARTMENT_NAME")
    public String branchName;

    public Location location;

    public BranchDetail(Integer branchNo, String branchName, Location location) {
      this.branchNo = branchNo;
      this.branchName = branchName;
      this.location = location;
    }
  }

  @Domain(valueType = String.class)
  public class Location {

    private final String value;

    public Location(final String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }
  }
}
