package taogunner.simplequest.network;

import io.netty.buffer.ByteBuf;
import taogunner.simplequest.client.ClientProxy;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class Packet01SendJSONToClient implements IMessage,IMessageHandler<Packet01SendJSONToClient, IMessage>
{
	public String jsonclient;
	
	public Packet01SendJSONToClient(){}
	
	public Packet01SendJSONToClient(String jsonclient)
	{
		this.jsonclient = jsonclient;
	}
	
	@Override
	public IMessage onMessage(Packet01SendJSONToClient message, MessageContext ctx)
	{
		ClientProxy.openGUI(1, message.jsonclient);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.jsonclient = new String(buf.readBytes(buf.readableBytes()).array());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBytes(jsonclient.getBytes());
	}
}
