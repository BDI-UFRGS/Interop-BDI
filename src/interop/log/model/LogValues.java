/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.log.model;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An ArrayList of pairs of depth/log values.
 *
 * @author Luan
 */
public class LogValues extends TreeMap<Float, Float> {

    public LogValues() {
        super(Float::compareTo);
    }

    /**
     * Gets a LogValue (pair) based on it's index.
     *
     * @param index Index of the pair
     * @return LogValue (pair: depth, value)
     */
    public LogValue getPair(int index) {
        if (index >= size() || index < 0)
            return null;

        // use Iterator
        Iterator<Map.Entry<Float, Float>> it = entrySet().iterator();

        // skip to i
        for (int i = index; i > 0; --i) {
            it.next();
        }

        Map.Entry<Float, Float> p = it.next();

        return new LogValue(p.getKey(), p.getValue());
    }

    /**
     * @param startIndex
     * @param stopIndex
     * @return
     */
    public LogValues subMapByIndex(int startIndex, int stopIndex) {
        LogValues subMap = new LogValues();

        SortedMap<Float, Float> values = subMap(getPair(startIndex).getDepth(), getPair(stopIndex).getDepth());

        for (Float depth : values.keySet()) {
            subMap.put(depth, values.get(depth));
        }

        return subMap;
    }
}
