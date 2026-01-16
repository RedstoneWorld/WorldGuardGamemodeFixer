package de.redstoneworld.worldguardgamemodefixer;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.gamemode.GameMode;
import com.sk89q.worldedit.world.gamemode.GameModes;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.entity.Player;

public class GameModeFlagScanner {

    private final WorldGuardGamemodeFixer plugin;

    public GameModeFlagScanner(WorldGuardGamemodeFixer plugin) {
        this.plugin = plugin;

    }
    
    
    /**
     * This method returns the Bukkit GameMode resulting from the 
     * WorldGuard flags based on the player and his position.
     * 
     * @param player (Player) the target player
     * @return the final bukkit gamemode
     */
    public static org.bukkit.GameMode getFinalGameMode(Player player) {

        if (hasBypassPermissions(player)) return null;

        // Get the converted bukkit objects and create the RegionManager.
        World bukkitWorld = BukkitAdapter.adapt(player.getWorld());
        BukkitPlayer bukkitPlayer = BukkitAdapter.adapt(player);
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(bukkitWorld);

        // Was the world of WorldGuard detected?
        if (manager == null) return null;
        
        // Get the set with all WorldGuard region.
        // See: https://worldguard.enginehub.org/en/latest/developer/regions/spatial-queries/#through-a-regionmanager
        ApplicableRegionSet set = manager.getApplicableRegions(bukkitPlayer.getBlockLocation().toVector().toBlockPoint());

        // See: https://worldguard.enginehub.org/en/latest/developer/regions/flag-calculation/#getting-one-value
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        GameMode gameMode = set.queryValue(localPlayer, Flags.GAME_MODE);
        
        if (gameMode == null) return null;
        
        if (gameMode.equals(GameModes.SURVIVAL)) {
            return org.bukkit.GameMode.SURVIVAL;
        } else if (gameMode.equals(GameModes.CREATIVE)) {
            return org.bukkit.GameMode.CREATIVE;
        } else if (gameMode.equals(GameModes.ADVENTURE)) {
            return org.bukkit.GameMode.ADVENTURE;
        } else if (gameMode.equals(GameModes.SPECTATOR)) {
            return org.bukkit.GameMode.SPECTATOR;
        } else {
            return null;
        }
    }
    
    /**
     * This method checks if the player has a bypass permission to
     * exempt him out of the gamemode update.
     */
    public static boolean hasBypassPermissions(Player p) {

        if (!p.hasPermission("rwm.worldguardgamemodefixer.bypass")) {
            return false;
        }
        return true;
    }
    
}
