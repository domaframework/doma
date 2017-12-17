package org.seasar.doma.internal.jdbc.command;

import java.util.Map;

import org.seasar.doma.MapKeyNamingType;

/**
 * @author taedium
 * 
 */
public class MapResultListHandler extends AbstractResultListHandler<Map<String, Object>> {

    public MapResultListHandler(MapKeyNamingType keyNamingType) {
        super(new MapIterationHandler<>(keyNamingType,
                new ResultListCallback<Map<String, Object>>()));
    }

}
