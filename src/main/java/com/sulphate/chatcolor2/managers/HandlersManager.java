package com.sulphate.chatcolor2.managers;

import com.sulphate.chatcolor2.commands.Handler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HandlersManager {

    private final Map<Class<? extends Handler>, Handler> handlers;

    public HandlersManager() {
        handlers = new HashMap<>();
    }

    public void registerHandler(Class<? extends Handler> clazz, Handler handler) {
        handlers.put(clazz, handler);
    }

    public boolean callHandler(Class<? extends Handler> clazz, Player sender) {
        return handlers.get(clazz).handle(sender);
    }

}
