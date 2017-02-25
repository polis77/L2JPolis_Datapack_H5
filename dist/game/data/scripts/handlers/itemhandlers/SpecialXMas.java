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
package handlers.itemhandlers;

import com.polis.gameserver.handler.IItemHandler;
import com.polis.gameserver.model.actor.L2Playable;
import com.polis.gameserver.model.items.instance.L2ItemInstance;
import com.polis.gameserver.network.SystemMessageId;
import com.polis.gameserver.network.serverpackets.ShowXMasSeal;

/**
 * @author devScarlet, mrTJO
 */
public class SpecialXMas implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		playable.broadcastPacket(new ShowXMasSeal(item.getId()));
		return true;
	}
}
