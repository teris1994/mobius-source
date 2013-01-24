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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.instancemanager.commission.CommissionShopManager;
import lineage2.gameserver.model.Player;

public class RequestCommissionBuyItem extends L2GameClientPacket
{
	private long auctionId;
	private int exItemType;
	
	@Override
	protected void readImpl()
	{
		auctionId = readQ();
		exItemType = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		CommissionShopManager.getInstance().requestBuyItem(player, auctionId, exItemType);
	}
}