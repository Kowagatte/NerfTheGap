package net.darkholm.nerfthegap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * A plugin that adds a cooldown to God apple consumption
 *
 * @author Kowagatte
 */
public class NerfTheGap
        extends JavaPlugin
        implements Listener
{
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    ArrayList<Player> Cooldown = new ArrayList();

    @EventHandler
    public void onGappleUse(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if (((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_AIR)) &&
                (p.getItemInHand() != null) &&
                (p.getItemInHand().equals(new ItemStack(Material.GOLDEN_APPLE, p.getItemInHand().getAmount(), (short)1))) &&
                (this.Cooldown.contains(p)))
        {
            e.setCancelled(true);
            p.sendMessage("You Cannot eat another God Apple yet.");
        }
    }

    @EventHandler
    public void onGappleConsume(PlayerItemConsumeEvent e)
    {
        final Player p = e.getPlayer();
        if (e.getItem().equals(new ItemStack(Material.GOLDEN_APPLE, p.getItemInHand().getAmount(), (short)1)))
        {
            p.sendMessage("You have eaten a God Apple");
            this.Cooldown.add(p);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
            {
                public void run()
                {
                    p.sendMessage("You can now eat a God Apple again.");
                    NerfTheGap.this.Cooldown.remove(p);
                }
            }, 1200L);
        }
    }
}
