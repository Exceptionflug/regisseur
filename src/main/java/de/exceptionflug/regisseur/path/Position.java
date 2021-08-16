package de.exceptionflug.regisseur.path;

import org.bukkit.Location;
import org.bukkit.World;

public final class Position extends Vector3D {

	public final float pitch;
	public final float yaw;

	public Position(final double x, final double y, final double z, final float pitch, final float yaw) {
		super(x, y, z);
		this.pitch = pitch;
		this.yaw = yaw;
	}

	@Override
	public String toString() {
		return super.toString() + padding + this.pitch + padding + this.yaw + padding;
	}

	public static Position fromString(final String input) {
		final String[] parts = input.split(padding);
		if (parts.length != 5) {
			return null;
		}

		try {
			final double x = Double.parseDouble(parts[0]);
			final double y = Double.parseDouble(parts[1]);
			final double z = Double.parseDouble(parts[2]);
			final float pitch = Float.parseFloat(parts[3]);
			final float yaw = Float.parseFloat(parts[4]);

			return new Position(x, y, z, pitch, yaw);
		} catch (final NumberFormatException e) {
			return null;
		}
	}

	public Location location(World world) {
		return new Location(world, x, y, z, yaw, pitch);
	}
}
