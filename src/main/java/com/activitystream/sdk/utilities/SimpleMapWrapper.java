package com.activitystream.sdk.utilities;

import java.util.Map;

/**
 * @author ivan
 */
public class SimpleMapWrapper implements ColumnarDataReader {

    private final Map<String,String> map;

    public SimpleMapWrapper(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String column(Enum column) {
        String value = null;
        if (column instanceof Labeled) {
            value = map.get(((Labeled) column).getLabel());
        }
        if (value == null) {
            value = map.get(column.name());
        }
        return value;
    }
}
