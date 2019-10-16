package net.endoedgar;

import java.util.List;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;

public class TextArrayWritable extends ArrayWritable {
    public TextArrayWritable() {
        super(Text.class);
    }

    public TextArrayWritable(List<String> values) {
        super(Text.class);
        Text[] writableValues = new Text[values.size()];
        for (int i = 0; i < values.size(); i++) {
            writableValues[i] = new Text(values.get(i));
        }
        set(writableValues);
    }
    
    public TextArrayWritable(String[] values) {
        super(Text.class);
        Text[] writableValues = new Text[values.length];
        for (int i = 0; i < values.length; i++) {
            writableValues[i] = new Text(values[i]);
        }
        set(writableValues);
    }
}