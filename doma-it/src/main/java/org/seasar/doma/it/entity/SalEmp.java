package org.seasar.doma.it.entity;

import java.sql.Array;
import java.util.HashSet;
import java.util.Set;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.ModifiedProperties;
import org.seasar.doma.Table;

@Entity
@Table(name = "SAL_EMP")
public class SalEmp {

    @Id
    String name;

    Array pay_by_quarter;

    Array schedule;

    @ModifiedProperties
    Set<String> modifiedProperties = new HashSet<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        modifiedProperties.add("name");
        this.name = name;
    }

    public Array getPay_by_quarter() {
        return pay_by_quarter;
    }

    public void setPay_by_quarter(Array payByQuarter) {
        modifiedProperties.add("payByQuarter");
        pay_by_quarter = payByQuarter;
    }

    public Array getSchedule() {
        return schedule;
    }

    public void setSchedule(Array schedule) {
        modifiedProperties.add("schedule");
        this.schedule = schedule;
    }

}
