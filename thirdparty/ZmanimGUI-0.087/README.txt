# About:
ZmanimGUI is a simple frontend for the 
Zmanim Java API - (http://www.kosherjava.com/zmanim-project/ ).

The GUI was written by Moshe Wagner
<moshe.wagner@gmail.com>

And is (like the API itself) - 
Licensed under GPLv2.
( See "License.txt" for detailed license information ).

# Running:
To run ZmanimGUI you must have the Java 6 runtime library (JRE 6) installed.
( It can be found here: http://www.java.com/en/download/index.jsp )

On windows you may be able to run it simply by double clicking the "ZmanimGUI.jar" file.

If that does not work, or for other operating systems, you should type this in the command line, from the directory that the file "ZmanimGUI.jar" is at:
"java -jar ZmanimGUI.jar"
(Make sure the "lib" directory is in the same dir as "ZmanimGUI.jar", or it will not run)


# Compiling from source:
In case you have made changes in the source code (found in the "src" directory), you must have Java SDK 6 installed (can be found here: http://java.sun.com/javase/downloads/index.jsp) ,
and then you can compile a new jar file, doing the following steps* (though the command line) :

- Go to the "src" folder
Run:
- "javac -classpath lib\zmanim.jar zmanimgui\*.java"
- "jar cfm ZmanimGUI.jar Manifest.txt zmanimgui\*.class"

The output Jar "ZmanimGUI.jar" now works with your new code.
(To run this one too, you must have the "lib" dir in the same dir as the jar file).

* - NOTE: the slash direction must fit your system, "/" for POSIX systems (Linux, Mac OS X, etc') but "/" for Windows systems.