package com.freelancer.misc.crcbench.cases.rdparty;

import com.freelancer.misc.crcbench.source.Crc32;
import org.apache.commons.lang3.time.StopWatch;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

/**
 * Created by Soyee.Deng on 2016/3/17.
 */
public class ThroughputTest {

    private static final int WHOLE_ITERATION = 1000000;
    private static ByteBuffer byteBuffer;

    static {
        // Run after the class loader initialization
        byteBuffer = ByteBuffer.wrap("Hello world".getBytes());
    }

    public static void main(String []args) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int index = 0; index < WHOLE_ITERATION; index++) {
            Crc32 crc32 = new Crc32();
            crc32.update(byteBuffer.array(), 0, byteBuffer.capacity());
            crc32.getValue();
        }

        stopWatch.stop();

        System.err.println(WHOLE_ITERATION * 1000 / (stopWatch.getTime()));
    }
}
