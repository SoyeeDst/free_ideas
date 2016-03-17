package com.freelancer.common.metrics.render;

/**
 * Created by Soyee.Deng on 2016/3/17.
 */
public interface MeasureReadableRender {

    /**
     * Converter to parse raw data towards indicated Type
     * @param rawStuff
     * @return
     */
    public Object renderStuff(Object rawStuff);
}
