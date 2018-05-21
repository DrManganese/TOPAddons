package io.github.drmanganese.topaddons.network;

import io.github.drmanganese.topaddons.TOPAddons;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class MessageElementSync implements IMessage, IMessageHandler<MessageElementSync, IMessage> {

    private Map<String, Integer> elementIdMap;

    public MessageElementSync() {
    }

    public MessageElementSync(Map<String, Integer> elementIdMap) {
        this.elementIdMap = elementIdMap;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        Map<String, Integer> elementIdMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            elementIdMap.put(ByteBufUtils.readUTF8String(buf), buf.readInt());
        }
        this.elementIdMap = elementIdMap;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.elementIdMap.size());
        for (Map.Entry<String, Integer> entry : elementIdMap.entrySet()) {
            ByteBufUtils.writeUTF8String(buf, entry.getKey());
            buf.writeInt(entry.getValue());
        }
    }

    @Override
    public IMessage onMessage(MessageElementSync message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            ctx.getServerHandler().player.getCapability(TOPAddons.SYNC_CAP, null).setElementIds(message.elementIdMap);
            TOPAddons.logger.info("Synced elements ids to server for player -> " + ctx.getServerHandler().player.getCapability(TOPAddons.SYNC_CAP, null).getAllElementIds());
        });
        return null;
    }
}
