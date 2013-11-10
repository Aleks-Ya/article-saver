package ru.yaal.project.articlesaver.ui;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Аппендер Log4j, пишущий в текстовый блок swing.
 * User: Aleks
 * Date: 10.11.13
 */
public class GuiAppender extends AppenderSkeleton{
    //todo дописать
    @Override
    protected void append(LoggingEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean requiresLayout() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
