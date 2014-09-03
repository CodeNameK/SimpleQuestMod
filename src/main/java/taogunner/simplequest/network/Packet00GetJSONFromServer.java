package taogunner.simplequest.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import taogunner.simplequest.entity.player.ExtendedPlayer;
import taogunner.simplequest.util.json.JSONFullScript;
import taogunner.simplequest.util.json.JSONQuery;
import taogunner.simplequest.util.json.JSONServerSide;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class Packet00GetJSONFromServer implements IMessage,IMessageHandler<Packet00GetJSONFromServer, IMessage>
{
	private int quest_num;
	private int quest_pos;

	public Packet00GetJSONFromServer() {}

	public Packet00GetJSONFromServer(int par1int)
	{
		this.quest_num = par1int;
		this.quest_pos = -1;
	}
	
	public Packet00GetJSONFromServer(int par1int, int par2int)
	{
		this.quest_num = par1int;
		this.quest_pos = par2int;
	}

	@Override
	public IMessage onMessage(Packet00GetJSONFromServer message, MessageContext ctx)
	{
		try
		{
			JSONFullScript jsonfull = new Gson().fromJson(JSONQuery.JSONReadFile(message.quest_num), JSONFullScript.class);
			JSONServerSide jsonserver;
			if (message.quest_pos != -1) { jsonserver = new JSONServerSide(jsonfull, ctx.getServerHandler().playerEntity, message.quest_pos); }
			else { jsonserver = new JSONServerSide(jsonfull, ctx.getServerHandler().playerEntity, ExtendedPlayer.get(ctx.getServerHandler().playerEntity).quest_position[message.quest_num]); }
			return new Packet01SendJSONToClient(new Gson().toJson(jsonserver));
		}
		catch (JsonSyntaxException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.quest_num = buf.readInt();
		this.quest_pos = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.quest_num);
		buf.writeInt(this.quest_pos);
	}
}
