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
package ai.hellbound;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

public class Kechi extends DefaultAI
{
	final Skill KechiDoubleCutter;
	final Skill KechiAirBlade;
	final Skill Invincible;
	final Skill NPCparty60ClanHeal;
	private static final int GUARD1 = 22309;
	private static final int GUARD2 = 22310;
	private static final int GUARD3 = 22417;
	private static final Location guard_spawn_loc = new Location(153384, 149528, -12136);
	private static final int[][] guard_run = new int[][]
	{
		{
			GUARD1,
			153384,
			149528,
			-12136
		},
		{
			GUARD1,
			153975,
			149823,
			-12152
		},
		{
			GUARD1,
			154364,
			149665,
			-12151
		},
		{
			GUARD1,
			153786,
			149367,
			-12151
		},
		{
			GUARD2,
			154188,
			149825,
			-12152
		},
		{
			GUARD2,
			153945,
			149224,
			-12151
		},
		{
			GUARD3,
			154374,
			149399,
			-12152
		},
		{
			GUARD3,
			153796,
			149646,
			-12159
		}
	};
	private static String[] chat = new String[]
	{
		"Стража, убейте их!",
		"Стража!",
		"Стража, на помощь!",
		"Добейте их.",
		"Вы все умрете!"
	};
	private int stage = 0;
	
	public Kechi(NpcInstance actor)
	{
		super(actor);
		TIntObjectHashMap<Skill> skills = getActor().getTemplate().getSkills();
		KechiDoubleCutter = skills.get(733);
		KechiAirBlade = skills.get(734);
		Invincible = skills.get(5418);
		NPCparty60ClanHeal = skills.get(5439);
	}
	
	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		Creature target;
		if ((target = prepareTarget()) == null)
		{
			return false;
		}
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return false;
		}
		double actor_hp_precent = actor.getCurrentHpPercents();
		switch (stage)
		{
			case 0:
				if (actor_hp_precent < 80)
				{
					spawnMobs();
					return true;
				}
				break;
			case 1:
				if (actor_hp_precent < 60)
				{
					spawnMobs();
					return true;
				}
				break;
			case 2:
				if (actor_hp_precent < 40)
				{
					spawnMobs();
					return true;
				}
				break;
			case 3:
				if (actor_hp_precent < 30)
				{
					spawnMobs();
					return true;
				}
				break;
			case 4:
				if (actor_hp_precent < 20)
				{
					spawnMobs();
					return true;
				}
				break;
			case 5:
				if (actor_hp_precent < 10)
				{
					spawnMobs();
					return true;
				}
				break;
			case 6:
				if (actor_hp_precent < 5)
				{
					spawnMobs();
					return true;
				}
				break;
		}
		int rnd_per = Rnd.get(100);
		if (rnd_per < 5)
		{
			addTaskBuff(actor, Invincible);
			return true;
		}
		double distance = actor.getDistance(target);
		if (!actor.isAMuted() && (rnd_per < 75))
		{
			return chooseTaskAndTargets(null, target, distance);
		}
		Map<Skill, Integer> d_skill = new HashMap<>();
		addDesiredSkill(d_skill, target, distance, KechiDoubleCutter);
		addDesiredSkill(d_skill, target, distance, KechiAirBlade);
		Skill r_skill = selectTopSkill(d_skill);
		return chooseTaskAndTargets(r_skill, target, distance);
	}
	
	private void spawnMobs()
	{
		stage++;
		NpcInstance actor = getActor();
		Functions.npcSay(actor, chat[Rnd.get(chat.length)]);
		for (int[] run : guard_run)
		{
			try
			{
				SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(run[0]));
				sp.setLoc(guard_spawn_loc);
				sp.setReflection(actor.getReflection());
				NpcInstance guard = sp.doSpawn(true);
				Location runLoc = new Location(run[1], run[2], run[3]);
				guard.setRunning();
				DefaultAI ai = (DefaultAI) guard.getAI();
				ai.addTaskMove(runLoc, true);
				ai.setGlobalAggro(0);
				Creature hated = actor.getAggroList().getRandomHated();
				if (hated != null)
				{
					guard.getAggroList().addDamageHate(hated, 0, Rnd.get(1, 100));
					ai.setAttackTimeout(getMaxAttackTimeout() + System.currentTimeMillis());
					ai.setAttackTarget(hated);
					ai.changeIntention(CtrlIntention.AI_INTENTION_ATTACK, hated, null);
					ai.addTaskAttack(hated);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}