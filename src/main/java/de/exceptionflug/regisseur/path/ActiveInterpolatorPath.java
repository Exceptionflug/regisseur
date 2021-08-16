package de.exceptionflug.regisseur.path;

import de.exceptionflug.regisseur.Cutscene;
import de.exceptionflug.regisseur.interpolator.Interpolator;
import de.exceptionflug.regisseur.util.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public final class ActiveInterpolatorPath extends ActivePath {

	private final Interpolator interpolator;
	private final List<Player> players;
	private final long iterations;
	private final Cutscene cutscene;

	private long currentIteration;

	public ActiveInterpolatorPath(Cutscene cutscene, Interpolator interpolator, long iterations) {
		this.players = cutscene.players();
		this.iterations = iterations;
		this.interpolator = interpolator;
		this.cutscene = cutscene;
		players.forEach(player -> Utils.teleport(cutscene.plugin(), player, this.interpolator.getPoint(0)));
	}

	@Override
	public void tick() {
		this.currentIteration++;

		final Position pos = this.interpolator.getPoint(this.currentIteration / (double) this.iterations);

		players.forEach(player -> Utils.teleport(cutscene.plugin(), player, pos));

		if (this.currentIteration >= this.iterations) {
			stop(cutscene);
		}
	}

}
