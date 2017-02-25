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
package gracia.AI.NPC.ZealotOfShilen;

import ai.npc.AbstractNpcAI;

import com.polis.gameserver.ai.CtrlIntention;
import com.polis.gameserver.datatables.SpawnTable;
import com.polis.gameserver.model.L2Spawn;
import com.polis.gameserver.model.actor.L2Attackable;
import com.polis.gameserver.model.actor.L2Character;
import com.polis.gameserver.model.actor.L2Npc;
import com.polis.gameserver.model.actor.instance.L2PcInstance;

/**
 * Zealot of Shilen AI.
 * @author nonom
 */
public final class ZealotOfShilen extends AbstractNpcAI
{
	// NPCs
	private static final int ZEALOT = 18782;
	private static final int[] GUARDS =
	{
		32628,
		32629
	};
	
	public ZealotOfShilen()
	{
		super(ZealotOfShilen.class.getSimpleName(), "gracia/AI/NPC");
		addSpawnId(ZEALOT);
		addFirstTalkId(GUARDS);
		
		for (int npcId : GUARDS)
		{
			for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(npcId))
			{
				L2Npc guard = spawn.getLastSpawn();
				guard.setIsInvul(true);
				((L2Attackable) guard).setCanReturnToSpawnPoint(false);
				startQuestTimer("WATCHING", 10000, guard, null, true);
			}
		}
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(ZEALOT))
		{
			spawn.getLastSpawn().setIsNoRndWalk(true);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("WATCHING") && !npc.isAttackingNow())
		{
			for (L2Character character : npc.getKnownList().getKnownCharacters())
			{
				if (character.isMonster() && !character.isDead() && !((L2Attackable) character).isDecayed())
				{
					npc.setRunning();
					((L2Attackable) npc).addDamageHate(character, 0, 999);
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, character, null);
				}
			}
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		return (npc.isAttackingNow()) ? "32628-01.html" : npc.getId() + ".html";
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		npc.setIsNoRndWalk(true);
		return super.onSpawn(npc);
	}
}
