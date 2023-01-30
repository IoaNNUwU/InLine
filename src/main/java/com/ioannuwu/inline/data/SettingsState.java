package com.ioannuwu.inline.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class SettingsState implements Serializable {

    public int numberOfWhitespaces = DefaultSettings.NUMBER_OF_WHITESPACES; // 0 .. 10

    public int border = DefaultSettings.BORDER; // 0 .. 10

    public SeverityLevelState error = DefaultSettings.ERROR;
    public SeverityLevelState warning = DefaultSettings.WARNING;
    public SeverityLevelState weak_warning = DefaultSettings.WEAK_WARNING;
    public SeverityLevelState information = DefaultSettings.INFORMATION;
    public SeverityLevelState serverError = DefaultSettings.SERVER_ERROR;
    public SeverityLevelState otherError = DefaultSettings.OTHER_ERROR;

    public String[] ignoreList = DefaultSettings.IGNORE_LIST;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingsState that = (SettingsState) o;
        return numberOfWhitespaces == that.numberOfWhitespaces && Objects.equals(error, that.error) && Objects.equals(warning, that.warning) && Objects.equals(weak_warning, that.weak_warning) && Objects.equals(information, that.information) && Objects.equals(serverError, that.serverError) && Objects.equals(otherError, that.otherError) && Arrays.equals(ignoreList, that.ignoreList) && border == that.border;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(numberOfWhitespaces, error, warning, weak_warning, information, serverError, otherError, border);
        result = 31 * result + Arrays.hashCode(ignoreList);
        return result;
    }
}