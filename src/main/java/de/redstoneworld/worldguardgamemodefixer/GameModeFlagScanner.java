package de.redstoneworld.worldguardgamemodefixer;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.gamemode.GameMode;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;

public class GameModeFlagScanner {

    private final WorldGuardGamemodeFixer plugin;

    public GameModeFlagScanner(WorldGuardGamemodeFixer plugin) {
        this.plugin = plugin;

    }


    public static org.bukkit.GameMode getFinalGameMode(Player player) {

        if (hasBypassPermissions(player)) return null;

        // Get the converted bukkit objects and create the RegionManager.
        World bukkitWorld = BukkitAdapter.adapt(player.getWorld());
        BukkitPlayer bukkitPlayer = BukkitAdapter.adapt(player);
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(bukkitWorld);

        // Was the world of WorldGuard detect?
        if (manager == null) return null;

        // Get the set with all WorldGuard region.
        ApplicableRegionSet set = manager.getApplicableRegions(bukkitPlayer.getBlockLocation().toVector().toBlockPoint());

        // Region-Prioritization
        ProtectedRegion finalRegion = null;
        GameMode finalGameMode = null;
        int highestPriority = 0;
        for (ProtectedRegion region : set.getRegions()) {
            GameMode flagGameMode;
            if ((region.getPriority() > highestPriority || finalRegion == null)
                    && (flagGameMode = region.getFlag(Flags.GAME_MODE)) != null) {
                highestPriority = region.getPriority();
                finalRegion = region;
                finalGameMode = flagGameMode;
            }
        }

        // No region (maybe only "__global__") with a gamemode flag found.
        if (finalRegion == null) return null;

        // Get the gamemode.
        return org.bukkit.GameMode.valueOf(finalGameMode.getName().toUpperCase());

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
