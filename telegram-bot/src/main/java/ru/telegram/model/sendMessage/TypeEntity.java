package ru.telegram.model.sendMessage;

public enum TypeEntity {
    BOT_COMMAND("bot_command");

    private final String command;

    TypeEntity(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
