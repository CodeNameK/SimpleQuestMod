package taogunner.simplequest.network;

import io.netty.buffer.ByteBuf;
import taogunner.simplequest.util.ServerUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class Packet00GetJSONFromServer implements IMessage,IMessageHandler<Packet00GetJSONFromServer, IMessage>
{
	private int npcID;
	private int quest_pos;
	
	public Packet00GetJSONFromServer() {}
	
	public Packet00GetJSONFromServer(int npcID, int quest_pos)
	{
		this.npcID = npcID;
		this.quest_pos = quest_pos;
	}

	@Override
	public IMessage onMessage(Packet00GetJSONFromServer message, MessageContext ctx)
	{
		ServerUtils.getJSONQuest(ctx.getServerHandler().playerEntity, message.npcID, message.quest_pos);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.npcID = buf.readInt();
		this.quest_pos = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.npcID);
		buf.writeInt(this.quest_pos);
	}
}
