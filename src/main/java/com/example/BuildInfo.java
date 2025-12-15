package com.example;

import java.io.InputStream;
import java.util.jar.Manifest;
import java.util.jar.Attributes;

public class BuildInfo {
    public static void printBuildInfo() {
        try {
            InputStream is = BuildInfo.class.getClassLoader()
                    .getResourceAsStream("META-INF/MANIFEST.MF");
            if (is != null) {
                Manifest manifest = new Manifest(is);
                Attributes attr = manifest.getMainAttributes();

                System.out.println("=== Build Info ===");
                System.out.println("Title: " + attr.getValue("Implementation-Title"));
                System.out.println("Version: " + attr.getValue("Implementation-Version"));
                System.out.println("Vendor: " + attr.getValue("Implementation-Vendor"));
                System.out.println("Git Commit: " + attr.getValue("Git-Commit"));
                System.out.println("Git Branch: " + attr.getValue("Git-Branch"));
                System.out.println("Git Commit Time: " + attr.getValue("Git-Commit-Time"));
                System.out.println("==================");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
