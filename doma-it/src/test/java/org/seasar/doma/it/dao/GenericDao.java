package org.seasar.doma.it.dao;

import java.util.List;

import doma.BatchDelete;
import doma.BatchInsert;
import doma.BatchUpdate;
import doma.Delete;
import doma.Insert;
import doma.Update;

@doma.GenericDao
public interface GenericDao<E> {

    @Insert
    int insert(E entity);

    @Insert(excludesNull = true)
    int insert_excludesNull(E entity);

    @BatchInsert
    int[] insert(List<E> entities);

    @Update
    int update(E entity);

    @Update(excludesNull = true)
    int update_excludesNull(E entity);

    @Update(includesVersion = true)
    int update_includesVersion(E entity);

    @Update(suppressesOptimisticLockException = true)
    int update_suppressesOptimisticLockException(E entity);

    @BatchUpdate
    int[] update(List<E> entities);

    @BatchUpdate(includesVersion = true)
    int[] update_includesVersion(List<E> entities);

    @BatchUpdate(suppressesOptimisticLockException = true)
    int[] update_suppressesOptimisticLockException(List<E> entities);

    @Delete
    int delete(E entity);

    @Delete(ignoresVersion = true)
    int delete_ignoresVersion(E entity);

    @Delete(suppressesOptimisticLockException = true)
    int delete_suppressesOptimisticLockException(E entity);

    @BatchDelete
    int[] delete(List<E> entity);

    @BatchDelete(ignoresVersion = true)
    int[] delete_ignoresVersion(List<E> entity);

    @BatchDelete(suppressesOptimisticLockException = true)
    int[] delete_suppressesOptimisticLockException(List<E> entity);
}
