package ru.yaal.project.habrahabr.saver;

import org.testng.annotations.Test;

import java.io.File;

public class HabrahabrArticleSaverTest {
    /**
     * Если параметры не указаны, бросается исключение
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void argsNull() {
        HabrahabrArticleSaver.main(null);
    }

    /**
     * Если параметры не указаны, бросается исключение
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void argsEmpty() {
        HabrahabrArticleSaver.main(new String[]{});
    }

    /**
     * Если целевая папка не существует, бросается исключение
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void destFolderNotExists() {
        HabrahabrArticleSaver.main(new String[]{"c:/notExists"});
    }

    /**
     * Если целевая папка является файлом, бросается исключение
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void destFolderIsFile() throws Exception {
        String notDirectory = File.createTempFile("habrsaver_", ".tmp").getAbsolutePath();
        HabrahabrArticleSaver.main(new String[]{notDirectory, "123456"});
    }
}
