package handlers.effecthandlers;

import com.polis.gameserver.model.StatsSet;
import com.polis.gameserver.model.conditions.Condition;
import com.polis.gameserver.model.effects.AbstractEffect;
import com.polis.gameserver.model.skills.BuffInfo;

/**
 * Cubic Mastery effect implementation.
 * @author Zoey76
 */
public final class CubicMastery extends AbstractEffect
{
	private final int _cubicCount;
	
	public CubicMastery(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params)
	{
		super(attachCond, applyCond, set, params);
		
		_cubicCount = params.getInt("cubicCount", 0);
	}
	
	@Override
	public boolean canStart(BuffInfo info)
	{
		return (info.getEffector() != null) && (info.getEffected() != null) && info.getEffected().isPlayer();
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		final int cubicCount = info.getEffected().getActingPlayer().getStat().getMaxCubicCount() + _cubicCount;
		info.getEffected().getActingPlayer().getStat().setMaxCubicCount(cubicCount);
	}
	
	@Override
	public boolean onActionTime(BuffInfo info)
	{
		return info.getSkill().isPassive();
	}
	
	@Override
	public void onExit(BuffInfo info)
	{
		final int cubicCount = info.getEffected().getActingPlayer().getStat().getMaxCubicCount() - _cubicCount;
		if (cubicCount <= 0)
		{
			info.getEffected().getActingPlayer().getStat().setMaxCubicCount(0);
		}
		else
		{
			info.getEffected().getActingPlayer().getStat().setMaxCubicCount(cubicCount);
		}
	}
}
