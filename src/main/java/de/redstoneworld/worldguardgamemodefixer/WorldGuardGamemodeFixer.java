/**
 *  This is a small plugin to update the gamemode of the
 *  player after a world change, if he is on a WorldGuard
 *  region with a separate gamemode flag. The gamemode is
 *  fetched from the region with the highest priority.
 *  It is executed after a few ticks delay during a
 *  world change.
 * 
 * Permissions:
 * rwm.worldguardgamemodefixer.bypass
 *
 * @author Robert Rauh alias RedstoneFuture
 */

package de.redstoneworld.worldguardgamemodefixer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class WorldGuardGamemodeFixer extends JavaPlugin {

	GameModeFlagScanner scanner;
	
	public void onEnable() {
		
        // register events
        new MyEventListener(this);

		// save default config
		// ...

		// initialize the gamemode flag scanner
		scanner = new GameModeFlagScanner(this);
		
	}

	public void onDisable() {
		
	}

	/**
	 * This method reads the specific messages in config.yml and replaces
	 * the minecraft color codes with a valid character.
	 * 
	 * @param key YAML key
	 * @param args placeholder without "%" and value for the placeholder
	 * 
	 * @return the config messages (String)
	 */
	String getLang(String key, String... args) {
		String lang = getConfig().getString("messages." + key, "&cUnknown language key &6" + key);
		for (int i = 0; i + 1 < args.length; i += 2) {
			lang = lang.replace("%" + args[i] + "%", args[i + 1]);
		}
		return ChatColor.translateAlternateColorCodes('&', lang);
	}
	
	boolean getBooleanOption(String key) {
		String option = getConfig().getString("features." + key);
		return Boolean.valueOf(option);
	}
	
	@SuppressWarnings("deprecation")
	void sendActionbarMessage(Player player, String message) {
		player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
		
	}

}
