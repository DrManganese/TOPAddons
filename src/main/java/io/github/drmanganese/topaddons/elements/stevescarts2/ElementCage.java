package io.github.drmanganese.topaddons.elements.stevescarts2;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;
import vswe.stevescarts.items.ModItems;

public class ElementCage implements IElement {

    private int id;

    private final String entityName;
    private final NBTTagCompound entityNbt;

    public ElementCage(int id, Entity entity) {
        this.id = id;
        this.entityName = EntityList.getEntityString(entity);
        this.entityNbt = entity.serializeNBT();
    }

    public ElementCage(ByteBuf buf) {
        this.entityName = NetworkTools.readString(buf);
        this.entityNbt = NetworkTools.readNBT(buf);

    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, entityName);
        NetworkTools.writeNBT(buf, entityNbt);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(int x, int y) {
        Entity entity = EntityList.createEntityFromNBT(entityNbt, Minecraft.getMinecraft().world);
        RenderHelper.renderEntity(entity, x, y-10, 5);
        RenderHelper.renderItemStack(Minecraft.getMinecraft(), Minecraft.getMinecraft().getRenderItem(), new ItemStack(ModItems.modules, 1, 57), x, y, "");
    }

    @Override
    public int getWidth() {
        return 20;
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public int getID() {
        return id;
    }
}
