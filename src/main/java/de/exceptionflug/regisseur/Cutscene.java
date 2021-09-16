package de.exceptionflug.regisseur;

import de.exceptionflug.regisseur.interpolator.*;
import de.exceptionflug.regisseur.path.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Date: 16.08.2021
 *
 * @author Exceptionflug
 */
public class Cutscene {

    private static final Timer TIMER = new Timer();
    private final List<Position> points = new ArrayList<>();
    private final List<PathChangeListener> listeners = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private final Map<UUID, GameMode> gameModeMap = new HashMap<>();
    private final Map<UUID, Location> locationMap = new HashMap<>();
    private final Plugin plugin;
    private Entity cameraEntity;
    private Vector3D target;
    private ActivePath activePath;
    private boolean debug;

    public Cutscene(Plugin plugin) {
        this.plugin = plugin;
    }

    public void startTravelling(long frames, World world) {
        Position[] pathCopy = waypoints();
        boolean cmovLinear = pathCopy.length == 2;

        cameraEntity = world.spawnEntity(points.get(0).location(world), EntityType.BEE, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
            entity.setInvulnerable(true);
            entity.setGravity(false);
            entity.setCustomNameVisible(false);
            entity.setSilent(true);
            if (entity instanceof Mob) {
                if (!debug) {
                    ((Mob) entity).setInvisible(true);
                }
                ((Mob) entity).setAI(false);
            }
        });

        PositionInterpolator a = cmovLinear ? LinearInterpolator.instance : CubicInterpolator.instance;
        PolarCoordinatesInterpolator b = target == null ? cmovLinear ? LinearInterpolator.instance : CubicInterpolator.instance : new TargetInterpolator(target);
        if (debug) {
            System.out.println("Waypoints: " + pathCopy.length+", Frames: " + frames);
            System.out.println("Interpolator: " + a.getClass().getSimpleName() + ", Polar Interpolator: " + b.getClass().getSimpleName());
        }

        activePath = new ActiveInterpolatorPath(this, new Interpolator(pathCopy, a, b), frames);

        players.forEach(player -> {
            gameModeMap.put(player.getUniqueId(), player.getGameMode());
            locationMap.put(player.getUniqueId(), player.getLocation());
        });

        if (!debug) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                players.forEach(player -> {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.setSpectatorTarget(cameraEntity);
                });
            }, 10);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isTravelling()) {
                    cancel();
                    return;
                }
                activePath.tick();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void target(Vector3D target) {
        this.target = target;
    }

    public void removeTarget() {
        target = null;
    }

    public boolean hasTarget() {
        return target != null;
    }

    public void stopTravelling() {
        activePath = null;
        players.forEach(player -> {
            if (debug) {
                return;
            }
            player.setSpectatorTarget(null);
            player.setGameMode(gameModeMap.get(player.getUniqueId()));
            player.teleport(locationMap.get(player.getUniqueId()));
        });
        cameraEntity.remove();
    }

    public boolean isTravelling() {
        return activePath != null;
    }

    public void addPathChangeListener(PathChangeListener listener) {
        listeners.add(listener);
    }

    private boolean isInBounds(int index) {
        return index > -1 && index < points.size();
    }

    public void debug(boolean debug) {
        this.debug = debug;
    }

    public void waypoints(List<Position> points) {
        this.points.clear();
        this.points.addAll(points);
        pushChange();
    }

    private void pushChange() {
        for (PathChangeListener listener : listeners) {
            listener.onPathChange();
        }
    }

    public Position[] waypoints() {
        return points.toArray(new Position[0]);
    }

    public void clearWaypoints() {
        points.clear();
        pushChange();
    }

    public void addWaypoint(Position pos) {
        points.add(pos);
        pushChange();
    }

    public Position waypoint(int index) {
        if (isInBounds(index)) {
            return points.get(index);
        }
        return null;
    }

    public boolean removeLastWaypoint() {
        if (points.isEmpty()) {
            return false;
        }
        points.remove(points.size() - 1);
        pushChange();
        return true;
    }

    public boolean remove(int index) {
        if (isInBounds(index)) {
            points.remove(index);
            pushChange();
            return true;
        }
        return false;
    }

    public boolean insert(Position position, int index) {
        if (isInBounds(index)) {
            points.add(index, position);
            pushChange();
            return true;
        }
        return false;
    }

    public boolean replace(Position position, int index) {
        if (isInBounds(index)) {
            points.set(index, position);
            pushChange();
            return true;
        }
        return false;
    }

    public int waypointSize() {
        return points.size();
    }

    public List<Player> players() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Plugin plugin() {
        return plugin;
    }

    public Entity cameraEntity() {
        return cameraEntity;
    }

    public static void shutdown() {
        TIMER.cancel();
    }

}
