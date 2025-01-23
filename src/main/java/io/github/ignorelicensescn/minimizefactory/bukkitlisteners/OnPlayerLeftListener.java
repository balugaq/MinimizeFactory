package io.github.ignorelicensescn.minimizefactory.bukkitlisteners;

import io.github.ignorelicensescn.minimizefactory.utils.timestampbasedmanagers.PageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeftListener implements Listener {
    @EventHandler
    void onPlayerLeft(PlayerQuitEvent p){
        PageManager.removePlayer(p.getPlayer());
    }
}
