package net.endoedgar;

import java.util.Set;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Writable;

// Class example taken from
// https://stackoverflow.com/questions/23209174/converting-mapwritable-to-a-string-in-hadoop
public class MyMapWritable extends MapWritable {
	@Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Set<Writable> keySet = this.keySet();

        for (Object key : keySet) {
            result.append("{" + key.toString() + " = " + this.get(key) + "}");
        }
        return result.toString();
    }
}
