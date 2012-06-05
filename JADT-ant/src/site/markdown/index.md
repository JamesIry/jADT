JADT Ant Module
===============

To use [JADT](../index.html) in  [Apache Ant](http://ant.apache.org/), create a taskdef and task like in the following sample build.xml.

    <?xml version="1.0"?>

    <project name="JADTTaskExample" default="compile" basedir=".">
      <taskdef name="jadt" classname="pogofish.jadt.ant.JADTAntTask" classpath="directory that has JADT-core.jar and JADT-ant.jar/>

      <target name="compile" depeneds="generate">
        <!-- normal compile stuff -->
      </target>
      
      <target name="generate">
        <jadt srcFile="full path of input file" destDir = "base directory for Java output"/>
      </target>
    </project>

To generate from the shell see the [Core module](../core/index.html).
