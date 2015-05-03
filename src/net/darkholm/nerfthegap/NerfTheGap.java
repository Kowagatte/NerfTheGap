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

import java.util.ArrayList;

/**
 * A plugin that adds a cooldown to God apple consumption
 *
 * @author Kowagatte
 */
public class NerfTheGap extends JavaPlugin implements Listener {

    /*
    In Java 7 and later version, new ArrayList<Player>() can simply be written new ArrayList<>() but in order to
    provide compatibility with Java 6 and earlier versions, this is not done here.
     */
    private ArrayList<Player> cooldown = new ArrayList<Player>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onGappleUse(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 1) {
            if (this.cooldown.contains(event.getPlayer())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot eat another God Apple yet.");
            }
        }
    }

    @EventHandler
    public void onGappleConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 1) {
            final Player player = event.getPlayer();
            this.cooldown.add(player);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    player.sendMessage(ChatColor.DARK_AQUA + "You can now eat a God Apple again.");
                    NerfTheGap.this.cooldown.remove(player);
                }
            }, 1200L);
        }
    }
}
