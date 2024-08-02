package de.frxdy.frxdyutils.settings;

public class PluginSettings {

    public boolean isSendJoinMessage() {
        return sendJoinMessage;
    }

    public void setSendJoinMessage(boolean sendJoinMessage) {
        this.sendJoinMessage = sendJoinMessage;
    }

    boolean sendJoinMessage = true;

    public boolean isSendDamageNotification() {
        return sendDamageNotification;
    }

    public void setSendDamageNotification(boolean sendDamageNotification) {
        this.sendDamageNotification = sendDamageNotification;
    }

    boolean sendDamageNotification = true;

}
