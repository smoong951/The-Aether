package com.aetherteam.aether.item.combat.abilities.armor;

import com.aetherteam.aether.attachment.AetherDataAttachments;
import com.aetherteam.aether.attachment.AetherPlayerAttachment;
import com.aetherteam.aether.item.EquipmentUtil;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public interface GravititeArmor {
    /**
     * Launches the entity if wearing a full set of Gravitite armor ({@link EquipmentUtil#hasFullGravititeSet(LivingEntity)}).<br><br>
     * If they're a player, there are additional checks for if the Gravitite jump ability is active. This is dependent on {@link AetherPlayerAttachment#isGravititeJumpActive()},
     * which is set by {@link com.aetherteam.aether.client.event.hooks.CapabilityClientHooks.AetherPlayerHooks} if the player is holding down the {@link com.aetherteam.aether.client.AetherKeys#GRAVITITE_JUMP_ABILITY} key (this is by default {@link org.lwjgl.glfw.GLFW#GLFW_KEY_LEFT_SHIFT}).
     *
     * @param entity The {@link LivingEntity} wearing the armor.
     * @see com.aetherteam.aether.event.listeners.abilities.ArmorAbilityListener#onEntityJump(LivingEvent.LivingJumpEvent)
     */
    static void boostedJump(LivingEntity entity) {
        if (EquipmentUtil.hasFullGravititeSet(entity)) {
            if (entity instanceof Player player) {
                if (player.onGround() && player.getData(AetherDataAttachments.AETHER_PLAYER).isGravititeJumpActive()) {
                    player.push(0.0, 1.0, 0.0);
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
                    }
                }
            } else {
                entity.push(0.0, 1.0, 0.0);
            }
        }
    }
}
