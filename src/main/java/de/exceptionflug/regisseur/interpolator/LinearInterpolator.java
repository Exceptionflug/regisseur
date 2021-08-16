package de.exceptionflug.regisseur.interpolator;

import de.exceptionflug.regisseur.path.PolarCoordinates;
import de.exceptionflug.regisseur.path.Position;
import de.exceptionflug.regisseur.path.Vector3D;

public final class LinearInterpolator
		implements PositionInterpolator, PolarCoordinatesInterpolator {

	public static final LinearInterpolator instance = new LinearInterpolator();

	private LinearInterpolator() {
	}

	@Override
	public void interpolatePosition(final PositionBuilder builder, final Position y0, final Position y1,
									final Position y2, final Position y3, final double step) {
		builder.setPosition(new Vector3D(InterpolationUtils.linear(y1.x, y2.x, step),
				InterpolationUtils.linear(y1.y, y2.y, step), InterpolationUtils.linear(y1.z, y2.z, step)));
	}

	@Override
	public void interpolatePolarCoordinates(final PositionBuilder builder, final Position y0, final Position y1,
			final Position y2, final Position y3, final double step) {
		builder.setPolarCoordinates(new PolarCoordinates((float) InterpolationUtils.linear(y1.pitch, y2.pitch, step),
				(float) InterpolationUtils.linear(y1.yaw, y2.yaw, step)));
	}

}
