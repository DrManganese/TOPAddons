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

public class MessageClientCfgSync implements IMessage, IMessageHandler<MessageClientCfgSync, IMessage> {

    private Map<String, String> syncMap;

    @SuppressWarnings("unused")
    public MessageClientCfgSync() {
    }

    public MessageClientCfgSync(Map<String, Object> syncMap) {
        this.syncMap = new HashMap<>(syncMap.size());
        syncMap.forEach((k, v) -> this.syncMap.put(k, v.toString()));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        final int size = buf.readInt();
        this.syncMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            syncMap.put(ByteBufUtils.readUTF8String(buf), ByteBufUtils.readUTF8String(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.syncMap.size());
        for (String key : syncMap.keySet()) {
            ByteBufUtils.writeUTF8String(buf, key);
            ByteBufUtils.writeUTF8String(buf, this.syncMap.get(key));
        }
    }

    @Override
    public IMessage onMessage(MessageClientCfgSync message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            //noinspection ConstantConditions
            ctx.getServerHandler().player.getCapability(TOPAddons.CLIENT_CFG_CAP, null).setAll(message.syncMap);
        });
        return null;
    }
}
