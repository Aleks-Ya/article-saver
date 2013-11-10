package ru.yaal.project.articlesaver.ui;

import org.apache.log4j.Level;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

/**
 * Обработчик события смены уровня логирования.
 * User: Aleks
 * Date: 10.11.13
 */
public class LogLevelActionListener implements ActionListener {
    private final Properties props;

    public LogLevelActionListener(Properties props) {
        this.props = props;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        Level level = Level.toLevel(command);
        props.put("logLevel", level.toString());
        //todo дописать
    }
}
