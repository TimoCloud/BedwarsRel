package io.github.yannici.bedwars.Com.v1_8_R2;

import java.lang.reflect.Field;
import java.util.ArrayList;

import io.github.yannici.bedwars.Shop.Specials.ITNTSheep;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftTNTPrimed;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityTargetEvent;

import net.minecraft.server.v1_8_R2.EntityHuman;
import net.minecraft.server.v1_8_R2.EntityLiving;
import net.minecraft.server.v1_8_R2.EntitySheep;
import net.minecraft.server.v1_8_R2.GenericAttributes;
import net.minecraft.server.v1_8_R2.EntityTNTPrimed;

public class TNTSheep extends EntitySheep implements ITNTSheep {
	
	private World world = null;
	private TNTPrimed primedTnt = null;
	
	public TNTSheep(World world, Player target) {
		super(((CraftWorld) world).getHandle());
		
		this.world = world;
		
		this.locX = target.getLocation().getX();
		this.locY = target.getLocation().getY();
		this.locZ = target.getLocation().getZ();

		try {
			Field b = this.goalSelector.getClass().getDeclaredField("b");
			b.setAccessible(true);
			b.set(this.goalSelector, new ArrayList<>());
			this.getAttributeInstance(GenericAttributes.b).setValue(128D);
			this.getAttributeInstance(GenericAttributes.d).setValue(0.37D);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.goalSelector.a(0, new PathfinderGoalBedwarsPlayer(this, EntityHuman.class, 1D, false));
		this.setGoalTarget((EntityLiving) (((CraftPlayer) target).getHandle()), EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, false);
		((Creature) this.getBukkitEntity()).setTarget((LivingEntity) target);
	}

	@Override
	public Location getLocation() {
		return new Location(this.world, this.locX, this.locY, this.locZ);
	}

	@Override
	public void setTNT(TNTPrimed tnt) {
		this.primedTnt = tnt;
	}
	
	@Override
	public TNTPrimed getTNT() {
		return this.primedTnt;
	}
	
	@Override
    public void setPassenger(TNTPrimed tnt) {
        this.getBukkitEntity().setPassenger(tnt);
    }
	
	@Override
    public void remove() {
        this.getBukkitEntity().remove();
    }
	
	@Override
    public void setTNTSource(Entity source) {
        try {
            Field sourceField = EntityTNTPrimed.class.getDeclaredField("source");
            sourceField.setAccessible(true);
            sourceField.set(((CraftTNTPrimed) primedTnt).getHandle(), ((CraftEntity) source).getHandle());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}