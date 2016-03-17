package com.freelancer.common.metrics.parser;

/**
 * Created by Soyee.Deng on 2016/3/17.
 */
public interface MeasureReadableParser<T> {

    /**
     * Converter to parse raw data towards indicated Type
     * @param rawStuff
     * @return
     */
    public T parseStuff(Object rawStuff);
}
