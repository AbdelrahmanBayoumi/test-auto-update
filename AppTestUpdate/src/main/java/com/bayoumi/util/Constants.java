package com.bayoumi.util;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Constants {
    // Program characteristics
    public static String assetsPath;
    public final static String APP_NAME = "AppTestUpdate";
    public final static String VERSION = "1.0.2";

    static {
        try {
            if (Files.isWritable(Paths.get(Constants.class.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
                assetsPath = "jarFiles";
            } else {
                assetsPath = System.getenv("LOCALAPPDATA") + "/" + Constants.APP_NAME + "/jarFiles";
            }
        } catch (Exception ex) {
            Logger.error(ex.getLocalizedMessage(), ex, Constants.class.getName() + " -> static init");
        }
    }
}
