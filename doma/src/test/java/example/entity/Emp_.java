package example.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.AssignedIdProperty;
import org.seasar.doma.internal.jdbc.BasicProperty;
import org.seasar.doma.internal.jdbc.DomaAbstractEntity;
import org.seasar.doma.internal.jdbc.GeneratedIdProperty;
import org.seasar.doma.internal.jdbc.NullEntityListener;
import org.seasar.doma.internal.jdbc.Property;
import org.seasar.doma.internal.jdbc.VersionProperty;


@Generated("")
public class Emp_ extends DomaAbstractEntity<Emp> implements Emp {

    private final NullEntityListener __listener = new NullEntityListener();

    private final AssignedIdProperty<IntegerDomain> id = new AssignedIdProperty<IntegerDomain>(
            "id", null, new IntegerDomain(), true, true);

    private final BasicProperty<StringDomain> name = new BasicProperty<StringDomain>(
            "name", null, new StringDomain(), true, true);

    private final BasicProperty<BigDecimalDomain> salary = new BasicProperty<BigDecimalDomain>(
            "salary", null, new BigDecimalDomain(), true, true);

    private final VersionProperty<IntegerDomain> version = new VersionProperty<IntegerDomain>(
            "version", null, new IntegerDomain(), true, true);

    private final String __name = "emp";

    private List<Property<?>> __properties;

    private Map<String, Property<?>> __propertyMap;

    public Emp_() {
        super(null, null, null);
    }

    @Override
    public IntegerDomain id() {
        return id.getDomain();
    }

    @Override
    public StringDomain name() {
        return name.getDomain();
    }

    @Override
    public BigDecimalDomain salary() {
        return salary.getDomain();
    }

    @Override
    public IntegerDomain version() {
        return version.getDomain();
    }

    @Override
    public Emp __asInterface() {
        return this;
    }

    @Override
    public String __getName() {
        return __name;
    }

    @Override
    public List<Property<?>> __getProperties() {
        if (__properties == null) {
            List<Property<?>> list = new ArrayList<Property<?>>();
            list.add(id);
            list.add(name);
            list.add(salary);
            list.add(version);
            __properties = Collections.unmodifiableList(list);
        }
        return __properties;
    }

    @Override
    public Property<?> __getPropertyByName(String propertyName) {
        if (__propertyMap == null) {
            Map<String, Property<?>> map = new HashMap<String, Property<?>>();
            map.put("id", id);
            map.put("name", name);
            map.put("salary", salary);
            map.put("version", version);
            __propertyMap = Collections.unmodifiableMap(map);
        }
        return __propertyMap.get(propertyName);
    }

    @Override
    public GeneratedIdProperty<?> __getGeneratedIdProperty() {
        return null;
    }

    @Override
    public VersionProperty<?> __getVersionProperty() {
        return version;
    }

    @Override
    public void __preInsert() {
        __listener.preInsert(this);
    }

    @Override
    public void __preUpdate() {
        __listener.preUpdate(this);
    }

    @Override
    public void __preDelete() {
        __listener.preDelete(this);
    }

}
