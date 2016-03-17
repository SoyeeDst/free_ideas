package com.freelancer.common.metrics.render.util;

import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Soyee.Deng on 2016/3/17.
 */
public class GenericRenderHelper {

    private static final int MAX_COLUMN_LENGTH = 20;
    private static List<Class> primitiveTypes;

    static {
        primitiveTypes = new ArrayList<Class>();
        primitiveTypes.addAll(Arrays.asList(
                boolean.class, Boolean.class,
                byte.class, Byte.class,
                short.class, Short.class,
                char.class, Character.class,
                int.class, Integer.class,
                long.class, Long.class,
                float.class, Float.class,
                double.class, Double.class));
    }

    public static void stdoutRenderObject(Object object) {
        if (object == null) {
            return;
        }
        Field[] fields = object.getClass().getFields();
        if (fields != null && fields.length > 0) {
            StringBuffer infoBuffer = new StringBuffer("");
            // In order to force to next line
            appendEndOfLine(infoBuffer);
            // First Line
            infoBuffer.append(StringUtils.rightPad("=", MAX_COLUMN_LENGTH * fields.length, "="));
            appendEndOfLine(infoBuffer);
            for (Field field : fields) {
                infoBuffer.append(StringUtils.rightPad(field.getName(), MAX_COLUMN_LENGTH, ' '));
            }
            appendEndOfLine(infoBuffer);
            for (Field field : fields) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), object.getClass());
                    String propertyValue = getPropertyValue(pd, object);
                    if (propertyValue != null) {
                        infoBuffer.append(StringUtils.rightPad(propertyValue.toString(), MAX_COLUMN_LENGTH, ' '));
                    } else {
                        infoBuffer.append(StringUtils.rightPad("", MAX_COLUMN_LENGTH, ' '));
                    }
                } catch (Exception e) {
                    infoBuffer.append(StringUtils.rightPad("", MAX_COLUMN_LENGTH, ' '));
                }
            }
            System.err.println(infoBuffer.toString());
        }
    }

    public static String getPropertyValue(PropertyDescriptor pd, Object object) throws Exception {
        Object value = pd.getReadMethod().invoke(object);
        if (value == null) {
            return null;
        }
        if (primitiveTypes.contains(value.getClass())) {
            return value.toString();
        }
        // Note that it is not declared method
        Method toStringMethod = value.getClass().getMethod("toString");
        if (toStringMethod != null) {
            Object nestedValue = toStringMethod.invoke(value, toStringMethod);
            if (nestedValue != null) {
                return nestedValue.toString();
            }
            return null;
        }
        // Use the default impl
        return value.toString();
    }

    public static void appendEndOfLine(StringBuffer stringBuffer) {
        // Well known for Windows
        // It is \r\n, while it can not be identified by UNIX/Linux alike
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") >= 0) {
            stringBuffer.append("\r\n");
        } else {
            stringBuffer.append("\n");
        }
    }
}
