package basicpipes.conductors;

import java.util.Random;

import basicpipes.BasicPipesMain;
import basicpipes.pipes.api.ILiquidConsumer;
import basicpipes.pipes.api.ILiquidProducer;
import basicpipes.pipes.api.Liquid;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockPipe extends BlockContainer 
{	
	
	public BlockPipe(int id)
	{
		super(id, Material.iron);
		this.setBlockName("Pipe");
		this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
		this.setHardness(1f);
		this.setResistance(3f);
	}
    public boolean isOpaqueCube(){return false;}
    public boolean renderAsNormalBlock(){return false;}
    public int getRenderType(){return -1;}
    public int idDropped(int par1, Random par2Random, int par3){return 0;}
	@Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
    }
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
    	super.onNeighborBlockChange(world, x, y, z, blockID);
    }
	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
		int var5 = par1World.getBlockId(par2, par3, par4);
        return var5 == 0 || blocksList[var5].blockMaterial.isGroundCover();
    }
	@Override
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
    {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		// TODO Auto-generated method stub
		return new TileEntityPipe();
	}
	 @Override
	public void breakBlock(World world, int x, int y, int z,int par5, int par6)
	 {
		 super.breakBlock(world, x, y, z, par5, par6);
		 TileEntity ent = world.getBlockTileEntity(x, y, z);
		 Random furnaceRand = new Random();
		 if(ent instanceof TileEntityPipe)
		 {
			 TileEntityPipe pipe = (TileEntityPipe) ent;
			 int meta = pipe.type.ordinal();
			 float var8 = furnaceRand.nextFloat() * 0.8F + 0.1F;
             float var9 = furnaceRand.nextFloat() * 0.8F + 0.1F;
             float var10 = furnaceRand.nextFloat() * 0.8F + 0.1F;
			 EntityItem var12 = new EntityItem(world, (double)((float)x + var8), (double)((float)y + var9), 
					 (double)((float)z + var10), new ItemStack(BasicPipesMain.itemPipes, 1, meta));	
             float var13 = 0.05F;
             var12.motionX = (double)((float)furnaceRand.nextGaussian() * var13);
             var12.motionY = (double)((float)furnaceRand.nextGaussian() * var13 + 0.2F);
             var12.motionZ = (double)((float)furnaceRand.nextGaussian() * var13);
             world.spawnEntityInWorld(var12);
		 }
	 }

 }

