package com.sulphate.chatcolor2.utils;

public enum Config {

    MAIN_CONFIG("config.yml"),
    MESSAGES("messages.yml"),
    GUI("gui.yml"),
    GROUPS("groups.yml"),
    CUSTOM_COLOURS("custom-colors.yml");

    private final String filename;

    Config(String fileName) {
        this.filename = fileName;
    }

    public String getFilename() {
        return filename;
    }

}
