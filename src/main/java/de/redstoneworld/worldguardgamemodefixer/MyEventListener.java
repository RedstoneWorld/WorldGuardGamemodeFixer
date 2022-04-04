package de.redstoneworld.worldguardgamemodefixer;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MyEventListener implements Listener {

	protected final WorldGuardGamemodeFixer plugin;
	private Player player;

	// config options
	// ...

	public MyEventListener(WorldGuardGamemodeFixer plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		// initialize config options:
		// ...
	}


	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		player = event.getPlayer();

		new BukkitRunnable() {
			public void run() {

				GameMode gameMode = GameModeFlagScanner.getFinalGameMode(player);

				if (gameMode != null) {
					player.setGameMode(gameMode);
					plugin.getLogger().info("Gamemode change in world " + player.getLocation().getWorld() + " for player " + player.getName() + ": new GameMode is now " + gameMode);
				} else {
					plugin.getLogger().info("No gamemode change");
				}

			}
		}.runTaskLater(plugin, 20);
	}


	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		player = event.getPlayer();

		// -- old world handling
		// ...

		// -- new world handling with a start delay
		new BukkitRunnable() {
			public void run() {

				GameMode gameMode = GameModeFlagScanner.getFinalGameMode(player);

				if (gameMode != null) {
					player.setGameMode(gameMode);
					plugin.getLogger().info("Gamemode change in world " + player.getLocation().getWorld() + " for player " + player.getName() + ": new GameMode is now " + gameMode);
				} else {
					plugin.getLogger().info("No gamemode change");
				}

			}
		}.runTaskLater(plugin, 20);
	}

}