package org.seasar.doma.internal.jdbc;

import java.util.List;

import org.seasar.doma.jdbc.Config;


public interface Entity<I> {

    String MEMBER_PREFIX = "__";

    String __getName();

    String __getQualifiedTableName(Config config);

    GeneratedIdProperty<?> __getGeneratedIdProperty();

    VersionProperty<?> __getVersionProperty();

    Property<?> __getPropertyByName(String propertyName);

    List<Property<?>> __getProperties();

    I __asInterface();

    void __preInsert();

    void __preUpdate();

    void __preDelete();
}
