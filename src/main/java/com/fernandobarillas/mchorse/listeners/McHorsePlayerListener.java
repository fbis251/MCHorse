package com.fernandobarillas.mchorse.listeners;

import com.fernandobarillas.mchorse.Constants;
import com.fernandobarillas.mchorse.helpers.MessageHelper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BlockIterator;

/**
 * Created by fb on 3/18/15.
 */
public class McHorsePlayerListener implements Listener {

    private Plugin mPlugin;

    private int mLastShotArrowId;

    private Block mTargetBlock;

    private Material mOriginalMaterial;

    public McHorsePlayerListener(Plugin plugin) {
        mPlugin = plugin;
    }

    // TODO: Add support for having custom names marking their current score
    // TODO: Hitting a block with grass/vines in front of it scores a miss
    // TODO: Arrows that go through grass and hit the target block still count as misses
    // TODO: Extinguish arrows that hit the target block

    /**
     * Allows a player to right click a block while holding an arrow in their hands to set the target block
     */
    @EventHandler
    public void onPlayerRightClickBlockWithArrowEvent(PlayerInteractEvent event) {
        // TODO: Allow setting a region of blocks, possibly use WorldEdit?
        Player player = event.getPlayer();
        // Make sure we only set a target block when a player right clicks a target block while holding an arrow
        if (player.getItemInHand().getType().equals(Material.ARROW)) {
            Block targetBlock = event.getClickedBlock();
            setTargetBlock(targetBlock, player);
        }
    }

    /**
     * Handles a player shooting an arrow with a bow to keep track of arrow Entity IDs to keep score with
     */
    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        Arrow arrow = (Arrow) event.getProjectile();
        // TODO: Track the last arrow per player
        mLastShotArrowId = arrow.getEntityId();
    }

    /**
     * Handles a projectile hit event, specifically looking for arrows hitting blocks.
     */
    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event) {
        ProjectileSource projectileSource = event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        if (!(projectileSource instanceof Player)) {
            // We only want projectiles shot by Players who are playing
            return;
        }

        if (!(projectile instanceof Arrow)) {
            // We only want to check Arrow projectiles
            return;
        }

        // Validate the arrow hit, making sure that the correct player hit the right block
        Player player = (Player) projectileSource;
        Arrow arrow = (Arrow) projectile;
        validateHit(arrow, player);
    }

    /**
     * Attempts to restore the target block to its original state
     */
    void restoreBlock() {
        if (mTargetBlock != null && mOriginalMaterial != null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mTargetBlock.setType(mOriginalMaterial);
        }
    }

    /**
     * Sets the CustomName for an arrow and displays it
     *
     * @param arrow      The arrow to set the name for
     * @param playerName The name of the Player who shot the arrow
     * @param arrowHit   True when the arrow hit its target, false when the arrow missed its target
     */
    void setArrowName(Arrow arrow, String playerName, boolean arrowHit) {
        String arrowName = playerName + " ";
        arrowName += (arrowHit) ? "HIT" : "MISS";
        arrow.setCustomName(arrowName);
        arrow.setCustomNameVisible(true);
    }

    /**
     * Sets the target block for the game of horse
     *
     * @param targetBlock The block that each Player needs to hit to win the round
     * @param player      The player who set the target block
     */
    private void setTargetBlock(Block targetBlock, Player player) {
        // TODO: Make sure player has permission to set the target block
        // TODO: Tell all players in the queue receive the target block set/unset messages
        String message = null;
        if (mTargetBlock == null) {
            mTargetBlock = targetBlock;
            mOriginalMaterial = mTargetBlock.getType();
            targetBlock.setType(Constants.TARGET_BLOCK_MATERIAL);
            message = player.getName() + " set the target block";
        } else {
            restoreBlock();
            mTargetBlock = null;
            message = player.getName() + " unset the target block";
        }
        if(message != null) {
            MessageHelper.broadcastMessage(mPlugin, message);
        }
    }

    /**
     * Verifies that a Player's Arrow hit the correct block, granting them a point
     *
     * @param arrow  The arrow to validate, making sure it hit its target block
     * @param player The Player who shot the Arrow
     */
    private void validateHit(Arrow arrow, Player player) {

        if (arrow.getEntityId() != mLastShotArrowId) {
            // This arrow was not the last one that was shot
            return;
        }

        if (mTargetBlock == null) {
            // There isn't any target block set by the player yet
            return;
        }

        World world = arrow.getWorld();
        BlockIterator iterator = new BlockIterator(world, arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0, 4);
        Block hitBlock;
        while (iterator.hasNext()) {
            hitBlock = iterator.next();

            // We only want to check blocks that don't contain air to score a hit
            if (hitBlock.getType() != Material.AIR) {
                if (hitBlock.getLocation().equals(mTargetBlock.getLocation())) {
                    MessageHelper.pluginMessage(player, "You hit the target block!");
                    setArrowName(arrow, player.getName(), true);
                    arrow.setFireTicks(0); // Extinguish arrow in 0 ticks
                    mLastShotArrowId = -1;
                } else {
                    MessageHelper.pluginMessage(player, "You missed the target block");
                    setArrowName(arrow, player.getName(), false);
                }
                break;
            }
        }
    }
}
