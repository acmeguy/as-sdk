package com.activitystream.sdk.utilities;

public class SimpleArrayWrapper implements ColumnarDataReader {

    private String[] line;

    public SimpleArrayWrapper(String[] line) {
        this.line = line;
    }

    @Override
    public String column(Enum field) {
        return this.line[field.ordinal()];
    }

    public String column(int fieldNr) {
        return this.line[fieldNr];
    }

    public int length() {
        return (line != null) ? line.length : 0;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (String part : line) {
            string.append("|").append(part);
        }
        return string.toString();
    }


}
