package com.freelancer.common.metrics.metric.vmstat;

import com.freelancer.common.MonitorSensor;
import com.freelancer.common.metrics.metric.MeasurableMetric;
import com.freelancer.common.metrics.render.MeasureReadableRender;
import com.freelancer.common.metrics.render.VMDataReadableRender;
import com.freelancer.common.metrics.render.factory.MeasureReadableRenderFactory;
import com.freelancer.common.metrics.vo.VMData;
import com.sun.management.OperatingSystemMXBean;
import com.sun.management.ThreadMXBean;

import java.lang.management.ManagementFactory;

/**
 * Created by Soyee.Deng on 2016/3/17.
 */
public class VMStatMeasurableMetric extends MeasurableMetric {

    /**
     * Constructor
     *
     * @param monitorSensor
     */
    public VMStatMeasurableMetric(MonitorSensor monitorSensor) {
        super(monitorSensor, MeasureReadableRenderFactory.getVMStatsParser());
    }

    /**
     * Sample JVM statistics
     *
     * @return
     */
    protected VMData sample() {
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean systemMXBean = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();

        VMData vmData = new VMData();
        vmData.setTotalMemory(runtime.totalMemory());
        vmData.setFreeMemory(runtime.freeMemory());
        vmData.setMaxMemory(runtime.maxMemory());
        vmData.setFreePhysicalMemorySize(systemMXBean.getFreePhysicalMemorySize() / 1024);
        vmData.setTotalMemorySize(systemMXBean.getTotalPhysicalMemorySize() / 1024);
        vmData.setUsedMemory((systemMXBean.getTotalPhysicalMemorySize() - systemMXBean.getFreePhysicalMemorySize()) / 1024);

        return vmData;
    }
}
