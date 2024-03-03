package com.sulphate.chatcolor2.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class ChatColorEvent extends Event implements Cancellable {

    private final HandlerList handlerList;
    private final Player player;
    private final String message;
    private final String colour;
    private final AsyncPlayerChatEvent chatEvent;

    private boolean cancelled;

    public ChatColorEvent(Player player, String message, String colour, AsyncPlayerChatEvent chatEvent) {
        super(true);

        handlerList = new HandlerList();
        cancelled = false;

        this.player = player;
        this.message = message;
        this.colour = colour;
        this.chatEvent = chatEvent;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

    public String getColour() {
        return colour;
    }

    public AsyncPlayerChatEvent getChatEvent() {
        return chatEvent;
    }

}
