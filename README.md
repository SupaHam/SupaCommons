# Supa Commons [![Build Status](https://ci.drtshock.net/buildStatus/icon?job=SupaCommons)](https://ci.drtshock.net/job/SupaCommons/)
SupaHam's Commons contains a bunch of utilities. This software is always being added to.

# Maven
    <project>
        ...
        <repositories>
            <repository>
                <id>drtshock-repo</id>
                <url>http://ci.drtshock.net/plugin/repository/everything</url>
            </repository>
        </repositories>
        <dependencies>
            <dependency>
                <groupId>com.supaham.commons</groupId>
                <artifactId>commons-core</artifactId>
                <version>0.1-SNAPSHOT</version> <!-- Change this value to whichever version you wish to depend on. -->
            </dependency>
        </dependencies>
        <!-- If you want to shade this dependency into your project, use the next piece of code -->
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>2.1</version>
                <configuration>
                    <artifactSet>
                        <includes>
                            <include>com.supaham.commons:commons-core</include>
                        </includes>
                    </artifactSet>
                </configuration>
            </plugin>
        </plugins>
    </project>

# Gradle
    dependencies {
        compile group: 'com.supaham.commons:commons-core:0.1-SNAPSHOT' // Change the last section to whichever version you wish to depend on.
    }
    // If you want to shade this dependency into your project, Utilize the ShadowJar plugin like so:
    shadowJar {
        dependencies {
            include(dependency('com.supaham.commons:commons-core'))
        }
    }

# Disclaimer
This project gives you no guarantee that it will not break in the future. Any APIs provided can 
very much change in a blink of an eye!

---

I wrote a post about this which you can find [here](http://supaham.com/supacommons-java-and-bukkit-commons-library/).
