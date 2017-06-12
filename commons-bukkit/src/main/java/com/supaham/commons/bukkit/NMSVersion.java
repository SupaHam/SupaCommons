package com.supaham.commons.bukkit;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.utils.ReflectionUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://gist.github.com/SupaHam/dad1db6406596c5f8e4b221ff473831c
 *
 * @author SupaHam (https://github.com/SupaHam)
 */
public class NMSVersion implements Comparable<NMSVersion> {

    private static final Pattern VERSION_PATTERN = Pattern.compile("^v(\\d+)_(\\d+)_R(\\d+)");
    public static final NMSVersion V1_8_R1 = NMSVersion.fromString("v1_8_R1");
    public static final NMSVersion V1_8_R2 = NMSVersion.fromString("v1_8_R2");
    public static final NMSVersion V1_8_R3 = NMSVersion.fromString("v1_8_R3");
    public static final NMSVersion V1_9_R1 = NMSVersion.fromString("v1_9_R1");
    public static final NMSVersion V1_10_R1 = NMSVersion.fromString("v1_10_R1");
    public static final NMSVersion V1_11_R1 = NMSVersion.fromString("v1_11_R1");
    public static final NMSVersion V1_12_R1 = NMSVersion.fromString("v1_12_R1");
    
    public static final NMSVersion CURRENT = fromString(ReflectionUtils.getServerVersion());

    private final int major;
    private final int minor;
    private final int release;

    public static NMSVersion fromString(String string) {
        Preconditions.checkNotNull(string, "string cannot be null.");
        Matcher matcher = VERSION_PATTERN.matcher(string);
        Preconditions.checkArgument(matcher.matches(), string + " is not in valid version format. e.g. v1_10_R1");
        return new NMSVersion(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
    }

    private NMSVersion(int major, int minor, int release) {
        this.major = major;
        this.minor = minor;
        this.release = release;
    }
    
    public boolean isHigherThan(NMSVersion o) {
        return compareTo(o) > 0;
    }
    
    public boolean isHigherThanOrEqualTo(NMSVersion o) {
        return compareTo(o) >= 0;
    }
    
    public boolean isLowerThan(NMSVersion o) {
        return compareTo(o) < 0;
    }
    
    public boolean isLowerThanOrEqualTo(NMSVersion o) {
        return compareTo(o) <= 0;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getRelease() {
        return release;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NMSVersion that = (NMSVersion) o;
        return major == that.major &&
            minor == that.minor &&
            release == that.release;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(major, minor, release);
    }

    @Override
    public String toString() {
        return "v" + major + "_" + minor  + "_R" + release;
    }

    @Override
    public int compareTo(NMSVersion o) {
        if (major < o.major) {
            return -1;
        } else if (major > o.major) {
            return 1;
        } else { // equal major
            if (minor < o.minor) {
                return -1;
            } else if (minor > o.minor) {
                return 1;
            } else { // equal minor
                if (release < o.release) {
                    return -1;
                } else if (release > o.release) {
                    return 1;
                } else {
                    return 0; // o is the same version as this.
                }
            }
        }
    }
}
