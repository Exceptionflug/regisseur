package de.exceptionflug.regisseur.interpolator;

import de.exceptionflug.regisseur.path.PolarCoordinates;
import de.exceptionflug.regisseur.path.Position;
import de.exceptionflug.regisseur.path.Vector3D;

public final class PositionBuilder {

	private Vector3D position;
	private PolarCoordinates polarCoordinates;

	public PositionBuilder setPosition(final Vector3D position) {
		this.position = position;
		return this;
	}

	public PositionBuilder setPolarCoordinates(final PolarCoordinates polarCoordinates) {
		this.polarCoordinates = polarCoordinates;
		return this;
	}

	public Vector3D getPosition() {
		return this.position;
	}

	public PolarCoordinates getPolarCoordinates() {
		return this.polarCoordinates;
	}

	public Position build() {
		return new Position(this.position.x, this.position.y, this.position.z, this.polarCoordinates.pitch,
				this.polarCoordinates.yaw);
	}

}
