/**
 * This is a small plugin to update the gamemode of the
 * player after a world change, if he is on a WorldGuard
 * region with a separate gamemode flag. The gamemode is
 * fetched from the region with the highest priority.
 * It is executed after a few ticks delay during a
 * world change.
 * 
 * Permissions:
 * rwm.worldguardgamemodefixer.bypass
 * 
 * @author Robert Rauh alias RedstoneFuture
 */

package de.redstoneworld.worldguardgamemodefixer;

import de.redstoneworld.worldguardgamemodefixer.listener.DebugListener;
import de.redstoneworld.worldguardgamemodefixer.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardGamemodeFixer extends JavaPlugin {

	GameModeFlagScanner scanner;
	
	// hard-coded setting:
	public static boolean debugMode = false;
	
	
	public void onEnable() {

		// save default config and load config
		// ...
		
		// register events
		new PlayerListener(this);
		if (debugMode) new DebugListener(this);
		
		// initialize the gamemode flag scanner
		scanner = new GameModeFlagScanner(this);
		
	}

	public void onDisable() {
		
	}
	
}
