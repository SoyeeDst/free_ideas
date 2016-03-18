package com.freelancer.common.metrics.metric;

import com.freelancer.common.MonitorSensor;
import com.freelancer.common.cache.LRUMapCache;
import com.freelancer.common.metrics.render.MeasureReadableRender;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Soyee.Deng on 2016/3/17.
 *
 * Metric basic templates containing all the common information
 */
public abstract class MeasurableMetric {

    private static final String DEFAULT_METRIC_NAME = "MetricNoName";
    private static final Integer DEFAULT_SAMPLE_INTERVAL = 5;
    private static final Integer DEFAULT_SAMPLE_DELAY = 2;

    private String metricName;
    // Delay to trigger the first sampling to give the system to be prepared fully
    private Integer delay = DEFAULT_SAMPLE_DELAY;
    // Information gather interval
    private Integer interval;
    // Current data stuff
    private Object dataStuff;
    // Recent 100 sample data for later graph
    private LRUMapCache<Long, Object> stats = new LRUMapCache<>(100);
    private MeasureReadableRender measureReadableRender;
    private MonitorSensor monitorSensor;

    public MeasurableMetric(MonitorSensor monitorSensor, MeasureReadableRender measureReadableRender) {
        this(DEFAULT_METRIC_NAME, monitorSensor, measureReadableRender);
    }

    public MeasurableMetric(String metricName, MonitorSensor monitorSensor, MeasureReadableRender measureReadableRender) {
        this(metricName, monitorSensor, measureReadableRender, DEFAULT_SAMPLE_INTERVAL);
    }

    public MeasurableMetric(String metricName, MonitorSensor monitorSensor, MeasureReadableRender measureReadableRender, int interval) {
        this.metricName = metricName;
        this.monitorSensor = monitorSensor;
        this.measureReadableRender = measureReadableRender;
        this.interval = interval;
    }

    public MeasurableMetric setDelay(Integer delay) {
        this.delay = delay;
        return this;
    }

    // Start working to collect data
    public void triggerSampling() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                dataStuff = sample();
                Object object = measureReadableRender.renderStuff(dataStuff);
                if (object != null) {
                    stats.put(System.currentTimeMillis(), object);
                }
            }
        }, delay, interval, TimeUnit.SECONDS);
    }

    /**
     * Please override this function to attempt to gather different types of statistics with different approaches.
     * But please note that this sample process should be finished within {@code MeasurableMetric#DEFAULT_SAMPLE_INTERVAL}, otherwise
     * it might delay the following retriggering tick time.
     *
     * @return
     */
    protected abstract Object sample();

}
