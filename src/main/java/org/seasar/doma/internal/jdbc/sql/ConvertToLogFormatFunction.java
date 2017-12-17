package org.seasar.doma.internal.jdbc.sql;

import org.seasar.doma.jdbc.SqlLogFormatter;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class ConvertToLogFormatFunction implements SqlLogFormattingFunction {

    @Override
    public <V> String apply(Wrapper<V> wrapper, SqlLogFormatter<V> formatter) {
        return formatter.convertToLogFormat(wrapper.get());
    }

}
