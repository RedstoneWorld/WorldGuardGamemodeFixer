package de.redstoneworld.worldguardgamemodefixer.listener;

import de.redstoneworld.worldguardgamemodefixer.WorldGuardGamemodeFixer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.ArrayList;

public class DebugListener implements Listener {
    

	protected final WorldGuardGamemodeFixer plugin;
	private Player player;

	// config options
	// ...

	public DebugListener(WorldGuardGamemodeFixer plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		// initialize config options:
		// ...
	}
    
    
    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        ArrayList<StackTraceElement> stackElements = new ArrayList<>();
        
        int i;
        for (i = 2; i < stackTrace.length; i++) {
            if (stackTrace[i].toString().matches(".+\\..+"))
                stackElements.add(stackTrace[i]);
        }

        plugin.getLogger().info("------------------------------------------------------");
        plugin.getLogger().info("Gamemode of user " + event.getPlayer().getName() + " changes to " + event.getNewGameMode().name() + "!");
        
        if (stackElements.isEmpty()) {
            plugin.getLogger().info("Changes via Minecraft (built-in)");
            
        } else {
            plugin.getLogger().info("Callstack Trace:");
            for (i = 0; i < stackElements.size(); i++) {
                
                String[] strSplit = stackElements.get(i).toString().split("//");
                if (strSplit.length < 2) continue;
                
                plugin.getLogger().info("Position " + i + " in call-stack:");
                plugin.getLogger().info(" - plugin name: " + strSplit[0]);
                plugin.getLogger().info(" - in Methode: " + strSplit[1]);
            }
        }
        
        plugin.getLogger().info("------------------------------------------------------");
    }
    
}
