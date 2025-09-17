package dev.prodzeus.utilities.world;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;

/**
 * This class is a region/Cuboid from one location to another. It can be used for {@link Block}s protection and things like WorldEdit.<br><br>
 * <p>
 * This class is useful when needing to highlight or otherwise manage bigger areas. Contains most, if not all, methods needed for managing
 * a Cuboid area, regarding both, but not limited to, {@link Player}s and {@link org.bukkit.entity.Entity}s, as well as block iteration,
 * and location finding.
 *
 * @author Unknown
 * @author prodzeus
 */
@SuppressWarnings({"unused", "deprecation", "RedundantSuppression"})
@Getter
public final class Cuboid implements Iterable<Block>, Cloneable, ConfigurationSerializable, Serializable {

    /**
     * The name of the {@link World}.
     */
    private final String worldName;
    /**
     * The first point of the Cuboid.
     */
    private final double x1, y1, z1;
    /**
     * The second point of the Cuboid.
     */
    private final double x2, y2, z2;
    /**
     * The center of the Cuboid.
     */
    private final double centerX, centerY, centerZ;
    /**
     * The length of the side of the Cuboid.
     */
    private final double lengthX, lengthY, lengthZ;
    /**
     * The shortest distance in the Cuboid.
     */
    private final double shortestDistance;
    /**
     * The longest distance in the Cuboid.
     */
    private final double longestDistance;
    /**
     * The volume of the Cuboid.
     */
    private final double volume;
    /**
     * The border of the Cuboid.
     */
    private final WorldBorder border;

    /**
     * Construct a Cuboid in the world of the given world name and XYZ-coordinates.
     * @param worldName The world's name.
     * @param x1        X coordinate of the first corner.
     * @param y1        Y coordinate of the first corner.
     * @param z1        Z coordinate of the first corner.
     * @param x2        X coordinate of the second corner.
     * @param y2        Y coordinate of the second corner.
     * @param z2        Z coordinate of the second corner.
     */
    private Cuboid(String worldName, double x1, double y1, double z1, double x2, double y2, double z2) {
        this(null, worldName, x1, y1, z1, x2, y2, z2);
    }


    /**
     * Construct a Cuboid in the world of the given world name and XYZ-coordinates.
     * @param server The server instance, used for creating the world border.
     * @param worldName The world's name.
     * @param x1    X coordinate of the first corner.
     * @param y1    Y coordinate of the first corner.
     * @param z1    Z coordinate of the first corner.
     * @param x2    X coordinate of the second corner.
     * @param y2    Y coordinate of the second corner.
     * @param z2    Z coordinate of the second corner.
     */
    private Cuboid(@Nullable final Server server, String worldName, double x1, double y1, double z1, double x2, double y2, double z2) {
        this.worldName = worldName;
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
        this.centerX = (this.x1 + this.x2) / 2;
        this.centerY = (this.y1 + this.y2) / 2;
        this.centerZ = (this.z1 + this.z2) / 2;
        this.lengthX = (this.x2 - this.x1) + 1;
        this.lengthY = (this.y2 - this.y1) + 1;
        this.lengthZ = (this.z2 - this.z1) + 1;
        this.volume = this.lengthX * this.lengthY * this.lengthZ;
        this.shortestDistance = Math.min(Math.min(this.lengthX, this.lengthZ), this.lengthY);
        this.longestDistance = Math.max(Math.max(x2 - x1, z2 - z1), y2 - y1);
        if (server == null) this.border = null;
        else {
            final WorldBorder border = server.createWorldBorder();
            border.setCenter(centerX, centerZ);
            border.setSize(longestDistance);
            this.border = border;
        }
    }

    /**
     * Construct a Cuboid in the world of the given world name and XYZ-coordinates.
     * @param server    The server instance, used for creating the world border.
     * @param worldName The world's name.
     * @param x1        X coordinate of the first corner.
     * @param y1        Y coordinate of the first corner.
     * @param z1        Z coordinate of the first corner.
     * @param x2        X coordinate of the second corner.
     * @param y2        Y coordinate of the second corner.
     * @param z2        Z coordinate of the second corner.
     */
    public static Cuboid create(Server server,String worldName, double x1, double y1, double z1, double x2, double y2, double z2) {
        return new Cuboid(server, worldName, x1, y1, z1, x2, y2, z2);
    }

