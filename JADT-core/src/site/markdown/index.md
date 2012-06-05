JADT Core Module
=================================
This is the core module for [JADT](../index.html). It includes the core logic of JADT and a shell interface for turning JADT files into Java.

To generate from the shell use the executable jar

    ["path to java"]java -jar "path to JADT-core.jar" "path of input file" "base directory for output"

If for some inexplicable reason that doesn't work for you, then

    ["path to java"]java -cp "path to JADT-core.jar" pogofish.jadt.JADT "path of input file" "base directory for output"

To generate using an Apache Ant task see the [Ant module](../ant/index.html).
