package com.freelancer.common.metrics.render;

import com.freelancer.common.metrics.render.util.GenericRenderHelper;
import com.freelancer.common.metrics.vo.VMData;

/**
 * Created by Soyee.Deng on 2016/3/17.
 */
public class VMDataReadableRender implements MeasureReadableRender {

    @Override
    public Object renderStuff(Object rawStuff) {
        if (rawStuff == null) {
            return null;
        }
        if (!(rawStuff instanceof VMData)) {
            throw new IllegalArgumentException("Wrong type captured");
        }
        GenericRenderHelper.stdoutRenderObject(rawStuff);
        return rawStuff;
    }
}
