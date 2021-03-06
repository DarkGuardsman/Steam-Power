package dark.SteamPower.steamengine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.network.IPacketReceiver;

import com.google.common.io.ByteArrayDataInput;

import dark.BasicUtilities.api.IForce;
import dark.BasicUtilities.api.IReadOut;
import dark.Library.prefab.TileEntityMachine;
import dark.SteamPower.SteamPowerMain;

public class TileEntitySteamPiston extends TileEntityMachine implements IPacketReceiver,IForce, IReadOut
{
    public int force = 0;
    public int aForce = 0;
    public int bForce = 0;
    private int frictionLoad = 10;
    public int steam = 0;
    public int water = 0;
    public int maxWater = 2;
    public int maxSteam = 10;
    public int pos = 0; // max at 7
    private int tickCount = 0;
    private int runTime = 0;
    private int genRate = 0;// max 100
    private int posCount = 0;
    public int tCount = 0;
    private ForgeDirection frontDir;
    public TileEntity ff;
    public TileEntity bb;
    private int pWater = 0;
    private int pSteam = 0;
    private int pForce = 0;
    public int pCount = 0;
    public boolean running = false;

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (tickCount++ >= 10)
        {
            tickCount = 0;
            // this.pos += 1; if(pos >= 8){pos = 0;}
            // ++tCount;if(tCount > 120){tCount = 0;}

            int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            int nMeta = 0;

            switch (meta)
            {
                case 0:
                    nMeta = 2;
                    break;
                case 1:
                    nMeta = 5;
                    break;
                case 2:
                    nMeta = 3;
                    break;
                case 3:
                    nMeta = 4;
                    break;
            }
            frontDir = ForgeDirection.getOrientation(nMeta);
            ff = worldObj.getBlockTileEntity(xCoord + frontDir.offsetX, yCoord + 1, zCoord + frontDir.offsetZ);
            bb = worldObj.getBlockTileEntity(xCoord + frontDir.getOpposite().offsetX, yCoord + 1, zCoord + frontDir.getOpposite().offsetZ);
            if (this.runTime > 0)
            {
                this.running = true;
            }
            else
            {
                this.running = false;
            }
            if (this.running)
            {
                int countA = 10 - (genRate / 10);
                if (posCount++ >= countA)
                {
                    posCount = 0;
                    pos += 1;
                    if (pos > 7)
                    {
                        pos = 0;
                    }
                }
            }
            if (!worldObj.isRemote)
            {
                if (this.runTime < 1 && this.steam > 0)
                {
                    this.steam--;
                    this.runTime = 60;
                }
                if (bb instanceof IForce)
                {
                    if (((IForce) bb).canOutputSide(frontDir))
                    {
                        this.bForce = ((IForce) bb).getForce();
                    }
                    else if (bb instanceof TileEntitySteamPiston)
                    {
                        if (((TileEntitySteamPiston) bb).getMeta() == this.getMeta())
                        {
                            this.bForce = ((TileEntitySteamPiston) bb).getForce();
                        }
                    }
                    else
                    {
                        this.bForce = 0;
                    }
                }
                if (this.runTime > 0)
                {
                    genRate = Math.min(genRate + 1, 100);
                    this.runTime -= 1;
                    this.force = Math.min(genRate * 10, 1000);
                    this.aForce = Math.max(force - this.frictionLoad + bForce, 0);
                }
                if (runTime == 0 && this.steam == 0)
                {
                    genRate = Math.max(genRate--, 0);
                    force = Math.max(force -= 10, 0);
                    ;
                }

                if (ff instanceof IForce)
                {
                    if (((IForce) ff).canInputSide(frontDir.getOpposite()))
                    {
                        ((IForce) ff).applyForce(this.aForce);
                    }
                    else
                    {

                    }
                }
                pWater = this.water;
                pSteam = this.steam;
                pForce = this.force;

            }
        }
    }

    // -------------------
    // Liquid and mechanical stuff
    // ----------------
    @Override
    public int getForceSide(ForgeDirection side)
    {
        return aForce;
    }

    @Override
    public boolean canOutputSide(ForgeDirection side)
    {
        if (frontDir.getOpposite() == side) { return true; }
        return false;
    }

    @Override
    public boolean canInputSide(ForgeDirection side)
    {
        if (frontDir == side) { return true; }
        return false;
    }

    @Override
    public int applyForce(int force)
    {
        this.bForce = force;
        return aForce;
    }
    // -------------------
    // Data
    // ----------------
    public Object[] getSendData()
    {
        return new Object[]
            { steam, water, force, aForce, genRate, runTime };
    }

    public boolean needUpdate()
    {
        if (this.pForce != this.force || this.pWater != this.water || this.pSteam != this.steam) { return true; }
        return false;
    }

    @Override
    public void handlePacketData(INetworkManager network, int packetType,
            Packet250CustomPayload packet, EntityPlayer player,
            ByteArrayDataInput dataStream)
    {
        try
        {
            this.steam = dataStream.readInt();
            this.water = dataStream.readInt();
            this.force = dataStream.readInt();
            this.aForce = dataStream.readInt();
            this.genRate = dataStream.readInt();
            this.runTime = dataStream.readInt();
            ++pCount;
        }
        catch (Exception e)
        {
            System.out.print("SteamPistonDataFail");
            e.printStackTrace();
        }

    }

    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.runTime = par1NBTTagCompound.getInteger("time");
        this.genRate = par1NBTTagCompound.getInteger("gen");
        this.steam = par1NBTTagCompound.getInteger("steam");
        this.water = par1NBTTagCompound.getInteger("water");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("time", (int) this.runTime);
        par1NBTTagCompound.setInteger("gen", (int) this.genRate);
        par1NBTTagCompound.setInteger("steam", (int) this.steam);
        par1NBTTagCompound.setInteger("water", (int) this.water);

    }

    @Override
    public int getAnimationPos()
    {
        // TODO Auto-generated method stub
        return this.pos;
    }

    @Override
    public int getForce()
    {
        // TODO Auto-generated method stub
        return this.force;
    }

    public int getMeta()
    {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    }

    @Override
    public String getChannel()
    {
        return SteamPowerMain.channel;
    }

    @Override
    public int getSizeInventory()
    {
        return 0;
    }

    @Override
    public String getMeterReading(EntityPlayer user, ForgeDirection side)
    {
        return this.aForce+"N Out "+this.bForce+"N In";
    }
}
