package com.aspook.assists.util;

import java.io.File;

/**
 * Created by ASPOOK on 17/7/30.
 */

public class FileManager {
    private static final String DB_ROOT = "/data/data/com.aspook.assists/databases/";
    private static final String WEBVIEW_CACHE = "/data/data/com.aspook.assists/cache/";

    private FileManager() {
    }

    public static void clearWebViewCache() {
        deleteDir(WEBVIEW_CACHE);
        FileManager.deleteFile(FileManager.DB_ROOT + "webview.db");
        FileManager.deleteFile(FileManager.DB_ROOT + "webviewCookiesChromium.db");
    }

    private static boolean deleteFile(String dir) {
        try {
            File file = new File(dir);
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * will delete all files and dirs in it,including itself; could be interrupted
     *
     * @param dir
     */
    private static void deleteDir(String dir) {
        File file = new File(dir);
        deleteAllFiles(file);
    }

    /**
     * recursively delete files and dirs, could be interrupted
     *
     * @param file
     */
    private static void deleteAllFiles(File file) {
        if (file == null) {
            return;
        }
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                int len = files.length;
                for (int i = 0; i < len; i++) {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    deleteAllFiles(files[i]);
                }
            }
            file.delete();
        }
    }
}
