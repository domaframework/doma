package org.seasar.doma.it.entity;

import java.sql.Timestamp;
import java.util.Date;

import doma.jdbc.EntityListener;

public class EmpListener implements EntityListener<Emp> {

    @Override
    public void preDelete(Emp entity) {
    }

    @Override
    public void preInsert(Emp entity) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        entity.insertTimestamp().set(timestamp);
    }

    @Override
    public void preUpdate(Emp entity) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        entity.updateTimestamp().set(timestamp);
    }

}
