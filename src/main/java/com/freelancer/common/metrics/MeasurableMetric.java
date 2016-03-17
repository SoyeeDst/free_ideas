package com.freelancer.common.metrics;

import com.freelancer.common.MonitorSensor;
import com.freelancer.common.cache.LRUMapCache;
import com.freelancer.common.metrics.parser.MeasureReadableParser;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Soyee.Deng on 2016/3/17.
 *
 * Metric basic templates containing all the common information
 */
public abstract class MeasurableMetric<T> {

    private static final String DEFAULT_METRIC_NAME = "MetricNoName";
    private static final Integer DEFAULT_SAMPLE_INTERVAL = 5;
    private static final Integer DEFAULT_SAMPLE_DELAY = 2;

    private String metricName;
    // Delay to trigger the first sampling to give the system to be prepared fully
    private Integer delay = DEFAULT_SAMPLE_DELAY;
    // Information gather interval
    private Integer interval;
    private Object dataStuff;
    // Recent 100 sample data for later graph
    private LRUMapCache<Long, T> stats = new LRUMapCache<>(100);
    private MeasureReadableParser<T> measureReadableParser;
    private MonitorSensor monitorSensor;

    public MeasurableMetric(MonitorSensor monitorSensor, MeasureReadableParser<T> measureReadableParser) {
        this(DEFAULT_METRIC_NAME, monitorSensor, measureReadableParser);
    }

    public MeasurableMetric(String metricName, MonitorSensor monitorSensor, MeasureReadableParser<T> measureReadableParser) {
        this(metricName, monitorSensor, measureReadableParser, DEFAULT_SAMPLE_INTERVAL);
    }

    public MeasurableMetric(String metricName, MonitorSensor monitorSensor, MeasureReadableParser<T> measureReadableParser, int interval) {
        this.metricName = metricName;
        this.monitorSensor = monitorSensor;
        this.measureReadableParser = measureReadableParser;
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
                T object = measureReadableParser.parseStuff(dataStuff);
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
