package io.github.drmanganese.topaddons.network;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import io.github.drmanganese.topaddons.TOPAddons;
import io.github.drmanganese.topaddons.reference.Names;

import io.netty.buffer.ByteBuf;

public class MessageSendTranslation implements IMessage, IMessageHandler<MessageSendTranslation, IMessage> {

    private String unloc;
    private String loc;

    public MessageSendTranslation() {

    }

    public MessageSendTranslation(String unloc, String loc) {
        this.unloc = unloc;
        this.loc = loc;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.unloc = ByteBufUtils.readUTF8String(buf);
        this.loc = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.unloc);
        ByteBufUtils.writeUTF8String(buf, this.loc);
    }

    @Override
    public IMessage onMessage(MessageSendTranslation message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            TOPAddons.LOGGER.info("Sending translation " + message.loc + " to server for " + message.unloc);
            Names.translations.put(message.unloc, message.loc);
        });
        return null;
    }
}
