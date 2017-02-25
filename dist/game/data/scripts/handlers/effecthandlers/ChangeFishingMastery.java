package handlers.effecthandlers;

import com.polis.gameserver.model.StatsSet;
import com.polis.gameserver.model.conditions.Condition;
import com.polis.gameserver.model.effects.AbstractEffect;

/**
 * Change Fishing Mastery dummy effect implementation.
 * @author Zoey76
 */
public final class ChangeFishingMastery extends AbstractEffect
{
	public ChangeFishingMastery(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params)
	{
		super(attachCond, applyCond, set, params);
	}
}
