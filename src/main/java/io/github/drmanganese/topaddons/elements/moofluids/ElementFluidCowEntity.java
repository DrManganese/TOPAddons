package io.github.drmanganese.topaddons.elements.moofluids;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import com.robrit.moofluids.common.entity.EntityFluidCow;
import com.robrit.moofluids.common.util.EntityHelper;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.rendering.RenderHelper;

public class ElementFluidCowEntity implements IElement {

    private final Fluid fluid;
    private int id;

    public ElementFluidCowEntity(int id, Fluid fluid) {
        this.id = id;
        this.fluid = fluid;
    }

    public ElementFluidCowEntity(ByteBuf buf) {
        this.fluid = FluidRegistry.getFluid(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void render(int x, int y) {
        EntityEntry entry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation("moofluids", "entityfluidcow"));
        if (entry != null) {
            final EntityFluidCow entity;
            entity = (EntityFluidCow) entry.newInstance(Minecraft.getMinecraft().world);
            entity.setEntityFluid(fluid);
            entity.setEntityTypeData(EntityHelper.getEntityData(fluid.getName()));

            RenderHelper.renderEntity(entity, x, y, 14.0f / (0.7F * entity.height + 0.3F));
        }
    }

    @Override
    public int getWidth() {
        return 25;
    }

    @Override
    public int getHeight() {
        return 25;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, fluid.getName());
    }

    @Override
    public int getID() {
        return this.id;
    }
}
