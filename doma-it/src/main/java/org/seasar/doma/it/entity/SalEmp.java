package org.seasar.doma.it.entity;

import java.sql.Array;
import java.util.HashSet;
import java.util.Set;

import org.seasar.doma.ChangedProperties;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "SAL_EMP")
public class SalEmp {

    @Id
    String name;

    Array payByQuarter;

    Array schedule;

    @ChangedProperties
    Set<String> changedProperties = new HashSet<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        changedProperties.add("name");
        this.name = name;
    }

    public Array getPayByQuarter() {
        return payByQuarter;
    }

    public void setPayByQuarter(Array payByQuarter) {
        changedProperties.add("payByQuarter");
        this.payByQuarter = payByQuarter;
    }

    public Array getSchedule() {
        return schedule;
    }

    public void setSchedule(Array schedule) {
        changedProperties.add("schedule");
        this.schedule = schedule;
    }

}
