package com.ioannuwu.inline.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class SettingsState implements Serializable {

    public int numberOfWhitespaces = DefaultSettings.NUMBER_OF_WHITESPACES; // 0 .. 10

    public EffectType effectType = DefaultSettings.EFFECT_TYPE;

    public SeverityLevelState error = DefaultSettings.ERROR;
    public SeverityLevelState warning = DefaultSettings.WARNING;
    public SeverityLevelState weakWarning = DefaultSettings.WEAK_WARNING;
    public SeverityLevelState information = DefaultSettings.INFORMATION;
    public SeverityLevelState serverError = DefaultSettings.SERVER_ERROR;
    public SeverityLevelState otherError = DefaultSettings.OTHER_ERROR;

    public String[] ignoreList = DefaultSettings.IGNORE_LIST;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingsState that = (SettingsState) o;
        return numberOfWhitespaces == that.numberOfWhitespaces && Objects.equals(error, that.error) &&
                Objects.equals(warning, that.warning) && Objects.equals(weakWarning, that.weakWarning) &&
                Objects.equals(information, that.information) && Objects.equals(serverError, that.serverError) &&
                Objects.equals(otherError, that.otherError) && Arrays.equals(ignoreList, that.ignoreList) &&
                Objects.equals(effectType, that.effectType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(numberOfWhitespaces, error, warning, weakWarning, information, serverError, otherError, effectType);
        result = 31 * result + Arrays.hashCode(ignoreList);
        return result;
    }

    @Override
    public String toString() {
        return "\nSettingsState{" +
                "\nnumberOfWhitespaces=" + numberOfWhitespaces +
                "\neffectType=" + effectType +
                "\nerror=" + error +
                "\nwarning=" + warning +
                "\nweakWarning=" + weakWarning +
                "\ninformation=" + information +
                "\nserverError=" + serverError +
                "\notherError=" + otherError +
                "\nignoreList=" + Arrays.toString(ignoreList) +
                '}';
    }
}