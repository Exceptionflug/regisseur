package de.exceptionflug.regisseur.path;

import de.exceptionflug.regisseur.Cutscene;

public abstract class ActivePath {

	public abstract void tick();

	protected final void stop(Cutscene cutscene) {
		cutscene.stopTravelling();
	}

}
