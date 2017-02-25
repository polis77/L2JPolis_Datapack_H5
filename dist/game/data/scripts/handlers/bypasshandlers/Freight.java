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
package handlers.bypasshandlers;

import com.polis.gameserver.handler.IBypassHandler;
import com.polis.gameserver.model.actor.L2Character;
import com.polis.gameserver.model.actor.instance.L2PcInstance;
import com.polis.gameserver.model.itemcontainer.PcFreight;
import com.polis.gameserver.model.items.instance.L2ItemInstance;
import com.polis.gameserver.network.SystemMessageId;
import com.polis.gameserver.network.serverpackets.PackageToList;
import com.polis.gameserver.network.serverpackets.WareHouseWithdrawalList;

/**
 * @author UnAfraid
 */
public class Freight implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"package_withdraw",
		"package_deposit"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		if (command.equalsIgnoreCase(COMMANDS[0]))
		{
			PcFreight freight = activeChar.getFreight();
			if (freight != null)
			{
				if (freight.getSize() > 0)
				{
					activeChar.setActiveWarehouse(freight);
					for (L2ItemInstance i : activeChar.getActiveWarehouse().getItems())
					{
						if (i.isTimeLimitedItem() && (i.getRemainingTime() <= 0))
						{
							activeChar.getActiveWarehouse().destroyItem("L2ItemInstance", i, activeChar, null);
						}
					}
					activeChar.sendPacket(new WareHouseWithdrawalList(activeChar, WareHouseWithdrawalList.FREIGHT));
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.NO_ITEM_DEPOSITED_IN_WH);
				}
			}
		}
		else if (command.equalsIgnoreCase(COMMANDS[1]))
		{
			if (activeChar.getAccountChars().size() < 1)
			{
				activeChar.sendPacket(SystemMessageId.CHARACTER_DOES_NOT_EXIST);
			}
			else
			{
				activeChar.sendPacket(new PackageToList(activeChar.getAccountChars()));
			}
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
