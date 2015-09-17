package com.supaham.commons.utils;

import com.sun.management.OperatingSystemMXBean;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServerConnection;


/**
 * Utility methods for runtime. This class contains methods such as {@link #getUsedMemory()},
 * {@link #getCPUUsage()}, and more.
 *
 * @since 0.3.6
 */
public class RuntimeUtils {

  private static OperatingSystemMXBean osMBean;

  /**
   * Returns the current amount of memory (in MB) this JVM is using.
   *
   * @return used memory
   */
  public static int getUsedMemory() {
    return getAllocatedMemory() - getFreeMemory();
  }

  /**
   * Returns the free amount of memory (in MB) belonging to JVM.
   *
   * @return free memory
   */
  public static int getFreeMemory() {
    return Math.round((float) (Runtime.getRuntime().freeMemory() / 1048576L));
  }

  /**
   * Returns the available maximum amount of memory (in MB) this JVM can use.
   *
   * @return maximum memory
   */
  public static int getMaximumMemory() {
    return Math.round((float) (Runtime.getRuntime().maxMemory() / 1048576L));
  }

  /**
   * Returns the allocated amount of memory (in MB) this JVM is using.
   *
   * @return allocated memory
   */
  public static int getAllocatedMemory() {
    return Math.round((float) (Runtime.getRuntime().totalMemory() / 1048576L));
  }

  /**
   * Returns the current CPU usage of this JVM alone. Not to be confused with the system's CPU
   * usage, available through {@link #getSystemCPUUsage()}.
   *
   * @return CPU usage
   */
  public static double getCPUUsage() {
    return Math.max(getOSMBean() == null ? -1 : getOSMBean().getProcessCpuLoad() * 100, 0);
  }

  /**
   * Returns the current CPU usage of this system alone. Not to be confused with the JVM's CPU
   * usage, available through {@link #getCPUUsage()}.
   *
   * @return CPU usage
   */
  public static double getSystemCPUUsage() {
    return Math.max(getOSMBean() == null ? -1 : getOSMBean().getSystemCpuLoad() * 100, 0);
  }

  private static OperatingSystemMXBean getOSMBean() {
    if (osMBean == null) {
      MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();
      try {
        osMBean = ManagementFactory
            .newPlatformMXBeanProxy(mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME,
                                    OperatingSystemMXBean.class);
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }
    return osMBean;
  }
}
