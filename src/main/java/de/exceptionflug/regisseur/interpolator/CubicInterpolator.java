package de.exceptionflug.regisseur.interpolator;

import de.exceptionflug.regisseur.path.PolarCoordinates;
import de.exceptionflug.regisseur.path.Position;
import de.exceptionflug.regisseur.path.Vector3D;

public final class CubicInterpolator
		implements PositionInterpolator, PolarCoordinatesInterpolator {

	public static final CubicInterpolator instance = new CubicInterpolator();

	private CubicInterpolator() {
	}

	@Override
	public void interpolatePosition(final PositionBuilder builder, final Position y0, final Position y1,
									final Position y2, final Position y3, final double step) {
		builder.setPosition(new Vector3D(InterpolationUtils.cubic_catmull(y0.x, y1.x, y2.x, y3.x, step),
				InterpolationUtils.cubic_catmull(y0.y, y1.y, y2.y, y3.y, step),
				InterpolationUtils.cubic_catmull(y0.z, y1.z, y2.z, y3.z, step)));
	}

	@Override
	public void interpolatePolarCoordinates(final PositionBuilder builder, final Position y0, final Position y1,
			final Position y2, final Position y3, final double step) {
		builder.setPolarCoordinates(
				new PolarCoordinates(InterpolationUtils.cubic(y0.pitch, y1.pitch, y2.pitch, y3.pitch, (float) step),
						InterpolationUtils.cubic(y0.yaw, y1.yaw, y2.yaw, y3.yaw, (float) step)));
	}

}
