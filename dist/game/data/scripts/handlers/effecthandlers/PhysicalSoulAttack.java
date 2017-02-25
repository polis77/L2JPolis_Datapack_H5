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

import com.polis.gameserver.enums.ShotType;
import com.polis.gameserver.model.StatsSet;
import com.polis.gameserver.model.actor.L2Character;
import com.polis.gameserver.model.conditions.Condition;
import com.polis.gameserver.model.effects.AbstractEffect;
import com.polis.gameserver.model.effects.L2EffectType;
import com.polis.gameserver.model.skills.BuffInfo;
import com.polis.gameserver.model.stats.BaseStats;
import com.polis.gameserver.model.stats.Formulas;
import com.polis.gameserver.network.SystemMessageId;
import com.polis.gameserver.network.serverpackets.SystemMessage;

/**
 * Physical Soul Attack effect implementation.
 * @author Adry_85
 */
public final class PhysicalSoulAttack extends AbstractEffect
{
	public PhysicalSoulAttack(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params)
	{
		super(attachCond, applyCond, set, params);
	}
	
	@Override
	public boolean calcSuccess(BuffInfo info)
	{
		return !Formulas.calcPhysicalSkillEvasion(info.getEffector(), info.getEffected(), info.getSkill());
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.PHYSICAL_ATTACK;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		L2Character target = info.getEffected();
		L2Character activeChar = info.getEffector();
		
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		if (((info.getSkill().getFlyRadius() > 0) || (info.getSkill().getFlyType() != null)) && activeChar.isMovementDisabled())
		{
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			sm.addSkillName(info.getSkill());
			activeChar.sendPacket(sm);
			return;
		}
		
		if (target.isPlayer() && target.getActingPlayer().isFakeDeath())
		{
			target.stopFakeDeath(true);
		}
		
		int damage = 0;
		boolean ss = info.getSkill().isPhysical() && activeChar.isChargedShot(ShotType.SOULSHOTS);
		final byte shld = Formulas.calcShldUse(activeChar, target, info.getSkill());
		// Physical damage critical rate is only affected by STR.
		boolean crit = false;
		if (info.getSkill().getBaseCritRate() > 0)
		{
			crit = Formulas.calcCrit(info.getSkill().getBaseCritRate() * 10 * BaseStats.STR.calcBonus(activeChar), true, target);
		}
		
		damage = (int) Formulas.calcPhysDam(activeChar, target, info.getSkill(), shld, false, ss);
		
		if ((info.getSkill().getMaxSoulConsumeCount() > 0) && activeChar.isPlayer())
		{
			// Souls Formula (each soul increase +4%)
			int chargedSouls = (activeChar.getActingPlayer().getChargedSouls() <= info.getSkill().getMaxSoulConsumeCount()) ? activeChar.getActingPlayer().getChargedSouls() : info.getSkill().getMaxSoulConsumeCount();
			damage *= 1 + (chargedSouls * 0.04);
		}
		if (crit)
		{
			damage *= 2;
		}
		
		if (damage > 0)
		{
			activeChar.sendDamageMessage(target, damage, false, crit, false);
			target.reduceCurrentHp(damage, activeChar, info.getSkill());
			target.notifyDamageReceived(damage, activeChar, info.getSkill(), crit, false);
			
			// Check if damage should be reflected
			Formulas.calcDamageReflected(activeChar, target, info.getSkill(), crit);
		}
		else
		{
			activeChar.sendPacket(SystemMessageId.ATTACK_FAILED);
		}
		
		if (info.getSkill().isSuicideAttack())
		{
			activeChar.doDie(activeChar);
		}
	}
}