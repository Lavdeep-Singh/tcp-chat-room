package com.chat;
public class Main {
    public static void main(String[] args) {
        System.err.println("Initializing");
    }
}

/*

javac -d out
    javac: Java compiler.
    -d out: Specifies the output directory for .class files. All compiled files will go into the out/ directory while maintaining the package structure (out/com/chat/...).

java -cp out com.chat.Server
    java: Runs the JVM.
    -cp out: Specifies the class path as the out directory.
    com.chat.Server: Fully qualified class name (packageName.className)
 */

// javac -d out src/main/java/com/chat/*.java
// java -cp out com.chat.Server
// java -cp out com.chat.Client