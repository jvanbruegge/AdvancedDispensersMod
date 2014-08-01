package com.supermanitu.advanceddispensers.placer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiPlacer extends GuiContainer
{
	private static final ResourceLocation placerGuiTextures = new ResourceLocation("textures/gui/container/dispenser.png");
    public TileEntityPlacer tileEntityPlacer;
    private static final String __OBFID = "CL_00000765";

    public GuiPlacer(InventoryPlayer inventoryPlayer, TileEntityPlacer tileEntityBreaker)
    {
        super(new ContainerPlacer(inventoryPlayer, tileEntityBreaker));
        this.tileEntityPlacer = tileEntityBreaker;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = this.tileEntityPlacer.hasCustomInventoryName() ? I18n.format(this.tileEntityPlacer.getInventoryName(), new Object[0]) :  I18n.format("container.dispenser", new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(placerGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
