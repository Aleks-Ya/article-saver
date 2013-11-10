package ru.yaal.project.articlesaver.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Обработчик события "Выйти из приложения".
 * User: Aleks
 * Date: 10.11.13
 */
public class ExitActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        //todo сохранить properties
        System.exit(0);
    }
}
