package micdoodle8.mods.galacticraft.planets.mars.dimension;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.ChunkProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.WorldChunkManagerMars;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsWorldProvider.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class WorldProviderMars extends WorldProvider implements IGalacticraftWorldProvider
{
	@Override
	public void setDimension(int var1)
	{
		this.dimensionId = var1;
		super.setDimension(var1);
	}

	@Override
	protected void generateLightBrightnessTable()
	{
		final float var1 = 0.0F;

		for (int var2 = 0; var2 <= 15; ++var2)
		{
			final float var3 = 1.0F - var2 / 15.0F;
			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
		}
	}

	@Override
	public float[] calcSunriseSunsetColors(float var1, float var2)
	{
		return null;
	}

	@Override
	public void registerWorldChunkManager()
	{
		this.worldChunkMgr = new WorldChunkManagerMars(this.worldObj, 0F);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Vec3 getFogColor(float var1, float var2)
	{
		return this.worldObj.getWorldVec3Pool().getVecFromPool((double) 210F / 255F, (double) 120F / 255F, (double) 59F / 255F);
	}

	@Override
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
	{
		return this.worldObj.getWorldVec3Pool().getVecFromPool(154 / 255.0F, 114 / 255.0F, 66 / 255.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1)
	{
        float f1 = this.worldObj.getCelestialAngle(par1);
        float f2 = 1.0F - (MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.25F);

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        return f2 * f2 * 0.75F;
	}

	@Override
	public float calculateCelestialAngle(long par1, float par3)
	{
		return super.calculateCelestialAngle(par1, par3);
	}

	public float calculatePhobosAngle(long par1, float par3)
	{
		return this.calculateCelestialAngle(par1, par3) * 3000;
	}

	public float calculateDeimosAngle(long par1, float par3)
	{
		return this.calculatePhobosAngle(par1, par3) * 0.0000000001F;
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new ChunkProviderMars(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
	}

	@Override
	public void updateWeather()
	{
		this.worldObj.getWorldInfo().setRainTime(0);
		this.worldObj.getWorldInfo().setRaining(false);
		this.worldObj.getWorldInfo().setThunderTime(0);
		this.worldObj.getWorldInfo().setThundering(false);
	}

	@Override
	public boolean isSkyColored()
	{
		return true;
	}

	@Override
	public double getHorizon()
	{
		return 44.0D;
	}

	@Override
	public int getAverageGroundLevel()
	{
		return 44;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return true;
	}

	@Override
	public boolean canCoordinateBeSpawn(int var1, int var2)
	{
		return true;
	}

	@Override
	public boolean canRespawnHere()
	{
		return !ConfigManagerCore.forceOverworldRespawn;
	}

	@Override
	public String getSaveFolder()
	{
		return "DIM" + ConfigManagerPlanets.dimensionIDMars;
	}

	@Override
	public String getWelcomeMessage()
	{
		return "Entering Mars";
	}

	@Override
	public String getDepartMessage()
	{
		return "Leaving Mars";
	}

	@Override
	public String getDimensionName()
	{
		return "Mars";
	}

	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
	{
		return false;
	}

	@Override
	public boolean canDoLightning(Chunk chunk)
	{
		return false;
	}

	@Override
	public boolean canDoRainSnowIce(Chunk chunk)
	{
		return false;
	}

	@Override
	public float getGravity()
	{
		return 0.058F;
	}

	@Override
	public int getHeight()
	{
		return 800;
	}

	@Override
	public double getMeteorFrequency()
	{
		return 10.0D;
	}

	@Override
	public double getFuelUsageMultiplier()
	{
		return 0.9D;
	}

	@Override
	public boolean canSpaceshipTierPass(int tier)
	{
		return tier >= 2;
	}

	@Override
	public float getFallDamageModifier()
	{
		return 0.38F;
	}

	@Override
	public float getSoundVolReductionAmount()
	{
		return 10.0F;
	}
}