    /**
     * Construct a Cuboid in the world of the given world name and XYZ-coordinates.
     * @param worldName The world's name.
     * @param x1    X coordinate of the first corner.
     * @param y1    Y coordinate of the first corner.
     * @param z1    Z coordinate of the first corner.
     * @param x2    X coordinate of the second corner.
     * @param y2    Y coordinate of the second corner.
     * @param z2    Z coordinate of the second corner.
     */
    public static Cuboid create(String worldName, double x1, double y1, double z1, double x2, double y2, double z2) {
        return new Cuboid(worldName, x1, y1, z1, x2, y2, z2);
    }

    /**
     * Construct a Cuboid in the given world and XYZ-coordinates.
     * @param world The Cuboid's world.
     * @param x1    X coordinate of the first corner.
     * @param y1    Y coordinate of the first corner.
     * @param z1    Z coordinate of the first corner.
     * @param x2    X coordinate of the second corner.
     * @param y2    Y coordinate of the second corner.
     * @param z2    Z coordinate of the second corner.
     */
    public static Cuboid create(World world, double x1, double y1, double z1, double x2, double y2, double z2) {
        return new Cuboid(world.getName(), x1, y1, z1, x2, y2, z2);
    }

    /**
     * Construct a Cuboid given two location objects which represent any two corners of the Cuboid.
     * @param locationOne First corner.
     * @param locationTwo Second corner.
     * @apiNote The 2 locations provided must be in the same world.
     */
    public static Cuboid create(Location locationOne, Location locationTwo) {
        if (locationOne == null || locationTwo == null) {
            throw new IllegalArgumentException("Locations must not be null");
        }

        if (!locationOne.getWorld().equals(locationTwo.getWorld())) {
            throw new IllegalArgumentException("Locations must be on the same world");
        }

        return new Cuboid(locationOne.getWorld().getName(), locationOne.getBlockX(), locationOne.getBlockY(), locationOne.getBlockZ(), locationTwo.getBlockX(), locationTwo.getBlockY(), locationTwo.getBlockZ());
    }

    /**
     * Construct a One-Block Cuboid at the given location of the Cuboid.
     * @param location The location of the Cuboid.
     */
    public static Cuboid create(Location location) {
        return create(location, location);
    }

    /**
     * Copy the constructor of another Cuboid.
     * @param other The Cuboid to copy.
     */
    public static Cuboid create(Cuboid other) {
        if (other == null) {
            throw new IllegalArgumentException("Cuboid must not be null");
        }

        return new Cuboid(other.worldName, other.x1, other.y1, other.z1, other.x2, other.y2, other.z2);
    }

    /**
     * Construct a Cuboid using a map with the following keys:<br>
     * {@code worldName} {@code x1} {@code x2} {@code y1} {@code y2} {@code z1} {@code z2}
     * @param map The map of keys.
     */
    public static Cuboid create(Map<String, Object> map) {
        String worldName = (String) map.get("worldName");
        double x1 = (Integer) map.get("x1");
        double x2 = (Integer) map.get("x2");
        double y1 = (Integer) map.get("y1");
        double y2 = (Integer) map.get("y2");
        double z1 = (Integer) map.get("z1");
        double z2 = (Integer) map.get("z2");

        return new Cuboid(worldName, (int) x1, (int) y1, (int) z1, (int) x2, (int) y2, (int) z2);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("worldName", this.worldName);
        map.put("x1", this.x1);
        map.put("y1", this.y1);
        map.put("z1", this.z1);
        map.put("x2", this.x2);
        map.put("y2", this.y2);
        map.put("z2", this.z2);
        return map;
    }

    /**
     * Get the world of the Cuboid.
     * @return The world object representing this Cuboid's world.
     * @throws IllegalStateException If the world is not loaded.
     */
    public World getWorld() {
        World world = Bukkit.getWorld(this.worldName);
        if (world == null) {
            throw new IllegalStateException("World '%s' is not loaded!".formatted(this.worldName));
        }
        return world;
    }

    /**
     * Get the size of the Cuboid along the X-Axis.
     * @return Size of the Cuboid along the X-Axis.
     */
    public double getSizeX() {
        return this.lengthX;
    }

    /**
     * Get the size of the Cuboid along the Y-Axis.
     * @return Size of the Cuboid along the Y-Axis.
     */
    public double getSizeY() {
        return this.lengthY;
    }

    /**
     * Get the size of the Cuboid along the Z-Axis
     * @return Size of the Cuboid along the Z-Axis.
     */
    public double getSizeZ() {
        return this.lengthZ;
    }

