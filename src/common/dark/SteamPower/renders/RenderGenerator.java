package dark.SteamPower.renders;

import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

import org.lwjgl.opengl.GL11;

import dark.SteamPower.SteamPowerMain;

public class RenderGenerator extends TileEntitySpecialRenderer
{
	int type = 0;
	private ModelGenerator model;
	
	public RenderGenerator()
	{
		model = new ModelGenerator();
	}


	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float d3) {
		bindTextureByName(SteamPowerMain.textureFile+"Generator.png");
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
		int meta = tileEntity.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
		switch(meta)
		{
			case 0:GL11.glRotatef(0f, 0f, 1f, 0f);break;
			case 1:GL11.glRotatef(90f, 0f, 1f, 0f);break;
			case 2:GL11.glRotatef(180f, 0f, 1f, 0f);break;
			case 3:GL11.glRotatef(270f, 0f, 1f, 0f);break;
		}
		model.RenderMain(0.0625F);
		GL11.glPopMatrix();
	}

}