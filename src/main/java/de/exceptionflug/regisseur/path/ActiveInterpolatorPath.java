package de.exceptionflug.regisseur.path;

import de.exceptionflug.regisseur.Cutscene;
import de.exceptionflug.regisseur.interpolator.Interpolator;
import org.bukkit.Bukkit;

public final class ActiveInterpolatorPath extends ActivePath {

	private final Interpolator interpolator;
	private final long iterations;
	private final Cutscene cutscene;

	private long currentIteration;

	public ActiveInterpolatorPath(Cutscene cutscene, Interpolator interpolator, long iterations) {
		this.iterations = iterations;
		this.interpolator = interpolator;
		this.cutscene = cutscene;
		cutscene.cameraEntity().teleport(this.interpolator.getPoint(1 / (double) this.iterations).location(cutscene.cameraEntity().getWorld()));
	}

	@Override
	public void tick() {
		this.currentIteration++;

		final Position pos = this.interpolator.getPoint(this.currentIteration / (double) this.iterations);

		cutscene.cameraEntity().teleport(pos.location(cutscene.cameraEntity().getWorld()));

		if (this.currentIteration >= this.iterations) {
			stop(cutscene);
		}
	}

}
