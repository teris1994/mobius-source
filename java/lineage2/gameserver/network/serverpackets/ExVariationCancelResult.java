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
package lineage2.gameserver.network.serverpackets;

public class ExVariationCancelResult extends L2GameServerPacket
{
	private final int _closeWindow;
	private final int _unk1;
	
	public ExVariationCancelResult(int result)
	{
		_closeWindow = 1;
		_unk1 = result;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x58);
		writeD(_unk1);
		writeD(_closeWindow);
	}
}