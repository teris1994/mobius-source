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
package lineage2.gameserver.handler.usercommands.impl;

import lineage2.gameserver.handler.usercommands.IUserCommandHandler;
import lineage2.gameserver.model.Player;

public class ResetName implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		117
	};
	
	@Override
	public boolean useUserCommand(int id, Player activeChar)
	{
		if (COMMAND_IDS[0] != id)
		{
			return false;
		}
		if (activeChar.getVar("oldtitle") != null)
		{
			activeChar.setTitleColor(Player.DEFAULT_TITLE_COLOR);
			activeChar.setTitle(activeChar.getVar("oldtitle"));
			activeChar.broadcastUserInfo(true);
			return true;
		}
		return false;
	}
	
	@Override
	public final int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}