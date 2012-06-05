Downloading Precompiled Jars
============================
Eventually JADT jars will be hosted in a Maven repository. For now download JADT from its [Github download page](https://github.com/JamesIry/JADT/downloads). 

At minimum, you'll need JADT-core-_version_.jar.

If you're using Apache Ant you'll want JADT-ant-_version_.jar.

Source and javadoc jars may help in debugging issues, but probably aren't necessary for most people.

Building From Source
====================
Get familiar with [Github](https://help.github.com/categories/54/articles) and [Maven](http://maven.apache.org/guides/getting-started/index.html) then [fork](https://github.com/jamesiry/JADT/) and build.

    cd myprojectworkspace
    
    git clone https://github.com/JamesIry/JADT.git JADT  # to fork into myprojectworkspace/JADT
    
    cd JADT
    
    mvn compile # to compile into myprojectworkspace/JADT/JADT-core/target/classes 
                # and myprojectworkspace/JADT/JADT-ant/target/classes
                
    mvn test    # to run the tests
    
    mvn package # to create the jars in myprojectworkspace/JADT/JADT-core/target
                # and myprojectworkspace/JADT/JADT-ant/target

    mvn install # to install the jars in your local maven repository
                    
    mvn site:site site:stage # to create all this pretty documentation 
                             # at myprojectworkspace/JADT/target/staging/index.html
