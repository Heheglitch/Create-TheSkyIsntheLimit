package net.ctslteam.ctsl.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class CelestialProxyEntity extends Entity {

    private String celestialId = "";

    public CelestialProxyEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void setCelestialId(String celestialId) {
        this.celestialId = celestialId;
    }

    public String getCelestialId() {
        return celestialId;
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) return;

        List<Player> players = level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(1.0));
        for (Player player : players) {
            // TODO resolve celestial by id
            // TODO teleport player to linked dimension
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        celestialId = tag.getString("CelestialId");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putString("CelestialId", celestialId);
    }
}
