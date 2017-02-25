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
package handlers.targethandlers;

import com.polis.gameserver.handler.ITargetTypeHandler;
import com.polis.gameserver.model.L2Object;
import com.polis.gameserver.model.actor.L2Character;
import com.polis.gameserver.model.skills.Skill;
import com.polis.gameserver.model.skills.targets.L2TargetType;

/**
 * Target Servitor handler.
 * @author Zoey76
 */
public class Servitor implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		if (activeChar.hasServitor())
		{
			return new L2Character[]
			{
				activeChar.getSummon()
			};
		}
		return EMPTY_TARGET_LIST;
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.SERVITOR;
	}
}
