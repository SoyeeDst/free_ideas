package com.freelancer.common;

import com.freelancer.common.metrics.metric.MeasurableMetric;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soyee.Deng on 2016/3/17.
 */
public class MonitorSensor {

    private List<MeasurableMetric> metricList = new ArrayList<>();

    public void registerMetrics(MeasurableMetric metrics) {
        metricList.add(metrics);
    }

    public void startSample() {
        for (MeasurableMetric metric : metricList) {
            metric.triggerSampling();
        }
    }

}
