package com.theundertaker11.GeneticsReborn;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class CreativeTabGR extends CreativeTabs
{
    public CreativeTabGR(int p1, String KStab)
    {
        super(p1, KStab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return Items.ARROW;
    }
}
