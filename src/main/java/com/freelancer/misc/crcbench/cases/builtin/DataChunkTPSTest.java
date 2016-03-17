package com.freelancer.misc.crcbench.cases.builtin;

import com.freelancer.common.MonitorPlugin;
import com.freelancer.common.MonitorSensor;
import com.freelancer.common.metrics.metric.vmstat.VMStatMeasurableMetric;
import org.apache.commons.lang3.time.StopWatch;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

/**
 * Created by Soyee.Deng on 2016/3/17.
 */
public class DataChunkTPSTest  {

    private static final int WHOLE_ITERATION = 1000000;

    // Let's simulate a file with 1MB size
    private static final int BYTE_LENGTH = 1 * 1024 * 1024;
    private static ByteBuffer byteBuffer;

    private static MonitorPlugin monitorPlugin;

    static {
        // Run after the class loader initialization
        byteBuffer = ByteBuffer.allocate(BYTE_LENGTH);
    }

    public static void main(String []args) throws Exception {
        MonitorSensor monitorSensor = new MonitorSensor();
        monitorSensor.registerMetrics(new VMStatMeasurableMetric(monitorSensor));
        monitorPlugin = new MonitorPlugin(monitorSensor);
        new Thread(monitorPlugin).start();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        long lastWaterMark = System.currentTimeMillis();
        for (int index = 0; index < WHOLE_ITERATION; index++) {
            CRC32 crc32 = new java.util.zip.CRC32();
            crc32.update(byteBuffer.array(), 0, BYTE_LENGTH);
            crc32.getValue();

            if (index % 10000 == 0) {
                System.err.println("Avg time consumption should be : " + (System.currentTimeMillis() - lastWaterMark) / 1000.000);
                lastWaterMark = System.currentTimeMillis();
            }
        }

        stopWatch.stop();

        System.err.println(WHOLE_ITERATION * 1000 / (stopWatch.getTime()));
    }
}
