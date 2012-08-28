package SteamPower.turbine;

import java.util.List;
import java.util.Random;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import SteamPower.SteamPowerMain;
import SteamPower.TileEntityMachine;

public class BlockSteamPiston extends universalelectricity.extend.BlockMachine{

	public BlockSteamPiston(int par1) {
		super("SteamEngine", par1, Material.iron);
		
	}
	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
		if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity blockEntity = (TileEntity)par1World.getBlockTileEntity(x, y, z);

            if (blockEntity != null)
            {
            	
            	if(blockEntity instanceof TileEntitySteamPiston)
            	{
            	par5EntityPlayer.openGui(SteamPowerMain.instance, 2, par1World, x, y, z);
            	}
            	if(blockEntity instanceof TileEntitytopGen)
            	{
            	par5EntityPlayer.openGui(SteamPowerMain.instance, 2, par1World, x, y-1, z);
            	}
            }
            return true;
        }
    }
	@Override
    public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
    {
        int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int metadata = par1World.getBlockMetadata(x, y, z);
        TileEntityMachine tileEntity = (TileEntityMachine)par1World.getBlockTileEntity(x, y, z);
        
	        switch (angle)
	        {
	        	case 0: tileEntity.setDirection(1); break;
	        	case 1: tileEntity.setDirection(2); break;
	        	case 2: tileEntity.setDirection(3); break;
	        	case 3: tileEntity.setDirection(4); break;
	        }
    }
	  public TileEntity createNewTileEntity(World var1)
	    {
		  return null;
	    }
	  public void breakBlock(World world, int x, int y, int z,int par5, int par6)
	  {
	  super.breakBlock(world, x, y, z, par5, par6);
	  int meta = world.getBlockMetadata(x, y, z);
	  if(meta < 4)
	  {
		  if(world.getBlockId(x, y+1, z) == this.blockID)
		  {
			  if(world.getBlockMetadata(x, y, z)> 4)
			  {
				  world.setBlockAndMetadataWithUpdate(x, y, z, 0, 0, true);
			  }
		  }
	  }
	  else
		  if(meta > 4)
		  {
			  if(world.getBlockId(x, y-1, z) == this.blockID)
			  {
				  if(world.getBlockMetadata(x, y, z)< 4)
				  {
					  world.setBlockAndMetadataWithUpdate(x, y, z, 0, 0, true);
				  }
			  }
		  }
	  }
	  @Override
	    public boolean isOpaqueCube()
	    {
	        return false;
	    }
		@Override
		public boolean renderAsNormalBlock()
		{
		    return false;
		}
		@Override
		public int getRenderType()
		{
		   return -1;
		}
		@Override
		public TileEntity createNewTileEntity(World world, int metadata)
	    {
			if(metadata < 4)
			{	
				return new TileEntitySteamPiston();
			}
			if(metadata == 14)
			{
				return new TileEntitytopGen();
			}
			return null;
		}
		 public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
		    {
		        int meta = par1World.getBlockMetadata(par2, par3, par4);
		        boolean var7 = false;
		        if (meta == 1)
		        {
		            if (par1World.getBlockId(par2, par3 + 1, par4) != this.blockID)
		            {
		                par1World.setBlockWithNotify(par2, par3, par4, 0);
		                var7 = true;
		            }
		        }
		        else
		        {
		            if (par1World.getBlockId(par2, par3 - 1, par4) != this.blockID)
		            {
		                par1World.setBlockWithNotify(par2, par3, par4, 0);
		            }
		        } 
		        if (var7)
		            {
		                if (!par1World.isRemote)
		                {
		                    this.dropBlockAsItem(par1World, par2, par3, par4, 0, 0);
		                }
		            }
		    }
		 @Override
		 public int idDropped(int par1, Random par2Random, int par3)
		    {
		        return SteamPowerMain.itemEngine.shiftedIndex;
		    }
		@Override
		public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	    {
	        int var5 = par1World.getBlockId(par2, par3, par4);
	        int var6 = par1World.getBlockId(par2, par3+1, par4);
	        return (var5 == 0 || blocksList[var5].blockMaterial.isGroundCover()) && (var6 == 0 || blocksList[var6].blockMaterial.isGroundCover());
	    }
}