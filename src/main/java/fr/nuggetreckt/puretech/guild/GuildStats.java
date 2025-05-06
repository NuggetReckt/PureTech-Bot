package fr.nuggetreckt.puretech.guild;

public class GuildStats {

    private int messageCount;
    String lastMessage;

    public GuildStats() {
        this.messageCount = 0;
        this.lastMessage = "";
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int count) {
        messageCount = count;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String message) {
        lastMessage = message;
    }
}
