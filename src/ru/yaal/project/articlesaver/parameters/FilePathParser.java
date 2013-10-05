package ru.yaal.project.articlesaver.parameters;

import java.io.File;

/**
 * Обрабатывает спецсимволы из пути к целевой папке,
 * прочитанному из файла свойств.
 * User: Aleks
 * Date: 05.10.13
 */
public class FilePathParser {
    public static File parse(String path) {
        path = path.replaceAll("\t", "\\\\t");
        path = path.replaceAll("\f", "\\\\f");
        path = path.replaceAll("\n", "\\\\n");
        path = path.replaceAll("\r", "\\\\r");
        path = path.replaceAll("\u0000", "");
        return new File(path);
    }
}
