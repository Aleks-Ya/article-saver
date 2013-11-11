package ru.yaal.project.articlesaver.ui;

import javax.swing.*;

/**
 * Строка для вывода статистики (название параметра + значение).
 * User: a.yablokov
 * Date: 11.11.13
 */
public class StatisticsLabel extends JLabel {
    private final String text;

    public StatisticsLabel(String text) {
        this.text = text;
        setValue(0);
    }

    public void setValue(int value) {
        setText(text + value);
    }
}
