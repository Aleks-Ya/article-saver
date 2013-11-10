package ru.yaal.project.articlesaver.article;

/**
 * Информация о состоянии процесса загрузки статей.
 * User: Aleks
 * Date: 10.11.13
 */
public interface ILoadingInfo {
    /**
     * Количество статей, ожидающих загрузки.
     */
    int waitForLoading();

    /**
     * Количество статей, загружаемых в настоящий момент.
     */
    int loadingNow();

    /**
     * Количество статей, загрузка которых завершена.
     */
    int hasLoaded();
}
