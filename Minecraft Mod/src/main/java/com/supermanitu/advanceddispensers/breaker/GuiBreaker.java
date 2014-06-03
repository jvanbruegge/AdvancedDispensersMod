package com.supermanitu.advanceddispensers.breaker;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiBreaker extends GuiContainer
{
	private static final ResourceLocation breakerGuiTextures = new ResourceLocation("textures/gui/container/dispenser.png");
    public TileEntityBreaker tileEntityBreaker;
    private static final String __OBFID = "CL_00000765";

    public GuiBreaker(InventoryPlayer inventoryPlayer, TileEntityBreaker tileEntityBreaker)
    {
        super(new ContainerBreaker(inventoryPlayer, tileEntityBreaker));
        this.tileEntityBreaker = tileEntityBreaker;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = this.tileEntityBreaker.hasCustomInventoryName() ? I18n.format(this.tileEntityBreaker.getInventoryName(), new Object[0]) :  I18n.format("container.dispenser", new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(breakerGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
