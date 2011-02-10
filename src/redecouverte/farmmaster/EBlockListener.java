package redecouverte.farmmaster;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockDamageLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRightClickEvent;
import org.bukkit.inventory.ItemStack;

public class EBlockListener extends BlockListener {

    private static final Logger logger = Logger.getLogger("Minecraft");
    private final FarmMaster parent;

    public EBlockListener(FarmMaster parent) {
        this.parent = parent;
    }

    public Block getBlockUnderneath(Block b) {
        if (b.getY() < 2) {
            return null;
        }

        Block ret = b.getWorld().getBlockAt(b.getX(), b.getY() - 1, b.getZ());

        return ret;
    }

    public boolean blockUnderneathIsFarmland(Block b) {
        Block u = getBlockUnderneath(b);
        if (u == null) {
            return false;
        }

        return u.getType() == Material.SOIL;
    }

    @Override
    public void onBlockDamage(BlockDamageEvent event) {

        try {

            if (event.isCancelled()) {
                return;
            }

            Block b = event.getBlock();

            if (parent.naturalMode() || parent.woolMode()) {
                if (event.getDamageLevel() == BlockDamageLevel.BROKEN) {
                    Material type = b.getType();

                    switch (type) {
                        case RED_ROSE: {
                            Random rand = new Random();
                            if (blockUnderneathIsFarmland(b) && rand.nextInt(2) == 0) {
                                ItemStack is = new ItemStack(Material.RED_ROSE);
                                is.setAmount(1);
                                b.getWorld().dropItem(b.getLocation(), is);
                            }

                            break;
                        }
                        case YELLOW_FLOWER: {
                            Random rand = new Random();
                            if (blockUnderneathIsFarmland(b) && rand.nextInt(2) == 0) {
                                ItemStack is = new ItemStack(Material.YELLOW_FLOWER);
                                is.setAmount(1);
                                b.getWorld().dropItem(b.getLocation(), is);
                            }

                            break;
                        }
                        case PUMPKIN: {
                            Random rand = new Random();
                            if (blockUnderneathIsFarmland(b) && rand.nextInt(2) == 0) {
                                ItemStack is = new ItemStack(Material.PUMPKIN);
                                is.setAmount(1);
                                b.getWorld().dropItem(b.getLocation(), is);
                            }

                            break;
                        }
                        case RED_MUSHROOM: {
                            Random rand = new Random();
                            if (blockUnderneathIsFarmland(b) && rand.nextInt(2) == 0) {
                                ItemStack is = new ItemStack(Material.RED_MUSHROOM);
                                is.setAmount(1);
                                b.getWorld().dropItem(b.getLocation(), is);
                            }

                            break;
                        }
                        case BROWN_MUSHROOM: {
                            Random rand = new Random();
                            if (blockUnderneathIsFarmland(b) && rand.nextInt(2) == 0) {
                                ItemStack is = new ItemStack(Material.BROWN_MUSHROOM);
                                is.setAmount(1);
                                b.getWorld().dropItem(b.getLocation(), is);
                            }

                            break;
                        }
                        case SUGAR_CANE_BLOCK: {
                            Random rand = new Random();
                            if (blockUnderneathIsFarmland(b) && rand.nextInt(2) == 0) {
                                ItemStack is = new ItemStack(Material.SUGAR_CANE);
                                is.setAmount(1);
                                b.getWorld().dropItem(b.getLocation(), is);
                            }

                            break;
                        }
                        case CACTUS: {
                            Random rand = new Random();
                            if (blockUnderneathIsFarmland(b) && rand.nextInt(2) == 0) {
                                ItemStack is = new ItemStack(Material.CACTUS);
                                is.setAmount(1);
                                b.getWorld().dropItem(b.getLocation(), is);
                            }

                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }

            if (parent.woolMode()) {
                if (b.getType() != Material.CROPS) {
                    return;
                }

                // if the crops has already grown, it cannot be changed anymore
                if (b.getData() != 0) {
                    return;

                }

                Player p = event.getPlayer();
                ItemStack is = p.getItemInHand();

                // if it wasn't touched with wool, return
                if (is.getType() != Material.WOOL) {
                    return;
                }


                if (parent.addPlant(b, is)) {
                    event.setCancelled(true);
                }
            }


        } catch (Exception e) {
            logger.log(Level.WARNING, "FarmMaster: error: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        try {
            if (event.isCancelled()) {
                return;
            }

            if (parent.naturalMode()) {
                Block b = event.getBlockPlaced();

                if (this.blockUnderneathIsFarmland(b)) {

                    ItemStack is = new ItemStack(b.getType());
                    is.setAmount(1);
                    if (parent.addPlantNaturally(b, is)) {
                    }

                }
            }

        } catch (Exception e) {
            logger.log(Level.WARNING, "FarmMaster: error: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onBlockRightClick(BlockRightClickEvent event) {
        try {
            if (parent.naturalMode()) {
                Block b = event.getBlock();

                if (b.getType() == Material.SOIL) {

                    int y = b.getY() + 1;
                    if(y > 126)
                    {
                        return;
                    }

                    Block plantBlock = b.getWorld().getBlockAt(b.getX(), y, b.getZ());

                    if (parent.addPlantNaturally(plantBlock, event.getItemInHand())) {
                        ItemStack is = new ItemStack(event.getItemInHand().getType());
                        is.setAmount(1);
                        event.getPlayer().getInventory().removeItem(is);
                    }

                }
            }

        } catch (Exception e) {
            logger.log(Level.WARNING, "FarmMaster: error: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void onBlockPhysics(BlockPhysicsEvent event) {
        try {
            if (event.isCancelled()) {
                return;
            }

            if (event.getBlock().getType() != Material.CROPS) {
                return;
            }

            parent.getPlantDB().UpdatePlant(event.getBlock());

        } catch (Exception e) {
            logger.log(Level.WARNING, "FarmMaster: error: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }
}
