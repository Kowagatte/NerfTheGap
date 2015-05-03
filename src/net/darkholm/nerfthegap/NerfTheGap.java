package net.darkholm.nerfthegap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.UUID;

/**
 * A plugin that adds a configurable cooldown to God apple consumption
 *
 * @author Kowagatte
 */
public class NerfTheGap extends JavaPlugin implements Listener {

    /*
    In Java 7 and later version, new ArrayList<UUID>() can simply be written new ArrayList<>() but in order to
    provide compatibility with Java 6 and earlier versions, this is not done here.
     */
    private HashSet<UUID> cooldown = new HashSet<UUID>();

    private int cooldownDuration = 60; // The length of the cooldown, in seconds. 60 by default.

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Imports nodes into the config.yml if empty; otherwise, does not edit the config file
        reloadConfig(); // Loads the config from its YAML file
        cooldownDuration = getConfig().getInt("cooldown-seconds");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onGappleUse(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 1) {
            if (this.cooldown.contains(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot eat another God Apple yet.");
            }
        }
    }

    @EventHandler
    public void onGappleConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 1) {
            final UUID uuid = event.getPlayer().getUniqueId();
            this.cooldown.add(uuid);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    Player player = getServer().getPlayer(uuid);
                    if (player != null)
                        player.sendMessage(ChatColor.DARK_AQUA + "You can now eat a God Apple again.");
                    NerfTheGap.this.cooldown.remove(uuid);
                }
            }, cooldownDuration * 20L);
        }
    }
}
