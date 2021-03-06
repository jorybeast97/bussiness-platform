package com.fanhao.businessplatform.utils;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.util.StringUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取两个时间值差值并变为 XXX小时XX分钟的状态
     * @return
     */
    public static String getPeriodFromDate(Long time) {
        long hour = 1000 * 60 * 60;
        long minute = 1000 * 60;
        long second = 1000;
        String result = "";
        int temp = 0;
        while (time >= hour) {
            temp += time / hour;
            time = time % hour;
        }
        result = result + temp + "小时";
        temp = 0;
        while (time  >= minute) {
            temp += time / minute;
            time = time % minute;
        }
        result = result + temp + "分钟";
        return result;
    }

    //date转为String
    public static String parseStringFromDate(final Date date,
                                             final String form) {
        SimpleDateFormat format = new SimpleDateFormat(form);
        return format.format(date);
    }

    //String转为Date
    public static Date parseDateFromString(final String dateStr) {
        if (StringUtils.isEmpty(dateStr)) return null;
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取CPU占用率
    private static OperatingSystemMXBean cpuInfo = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    public static String cpuLoad() {
        double cpuLoad = cpuInfo.getSystemCpuLoad();
        int percentCpuLoad = (int) (cpuLoad * 100);
        return percentCpuLoad+"%";
    }

    //获取内存占用率
    private static OperatingSystemMXBean memoryInfo = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    public static String memoryLoad() {
        double totalvirtualMemory = memoryInfo.getTotalPhysicalMemorySize();
        double freePhysicalMemorySize = memoryInfo.getFreePhysicalMemorySize();
        double value = freePhysicalMemorySize/totalvirtualMemory;
        int percentMemoryLoad = (int) ((1-value)*100);
        return percentMemoryLoad+"%";
    }

    //获取JDK版本
    public static String jdkInfo() {
        String jdkInfo = System.getProperty("java.runtime.version");
        return "JDK:" + jdkInfo;
    }
}
