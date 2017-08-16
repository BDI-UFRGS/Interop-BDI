package interop.log.model;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * @author Lucas Hagen
 */

public class LogValuesIterator implements Iterator<LogValues> {

    private LogValues[] values;
    private int i;

    public LogValuesIterator(TreeMap<Float, Float> map) {
        values = new LogValues[map.size()];

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public LogValues next() {
        return null;
    }

    @Override
    public void remove() {

    }

    @Override
    public void forEachRemaining(Consumer action) {

    }

}