    /**
     * Get the minimum X coordinate of the Cuboid.
     * @return The minimum X coordinate.
     */
    public double getLowerX() {
        return this.x1;
    }

    /**
     * Get the maximum X coordinate of the Cuboid.
     * @return The maximum X coordinate.
     */
    public double getUpperX() {
        return this.x2;
    }

    /**
     * Get the minimum Y coordinate of the Cuboid.
     * @return The minimum Y coordinate.
     */
    public double getLowerY() {
        return this.y1;
    }

    /**
     * Get the maximum Y coordinate of the Cuboid.
     * @return The maximum Y coordinate.
     */
    public double getUpperY() {
        return this.y2;
    }

    /**
     * Get the minimum Z coordinate of the Cuboid.
     * @return The minimum Z coordinate.
     */
    public double getLowerZ() {
        return this.z1;
    }

    /**
     * Get the maximum Z coordinate of the Cuboid.
     * @return The maximum Z coordinate.
     */
    public double getUpperZ() {
        return this.z2;
    }

    /**
     * Get the radius of X-Axis of the Cuboid.
     * @return The X radius.
     */
    public double getRadiusX() {
        return (getUpperX() - getLowerX()) / 2;
    }

    /**
     * Get the radius of Y-Axis of the Cuboid.
     * @return The Y radius.
     */
    public double getRadiusY() {
        return (getUpperY() - getLowerY()) / 2;
    }

    /**
     * Get the radius of Z-Axis of the Cuboid.
     * @return The Z radius.
     */
    public double getRadiusZ() {
        return (getUpperZ() - getLowerZ()) / 2;
    }

    /**
     * Get the diameter of X-Axis of the Cuboid.
     * @return The X radius.
     */
    public double getDiameterX() {
        return lengthX;
    }

    /**
     * Get the diameter of Y-Axis of the Cuboid.
     * @return The Y radius.
     */
    public double getDiameterY() {
        return lengthY;
    }

    /**
     * Get the diameter of Z-Axis of the Cuboid.
     * @return The Z radius.
     */
    public double getDiameterZ() {
        return lengthZ;
    }

    /**
     * Get the center of the X-Axis.
     * @return The center X coordinate.
     */
    public double getCenterX() {
        return this.centerX;
    }

    /**
     * Get the center of the Y-Axis.
     * @return The center Y coordinate.
     */
    public double getCenterY() {
        return this.centerY;
    }

    /**
     * Get the center of the Z-Axis.
     * @return The center Z coordinate.
     */
    public double getCenterZ() {
        return this.centerZ;
    }

    /**
     * Get the volume of the Cuboid.
     * @return The Cuboid volume, in blocks.
     */
    public double getVolume() {
        return this.volume;
    }

