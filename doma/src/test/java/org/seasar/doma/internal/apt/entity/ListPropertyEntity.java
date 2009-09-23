package org.seasar.doma.internal.apt.entity;

import java.util.List;

import org.seasar.doma.Entity;
import org.seasar.doma.Transient;

@Entity
public class ListPropertyEntity {

    @Transient
    List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

}
