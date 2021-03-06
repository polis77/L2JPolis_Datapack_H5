/*
 * Copyright (C) 2004-2014 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.effecthandlers;

import com.polis.gameserver.model.StatsSet;
import com.polis.gameserver.model.conditions.Condition;
import com.polis.gameserver.model.effects.AbstractEffect;
import com.polis.gameserver.model.holders.SkillHolder;
import com.polis.gameserver.model.skills.BuffInfo;

/**
 * Call Skill effect implementation.
 * @author Nos
 */
public final class CallSkill extends AbstractEffect
{
	private final SkillHolder _skill;
	
	public CallSkill(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params)
	{
		super(attachCond, applyCond, set, params);
		
		_skill = new SkillHolder(params.getInt("skillId"), params.getInt("skillLevel", 1));
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		info.getEffector().makeTriggerCast(_skill.getSkill(), info.getEffected(), true);
	}
}
