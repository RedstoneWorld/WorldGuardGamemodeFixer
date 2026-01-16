package de.redstoneworld.worldguardgamemodefixer.listener;

import de.redstoneworld.worldguardgamemodefixer.GameModeFlagScanner;
import de.redstoneworld.worldguardgamemodefixer.WorldGuardGamemodeFixer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

	protected final WorldGuardGamemodeFixer plugin;
	private Player player;

	// config options
	// ...

	public PlayerListener(WorldGuardGamemodeFixer plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		// initialize config options:
		// ...
	}


	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		player = event.getPlayer();
		
		if (hasBypassPermissions(player)) return;
		
		setGameMode();
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		player = event.getPlayer();
		
		if (hasBypassPermissions(player)) return;
		
		setGameMode();
	}
	
	private void setGameMode() {

		new BukkitRunnable() {
			public void run() {

				GameMode targetGameMode = GameModeFlagScanner.getFinalGameMode(player);
				
				if (targetGameMode == null) return;
				
				if (!player.getGameMode().equals(targetGameMode)) {
					player.setGameMode(targetGameMode);
					plugin.getLogger().info("Gamemode change in world '" + player.getLocation().getWorld().getName() + "' for player " 
							+ player.getName() + ". New Gamemode is now " + targetGameMode + ".");
				} else {
					plugin.getLogger().info("No gamemode change in world '" + player.getLocation().getWorld().getName() + "' for player " 
							+ player.getName() + ". Gamemode ist already " + targetGameMode + ".");
				}

			}
		}.runTaskLater(plugin, 20);
		
	}
	
	// Permission checks:
	
    /**
     * This method checks if the player has a bypass permission to
     * exempt him out of the gamemode update.
     */
    public static boolean hasBypassPermissions(Player player) {

        if (!player.hasPermission("rwm.worldguardgamemodefixer.bypass")) {
            return false;
        }
        return true;
    }
	
}