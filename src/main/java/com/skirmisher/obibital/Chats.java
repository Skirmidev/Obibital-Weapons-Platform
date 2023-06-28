package com.skirmisher.obibital;

public enum Chats {
    SFW("sfw"),
    NSFW("nsfw"),
    EVENTS("events"),
    MOD("mod"),
    LOBBY("lobby"),
    ANNOUNCE("announce")
    ;

    private final String str;

    Chats(final String str) {
        this.str = str;
    }

    @Override
    public String toString(){
        return str;
    }

    public static Chats fromString(String text) {
        for (Chats i : Chats.values()) {
            if (i.toString().equalsIgnoreCase(text)) {
                return i;
            }
        }
        return null;
    }
}