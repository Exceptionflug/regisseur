package de.exceptionflug.regisseur.util;

import com.comphenix.protocol.ProtocolLibrary;
import de.exceptionflug.regisseur.packetwrapper.WrapperPlayServerPosition;
import de.exceptionflug.regisseur.path.Position;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;

/**
 * Date: 16.08.2021
 *
 * @author Exceptionflug
 */
public final class Utils {

    private static int calls;
    private Utils() {}

    public static void teleport(Plugin plugin, Player player, Position position) {
        WrapperPlayServerPosition packet = new WrapperPlayServerPosition();
        packet.setX(position.x);
        packet.setY(position.y);
        packet.setZ(position.z);
        packet.setYaw(position.yaw);
        packet.setPitch(position.pitch);
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet.getHandle());
        } catch (InvocationTargetException e) {
        }

        calls ++;
        if (calls % 50 == 0) {
            Bukkit.getScheduler().callSyncMethod(plugin, () -> player.teleportAsync(position.location(player.getWorld())));
        }
    }

}
