package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import java.util.Map;

import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.command.MapProvider;
import org.seasar.doma.jdbc.query.Query;

/**
 * @author taedium
 * 
 */
public class MapListParameter extends AbstractListParameter<Map<String, Object>> {

    protected final MapKeyNamingType mapKeyNamingType;

    public MapListParameter(MapKeyNamingType mapKeyNamingType, List<Map<String, Object>> list,
            String name) {
        super(list, name);
        assertNotNull(mapKeyNamingType);
        this.mapKeyNamingType = mapKeyNamingType;
    }

    @Override
    public MapProvider createObjectProvider(Query query) {
        return new MapProvider(query, mapKeyNamingType);
    }

}
