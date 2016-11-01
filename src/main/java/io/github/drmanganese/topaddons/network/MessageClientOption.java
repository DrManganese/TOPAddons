package io.github.drmanganese.topaddons.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import io.github.drmanganese.topaddons.ConfigClient;

import io.netty.buffer.ByteBuf;

public class MessageClientOption implements IMessage, IMessageHandler<MessageClientOption, IMessage>{

    private String optionToSend;
    private int valueToSend;

    public MessageClientOption(){}

    public MessageClientOption(String option, int value) {
        this.optionToSend = option;
        this.valueToSend = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.optionToSend = ByteBufUtils.readUTF8String(buf);
        this.valueToSend = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.optionToSend);
        buf.writeInt(this.valueToSend);
    }

    @Override
    public IMessage onMessage(MessageClientOption message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> ConfigClient.set(message.optionToSend, message.valueToSend));
        return null;
    }
}
