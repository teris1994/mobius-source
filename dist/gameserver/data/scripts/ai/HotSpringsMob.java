/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ai;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

public class HotSpringsMob extends Mystic
{
	private static final int DeBuffs[] =
	{
		4554,
		4552
	};
	
	public HotSpringsMob(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance actor = getActor();
		if ((attacker != null) && Rnd.chance(5))
		{
			int DeBuff = DeBuffs[Rnd.get(DeBuffs.length)];
			List<Effect> effect = attacker.getEffectList().getEffectsBySkillId(DeBuff);
			if (effect != null)
			{
				int level = effect.get(0).getSkill().getLevel();
				if (level < 10)
				{
					effect.get(0).exit();
					Skill skill = SkillTable.getInstance().getInfo(DeBuff, level + 1);
					skill.getEffects(actor, attacker, false, false);
				}
			}
			else
			{
				Skill skill = SkillTable.getInstance().getInfo(DeBuff, 1);
				if (skill != null)
				{
					skill.getEffects(actor, attacker, false, false);
				}
				else
				{
					System.out.println("Skill " + DeBuff + " is null, fix it.");
				}
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
}