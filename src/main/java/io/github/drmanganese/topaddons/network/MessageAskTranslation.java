package io.github.drmanganese.topaddons.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import io.github.drmanganese.topaddons.TOPAddons;

import io.netty.buffer.ByteBuf;

public class MessageAskTranslation implements IMessage, IMessageHandler<MessageAskTranslation, IMessage> {

    private String unloc;

    public MessageAskTranslation() {

    }

    public MessageAskTranslation(String unloc) {
        this.unloc = unloc;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.unloc = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.unloc);
    }

    @Override
    public IMessage onMessage(MessageAskTranslation message, MessageContext ctx) {
        if (I18n.hasKey(message.unloc)) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                TOPAddons.LOGGER.info("Asking " + Minecraft.getMinecraft().player.getName() + " to translate " + message.unloc);
                PacketHandler.INSTANCE.sendToServer(new MessageSendTranslation(message.unloc, I18n.hasKey(message.unloc) ? I18n.format(message.unloc) : message.unloc));
            });
        }
        return null;
    }
}
