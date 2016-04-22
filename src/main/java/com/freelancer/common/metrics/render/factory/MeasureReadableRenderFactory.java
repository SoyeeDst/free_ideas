package com.freelancer.common.metrics.render.factory;

import com.freelancer.common.metrics.render.MeasureReadableRender;
import com.freelancer.common.metrics.render.VMDataReadableRender;
import com.freelancer.common.metrics.render.context.ReadableParserFactoryContext;

/**
 * Created by Soyee.Deng on 2016/3/17.
 *
 * Parser factory which contains some commonly used parser
 */
public class MeasureReadableRenderFactory {

    private static ReadableParserFactoryContext rpfContext;

    static {
        // Do some special initialization
        rpfContext = null;
    }

    public static MeasureReadableRender getVMStatsParser() {
        return new VMDataReadableRender();
    }

}