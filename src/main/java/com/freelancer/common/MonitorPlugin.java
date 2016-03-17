package com.freelancer.common;

/**
 * Created by Soyee.Deng on 2016/3/17.
 */
public class MonitorPlugin implements Runnable {

    private MonitorSensor monitorSensor;

    public MonitorPlugin(MonitorSensor monitorSensor) {
        this.monitorSensor = monitorSensor;
    }

    @Override
    public void run() {
        monitorSensor.startSample();
    }
}
