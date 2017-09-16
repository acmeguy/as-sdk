package com.activitystream.sdk.utilities;

/**
 * Helper class for reading the records from different structures consistently (HashMap, Array...)
 *
 * @author ivan
 */
//TODO think about the naming
public interface ColumnarDataReader {
    
    String column(Enum column);
    
}
