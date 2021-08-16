package de.exceptionflug.regisseur.interpolator;

import de.exceptionflug.regisseur.path.Position;
import de.exceptionflug.regisseur.path.Vector3D;

public class TargetInterpolator implements PolarCoordinatesInterpolator {

	protected final Vector3D target;

	public TargetInterpolator(final Vector3D target) {
		this.target = target;
	}

	@Override
	public void interpolatePolarCoordinates(final PositionBuilder builder, final Position y0, final Position y1,
											final Position y2, final Position y3, final double step) {
		// 1.62 is the default height for player eye positions
		builder.setPolarCoordinates(builder.getPosition().addY(1.62).lookAt(this.target));
	}

}