    /**
     * Calculates the squared distance between the given location and the nearest face of the Cuboid.
     * @param point The location to check.
     * @return      The squared distance to the nearest face of the Cuboid
     *              or null if the location is in a different world or otherwise invalid.
     */
    public Double getShortestSquaredDistanceToSurface(Location point) {
        if (point.getWorld() != getWorld()) return null;

        double px = point.getX(), py = point.getY(), pz = point.getZ();

        double dx = (px < x1) ? (x1 - px) : (px > x2) ? (px - x2) : 0;
        double dy = (py < y1) ? (y1 - py) : (py > y2) ? (py - y2) : 0;
        double dz = (pz < z1) ? (z1 - pz) : (pz > z2) ? (pz - z2) : 0;

        if (dx == 0 && dy == 0 && dz == 0) {
            return 0.0;
        }

        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Calculates the Euclidean distance using the squared distance method.
     * @param point The location to check.
     * @return      The true distance to the nearest face of the Cuboid or null if the location is invalid.
     */
    public Double getShortestDistanceToSurface(Location point) {
        Double squaredDistance = getShortestSquaredDistanceToSurface(point);
        return (squaredDistance != null) ? Math.sqrt(squaredDistance) : null;
    }

    /**
     * Get all the blocks present in the Cuboid of the given Material.
     * @return A list of all the matching blocks found.
     */
    public List<Block> getBlocks(Material material) {
        Iterator<Block> blockI = this.iterator();
        List<Block> copy = new ArrayList<>();
        try {
            while (blockI.hasNext()) {
                Block block = blockI.next();
                if (block.getType().equals(material)) {
                    copy.add(block);
                }
            }
        } catch (Exception ignored) {
        }
        return copy;
    }

    /**
     * Get all the blocks in the Cuboid.
     * @return A list of all blocks in the Cuboid.
     */
    public List<Block> getBlocks() {
        return getBlocks(true);
    }

    /**
     * Get all the blocks in the Cuboid.
     * @param includeAir Whether blocks of the type {@link Material#AIR} should be included in the list.
     * @return           A list of all blocks in the Cuboid.
     */
    public List<Block> getBlocks(final boolean includeAir) {
        Iterator<Block> blockI = this.iterator();
        List<Block> copy = new ArrayList<>();
        blockI.forEachRemaining(block -> {
            if (!includeAir && block.getType().equals(Material.AIR)) return;
            copy.add(block);
        });
        return copy;
    }

    /**
     * Get the center of the Cuboid.
     * @return The location at the center of the Cuboid.
     */
    public Location getCenter() {
        double x1 = this.getUpperX() - 0.0;
        double y1 = this.getUpperY() + 1;
        double z1 = this.getUpperZ() - 0.5;
        return new Location(this.getWorld(),
                this.getLowerX() + (x1 - this.getLowerX()) / 2.0,
                this.getLowerY() + (y1 - this.getLowerY()) / 2.0,
                this.getLowerZ() + (z1 - this.getLowerZ()) / 2.0);
    }

    /**
     * Get the Blocks at the eight corners of the Cuboid.
     * @return An array of Block objects representing the corners of the Cuboid.
     */
    public Block[] corners() {
        Block[] res = new Block[8];
        World w = this.getWorld();
        res[0] = w.getBlockAt((int) this.x1, (int) this.y1, (int) this.z1);
        res[1] = w.getBlockAt((int) this.x1, (int) this.y1, (int) this.z2);
        res[2] = w.getBlockAt((int) this.x1, (int) this.y2, (int) this.z1);
        res[3] = w.getBlockAt((int) this.x1, (int) this.y2, (int) this.z2);
        res[4] = w.getBlockAt((int) this.x2, (int) this.y1, (int) this.z1);
        res[5] = w.getBlockAt((int) this.x2, (int) this.y1, (int) this.z2);
        res[6] = w.getBlockAt((int) this.x2, (int) this.y2, (int) this.z1);
        res[7] = w.getBlockAt((int) this.x2, (int) this.y2, (int) this.z2);
        return res;
    }

    /**
     * Get the location of the lower South-West corner of the Cuboid.
     * @return The location of the lower South-West corner.
     */
    public Location getLowerSW() {
        return new Location(this.getWorld(), this.x2, this.y1, this.z2);
    }

    /**
     * Get the location of the upper South-West corner of the Cuboid.
     * @return The location of the upper South-West corner.
     */
    public Location getUpperSW() {
        return new Location(this.getWorld(), this.x2, this.y2, this.z2);
    }

    /**
     * Get the location of the lower South-East corner of the Cuboid.
     * @return The location of the lower South-East corner.
     */
    public Location getLowerSE() {
        return new Location(this.getWorld(), this.x1, this.y1, this.z2);
    }

    /**
     * Get the location of the upper South-East corner of the Cuboid.
     * @return The location of the upper South-East corner.
     */
    public Location getUpperSE() {
        return new Location(this.getWorld(), this.x1, this.y2, this.z2);
    }

    /**
     * Get the location of the lower North-West corner of the Cuboid.
     * @return The location of the lower North-West corner.
     */
    public Location getLowerNW() {
        return new Location(this.getWorld(), this.x2, this.y1, this.z1);
    }

    /**
     * Get the location of the upper North-West corner of the Cuboid.
     * @return The location of the upper North-West corner.
     */
    public Location getUpperNW() {
        return new Location(this.getWorld(), this.x2, this.y2, this.z1);
    }

    /**
     * Get the location of the lower North-East corner of the Cuboid.
     * @return The location of the lower North-East corner.
     */
    public Location getLowerNE() {
        return new Location(this.getWorld(), this.x1, this.y1, this.z1);
    }

    /**
     * Get the location of the upper North-East corner of the Cuboid.
     * @return The location of the upper North-East corner.
     */
    public Location getUpperNE() {
        return new Location(this.getWorld(), this.x1, this.y2, this.z1);
    }

    /**
     * Expand the Cuboid in the given {@link CuboidDirection} by the given distance. Negative distances will shrink the Cuboid in the given direction. Shrinking a
     * Cuboid's face past the opposite face is not an error and will return a valid Cuboid.
     * @param direction The direction in which to expand.
     * @param distance  The number of blocks by which to expand.
     * @return          A new Cuboid expanded by the given direction and distance.
     */
    public Cuboid expand(CuboidDirection direction, int distance) {
        return switch (direction) {
            case North -> new Cuboid(this.worldName, this.x1 - distance, this.y1, this.z1, this.x2, this.y2, this.z2);
            case South -> new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2 + distance, this.y2, this.z2);
            case East -> new Cuboid(this.worldName, this.x1, this.y1, this.z1 - distance, this.x2, this.y2, this.z2);
            case West -> new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + distance);
            case Down -> new Cuboid(this.worldName, this.x1, this.y1 - distance, this.z1, this.x2, this.y2, this.z2);
            case Up -> new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2 + distance, this.z2);
            default -> throw new IllegalArgumentException("Invalid direction: %s".formatted(direction.toString()));
        };
    }

    /**
     * Shift the Cuboid in the given direction by the given amount.
     * @param direction The direction in which to shift.
     * @param distance  The number of blocks by which to shift.
     * @return          A new Cuboid shifted by the given direction and amount.
     */
    public Cuboid shift(CuboidDirection direction, int distance) {
        return expand(direction, distance).expand(direction.opposite(), -distance);
    }

    /**
     * Outset (grow) the Cuboid in the given direction by the given amount.
     *
     * @param direction The direction in which to outset (must be Horizontal, Vertical, or Both).
     * @param distance  The number of blocks by which to outset.
     * @return A new Cuboid outset by the given direction and amount.
     */
    public Cuboid outset(CuboidDirection direction, int distance) {
        return switch (direction) {
            case Horizontal -> expand(CuboidDirection.North, distance)
                    .expand(CuboidDirection.South, distance)
                    .expand(CuboidDirection.East, distance)
                    .expand(CuboidDirection.West, distance);
            case Vertical -> expand(CuboidDirection.Down, distance)
                    .expand(CuboidDirection.Up, distance);
            case Both -> outset(CuboidDirection.Horizontal, distance)
                    .outset(CuboidDirection.Vertical, distance);
            default -> throw new IllegalArgumentException("Invalid direction: %s".formatted(direction.toString()));
        };
    }

    /**
     * Inset (shrink) the Cuboid in the given direction by the given amount. Equivalent to calling {@link Cuboid#inset} with a negative amount.
     *
     * @param direction The {@link CuboidDirection} in which to inset (must be Horizontal, Vertical, or Both).
     * @param distance  The number of blocks by which to inset.
     * @return A new Cuboid inset by the given direction and amount.
     */
    public Cuboid inset(CuboidDirection direction, int distance) {
        return this.outset(direction, -distance);
    }

    /**
     * Returns true if the point at (X,Y,Z) is contained within this Cuboid.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @return True if the given point is within this Cuboid, otherwise false.
     */
    public boolean contains(int x, int y, int z) {
        return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2 && z >= this.z1 && z <= this.z2;
    }

    /**
     * Check if the given Block is contained within this Cuboid.
     *
     * @param block The Block to check.
     * @return True if the Block is within this Cuboid, otherwise false.
     */
    public boolean contains(Block block) {
        return this.contains(block.getLocation());
    }

    /**
     * Check if the given location is contained within this Cuboid.
     *
     * @param location The location to check.
     * @return True if the location is within this Cuboid, otherwise false.
     */
    public boolean contains(Location location) {
        if (!this.worldName.equals(location.getWorld().getName())) {
            return false;
        }
        return this.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Check if the given player is inside the Cuboid.
     *
     * @param player The player.
     * @return True | False
     */
    public boolean isPlayerInside(final Player player) {
        return contains(player.getLocation());
    }

    /**
     * Removes all entities located inside the Cuboid.
     */
    public void removeEntities() {
        removeEntities(15);
    }

    /**
     * Removes all entities located inside the Cuboid.
     *
     * @param distance How many blocks the entities should be teleported away from the Cuboid.
     */
    public void removeEntities(final int distance) {
        final Location safeLocation = new Location(getWorld(), getUpperX(), getWorld().getHighestBlockAt((int) getUpperX() + distance, (int) getUpperZ()).getY(), getUpperZ() + distance);
        getCenter().getNearbyLivingEntities(getRadiusX(), getRadiusY(), getRadiusZ()).forEach(entity -> entity.teleport(safeLocation));
    }

    /**
     * Removes all players located inside the Cuboid.
     */
    public void removePlayers() {
        removePlayers(15);
    }

    /**
     * Removes all players located inside the Cuboid.
     *
     * @param distance How many blocks the players should be teleported away from the Cuboid.
     */
    public void removePlayers(final int distance) {
        final Location safeLocation = new Location(getWorld(), getUpperX(), getWorld().getHighestBlockAt((int) getUpperX() + distance, (int) getUpperZ()).getY(), getUpperZ() + distance);
        getCenter().getNearbyLivingEntities(getRadiusX(), getRadiusY(), getRadiusZ()).forEach(entity -> {
            if (entity instanceof Player player) player.teleport(safeLocation);
        });
    }

    /**
     * Get the average light level of all empty blocks ({@link Material#AIR}) in the Cuboid. Returns 0 if there are no empty blocks.
     *
     * @return The average light level of this Cuboid
     */
    public byte getAverageLightLevel() {
        long total = 0;
        int n = 0;
        for (Block b : this) {
            if (b.isEmpty()) {
                total += b.getLightLevel();
                ++n;
            }
        }
        return n > 0 ? (byte) (total / n) : 0;
    }

    /**
     * Contract the Cuboid, returning a Cuboid with any air around the edges removed, just large enough to include all non-air blocks.
     *
     * @return A new Cuboid with no external air blocks.
     */
    public Cuboid contract() {
        return this.contract(CuboidDirection.Down)
                .contract(CuboidDirection.South)
                .contract(CuboidDirection.East)
                .contract(CuboidDirection.Up)
                .contract(CuboidDirection.North)
                .contract(CuboidDirection.West);
    }

    /**
     * Contract the Cuboid in the given direction, returning a new Cuboid which has no exterior empty space.
     * E.g., a direction of {@link CuboidDirection#Down} will push the top face downwards as much as possible.
     *
     * @param direction The direction in which to contract.
     * @return A new Cuboid contracted in the given direction.
     */
    public Cuboid contract(CuboidDirection direction) {
        Cuboid face = getFace(direction.opposite());
        switch (direction) {
            case Down -> {
                while (face.containsOnly(0) && face.getLowerY() > this.getLowerY()) {
                    face = face.shift(CuboidDirection.Down, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, face.getUpperY(), this.z2);
            }
            case Up -> {
                while (face.containsOnly(0) && face.getUpperY() < this.getUpperY()) {
                    face = face.shift(CuboidDirection.Up, 1);
                }
                return new Cuboid(this.worldName, this.x1, face.getLowerY(), this.z1, this.x2, this.y2, this.z2);
            }
            case North -> {
                while (face.containsOnly(0) && face.getLowerX() > this.getLowerX()) {
                    face = face.shift(CuboidDirection.North, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, face.getUpperX(), this.y2, this.z2);
            }
            case South -> {
                while (face.containsOnly(0) && face.getUpperX() < this.getUpperX()) {
                    face = face.shift(CuboidDirection.South, 1);
                }
                return new Cuboid(this.worldName, face.getLowerX(), this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case East -> {
                while (face.containsOnly(0) && face.getLowerZ() > this.getLowerZ()) {
                    face = face.shift(CuboidDirection.East, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, face.getUpperZ());
            }
            case West -> {
                while (face.containsOnly(0) && face.getUpperZ() < this.getUpperZ()) {
                    face = face.shift(CuboidDirection.West, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, face.getLowerZ(), this.x2, this.y2, this.z2);
            }
            default -> throw new IllegalArgumentException("Invalid direction: %s".formatted(direction.toString()));
        }
    }

    /**
     * Get the Cuboid representing the face of this Cuboid. The resulting Cuboid will be one block thick in the axis perpendicular to the requested face.
     *
     * @param direction which face of the Cuboid to get.
     * @return The Cuboid representing this Cuboid's requested face.
     */
    public Cuboid getFace(CuboidDirection direction) {
        return switch (direction) {
            case Down -> new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y1, this.z2);
            case Up -> new Cuboid(this.worldName, this.x1, this.y2, this.z1, this.x2, this.y2, this.z2);
            case North -> new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x1, this.y2, this.z2);
            case South -> new Cuboid(this.worldName, this.x2, this.y1, this.z1, this.x2, this.y2, this.z2);
            case East -> new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z1);
            case West -> new Cuboid(this.worldName, this.x1, this.y1, this.z2, this.x2, this.y2, this.z2);
            default -> throw new IllegalArgumentException("Invalid direction: %s".formatted(direction.toString()));
        };
    }

    /**
     * Check if the Cuboid contains only blocks of the given type.
     *
     * @param blockId The block ID to check for.
     * @return True if this Cuboid contains only blocks of the given type, otherwise false.
     */
    private boolean containsOnly(int blockId) {
        for (Block b : this) {
            if (b.getType().getId() != blockId) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the given location is within range of this Cuboid.
     *
     * @param point The Location to check.
     * @param range The maximum distance to the Cuboid.
     * @return True if the location is within range of the Cuboid, otherwise false.
     */
    public boolean isPointInRange(final Location point, final double range) {
        Double squaredDistance = getShortestSquaredDistanceToSurface(point);
        return squaredDistance != null && squaredDistance <= range * range;
    }

    /**
     * Get the Cuboid big enough to hold both this Cuboid and the given one.
     *
     * @param other The other Cuboid.
     * @return A new Cuboid large enough to hold this Cuboid and the given Cuboid.
     */
    public @NotNull Cuboid getBoundingCuboid(@Nullable final Cuboid other) {
        if (other == null) {
            return this;
        }

        double xMin = Math.min(this.getLowerX(), other.getLowerX());
        double yMin = Math.min(this.getLowerY(), other.getLowerY());
        double zMin = Math.min(this.getLowerZ(), other.getLowerZ());
        double xMax = Math.max(this.getUpperX(), other.getUpperX());
        double yMax = Math.max(this.getUpperY(), other.getUpperY());
        double zMax = Math.max(this.getUpperZ(), other.getUpperZ());

        return new Cuboid(this.worldName, xMin, yMin, zMin, xMax, yMax, zMax);
    }

    /**
     * Get a block relative to the lower North-East point of the Cuboid.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @return The block at the given position.
     * @apiNote Repeated calls to this method will likely negatively impact performance, due to calling {@link Bukkit#getWorld} at each iteration.
     * @see Cuboid#getRelativeBlock(World, double, double, double) getRelativeBlock
     */
    public @NotNull Block getRelativeBlock(double x, double y, double z) {
        return this.getWorld().getBlockAt((int) (this.x1 + x), (int) (this.y1 + y), (int) (this.z1 + z));
    }

    /**
     * Get a block relative to the lower North-East point of the Cuboid in the given world.
     *
     * @param w The world.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @return The block at the given position.
     * @apiNote This version of {@link Cuboid#getRelativeBlock} is repeat-safe, and should be used if repeated calls are made, to avoid excessive calls to {@link Bukkit#getWorld}.
     */
    public Block getRelativeBlock(World w, double x, double y, double z) {
        return w.getBlockAt((int) (this.x1 + x), (int) (y1 + y), (int) (this.z1 + z));
    }

    /**
     * Get a list of the chunks which are fully or partially contained in this Cuboid.
     *
     * @return A list of Chunk objects.
     */
    public @NotNull List<Chunk> getChunks() {
        List<Chunk> res = new ArrayList<>();

        World w = this.getWorld();
        int x1 = ((int) this.getLowerX()) & ~0xf;
        int x2 = ((int) this.getUpperX()) & ~0xf;
        int z1 = ((int) this.getLowerZ()) & ~0xf;
        int z2 = ((int) this.getUpperZ()) & ~0xf;
        for (int x = x1; x <= x2; x += 16) {
            for (int z = z1; z <= z2; z += 16) {
                res.add(w.getChunkAt(x >> 4, z >> 4));
            }
        }
        return res;
    }

    public @NotNull Iterator<Block> iterator() {
        return new CuboidIterator(this.getWorld(), (int) this.x1, (int) this.y1, (int) this.z1, (int) this.x2, (int) this.y2, (int) this.z2);
    }

    @Override
    public Cuboid clone() {
        return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    @Override
    public String toString() {
        return "Cuboid{" +
                "worldName='" +
                worldName +
                '\'' +
                ", x1=" +
                x1 +
                ", y1=" +
                y1 +
                ", z1=" +
                z1 +
                ", x2=" +
                x2 +
                ", y2=" +
                y2 +
                ", z2=" +
                z2 +
                ", centerX=" +
                centerX +
                ", centerY=" +
                centerY +
                ", centerZ=" +
                centerZ +
                '}';
    }

    /**
     * Finds the first safe location in the Cuboid with one solid, non-passable block beneath and 2 air blocks above.
     *
     * @return The location of the non-passable block.
     */
    public @Nullable Location getSafeTeleportLocation() {
        for (int x = (int) x1; x <= x2; x++) {
            for (int z = (int) z1; z <= z2; z++) {
                for (int y = (int) y1; y <= y2; y++) {
                    if (!getWorld().getBlockAt(x, y, z).isPassable()
                            && getWorld().getBlockAt(x, y + 1, z).isPassable()
                            && getWorld().getBlockAt(x, y + 2, z).isPassable()) continue;
                    return new Location(getWorld(), x, y, z);
                }
            }
        }
        return null;
    }

    /**
     * Check if the Cuboid has a WorldBorder.
     * @return The WorldBorder.
     */
    public boolean hasWorldBorder() {
        return this.border != null;
    }

    /**
     * Get the border of the Cuboid.
     * @return The WorldBorder.
     */
    public @Nullable WorldBorder getWorldBorder() {
        return this.border;
    }

    /**
     * Set the warning time that causes the screen to be tinted red when a contracting border will reach the {@link Player} within the specified time.
     *
     * @param seconds The duration in seconds. (Default: 15 seconds)
     * @return The WorldBorder.
     */
    public @Nullable WorldBorder setWarningTime(final int seconds) {
        this.border.setWarningTime(seconds);
        return this.border;
    }

    /**
     * Set the warning distance that causes the screen to be tinted red when the {@link Player} is within the specified number of blocks from the border.
     *
     * @param distance The distance in blocks.
     * @return The WorldBorder.
     */
    public @Nullable WorldBorder setWarningDistance(final int distance) {
        this.border.setWarningDistance(distance);
        return this.border;
    }

    /**
     * Set the amount of damage a {@link Player} takes when traveling outside the border.
     *
     * @param amount The amount of damage.
     * @return The WorldBorder.
     */
    public @Nullable WorldBorder setDamageAmount(final double amount) {
        this.border.setDamageAmount(amount);
        return this.border;
    }

    /**
     * Set the distance of blocks a {@link Player} can travel outside the border before taking damage.
     *
     * @param distance The distance in blocks.
     * @return The WorldBorder.
     */
    public @Nullable WorldBorder setDamageBuffer(final double distance) {
        this.border.setDamageBuffer(distance);
        return this.border;
    }

    /**
     * Show the border for the given {@link Player}.
     *
     * @param player The player.
     */
    public void showBorder(@NotNull final Player player) {
        player.setWorldBorder(this.getWorldBorder());
    }

    /**
     * Show the border for the given {@link Player}s.
     *
     * @param players The players.
     */
    public void showBorder(@NotNull final Player... players) {
        for (Player player : players) showBorder(player);
    }

    public enum CuboidDirection {
        North,
        East,
        South,
        West,
        Up,
        Down,
        Horizontal,
        Vertical,
        Both,
        Unknown;

        public CuboidDirection opposite() {
            return switch (this) {
                case North -> South;
                case East -> West;
                case South -> North;
                case West -> East;
                case Horizontal -> Vertical;
                case Vertical -> Horizontal;
                case Up -> Down;
                case Down -> Up;
                case Both -> Both;
                default -> Unknown;
            };
        }

    }

    public static class CuboidIterator implements Iterator<Block> {
        private final World w;
        private final int baseX;
        private final int baseY;
        private final int baseZ;
        private final int sizeX;
        private final int sizeY;
        private final int sizeZ;
        private int x, y, z;

        public CuboidIterator(World w, int x1, int y1, int z1, int x2, int y2, int z2) {
            this.w = w;
            this.baseX = x1;
            this.baseY = y1;
            this.baseZ = z1;
            this.sizeX = Math.abs(x2 - x1) + 1;
            this.sizeY = Math.abs(y2 - y1) + 1;
            this.sizeZ = Math.abs(z2 - z1) + 1;
            this.x = this.y = this.z = 0;
        }

        public boolean hasNext() {
            return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
        }

        public Block next() {
            Block b = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
            if (++x >= this.sizeX) {
                this.x = 0;
                if (++this.y >= this.sizeY) {
                    this.y = 0;
                    ++this.z;
                }
            }
            return b;
        }

        @Override
        public void remove() {
        }
    }
}