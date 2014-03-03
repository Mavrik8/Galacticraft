package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockMulti.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockMulti extends BlockContainer implements IPartialSealableBlock
{
	private IIcon[] fakeIcons;

	public BlockMulti(String assetName)
	{
		super(GCBlocks.machine);
		this.setStepSound(Block.soundTypeMetal);
		this.setBlockTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
		this.setResistance(1000000000000000.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.fakeIcons = new IIcon[4];
		this.fakeIcons[0] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "launch_pad");
		this.fakeIcons[1] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "workbench_nasa_top");
		this.fakeIcons[2] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "solar_basic_0");

		if (Loader.isModLoaded("GalacticraftPlanets"))
		{
			try
			{
				Class<?> c = Class.forName("micdoodle8.mods.galacticraft.planets.GalacticraftPlanets");
				String texturePrefix = (String) c.getField("TEXTURE_PREFIX").get(null);
				this.fakeIcons[3] = par1IconRegister.registerIcon(texturePrefix + "cryoDummy");
			}
			catch (Exception e)
			{
				this.fakeIcons[3] = this.fakeIcons[2];
				e.printStackTrace();
			}
		}
		else
		{
			this.fakeIcons[3] = this.fakeIcons[2];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		switch (par2)
		{
		case 2:
			return this.fakeIcons[0];
		case 3:
			return this.fakeIcons[1];
		case 4:
			return this.fakeIcons[2];
		case 5:
			return this.fakeIcons[3];
		default:
			return this.fakeIcons[0];
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);

		if (meta == 2)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		}
		else
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity)
	{
		int meta = world.getBlockMetadata(x, y, z);

		if (meta == 2)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}
		else
		{
			super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public boolean canDropFromExplosion(Explosion par1Explosion)
	{
		return false;
	}

	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock, int meta)
	{
		worldObj.setBlock(position.intX(), position.intY(), position.intZ(), this, meta, 3);
		((TileEntityMulti) worldObj.getTileEntity(position.intX(), position.intY(), position.intZ())).setMainBlock(mainBlock);
	}

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		return par1World.getBlockMetadata(par2, par3, par4) == 1 ? -1.0F : this.blockHardness;
	}

	@Override
	public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction)
	{
		int metadata = world.getBlockMetadata(x, y, z);

		if (metadata == 4)
		{
			return direction == ForgeDirection.UP || direction == ForgeDirection.DOWN;
		}

		return true;
	}

	@Override
	public Block setBlockTextureName(String name)
	{
		this.textureName = name;
		return this;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity instanceof TileEntityMulti)
		{
			((TileEntityMulti) tileEntity).onBlockRemoval();
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	/**
	 * Called when the block is right clicked by the player. This modified
	 * version detects electric items and wrench actions on your machine block.
	 * Do not override this function. Use machineActivated instead! (It does the
	 * same thing)
	 */
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		TileEntityMulti tileEntity = (TileEntityMulti) par1World.getTileEntity(x, y, z);
		return tileEntity.onBlockActivated(par1World, x, y, z, par5EntityPlayer);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int meta)
	{
		return new TileEntityMulti();
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World par1World, int x, int y, int z)
	{
		TileEntity tileEntity = par1World.getTileEntity(x, y, z);
		Vector3 mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

		if (mainBlockPosition != null)
		{
			Block mainBlockID = par1World.getBlock(mainBlockPosition.intX(), mainBlockPosition.intY(), mainBlockPosition.intZ());

			if (mainBlockID != Blocks.air)
			{
				return mainBlockID.getPickBlock(target, par1World, mainBlockPosition.intX(), mainBlockPosition.intY(), mainBlockPosition.intZ());
			}
		}

		return null;
	}
	
    public int getBedDirection(IBlockAccess world, int x, int y, int z)
    {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		Vector3 mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;
		
		if (mainBlockPosition != null)
		{
			return mainBlockPosition.getBlock(world).getBedDirection(world, mainBlockPosition.intX(), mainBlockPosition.intY(), mainBlockPosition.intZ());
		}
		
        return BlockBed.getDirection(world.getBlockMetadata(x,  y, z));
    }

	@Override
	public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		Vector3 mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;
		
		if (mainBlockPosition != null)
		{
			return mainBlockPosition.getBlock(world).isBed(world, mainBlockPosition.intX(), mainBlockPosition.intY(), mainBlockPosition.intZ(), player);
		}
		
		return super.isBed(world, x, y, z, player);
	}

	@Override
	public void setBedOccupied(IBlockAccess world, int x, int y, int z, EntityPlayer player, boolean occupied)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		Vector3 mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;
		
		if (mainBlockPosition != null)
		{
			mainBlockPosition.getBlock(world).setBedOccupied(world, mainBlockPosition.intX(), mainBlockPosition.intY(), mainBlockPosition.intZ(), player, occupied);
		}
		else
		{
			super.setBedOccupied(world, x, y, z, player, occupied);
		}
	}
}
