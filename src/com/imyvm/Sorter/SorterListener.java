package com.imyvm.Sorter;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static me.crafter.mc.lockettepro.LocketteProAPI.isLocked;
import static me.crafter.mc.lockettepro.LocketteProAPI.isUser;

public class SorterListener implements Listener {
    private Sorter sorter;

    public SorterListener(Sorter it){
        sorter = it;
    }
    private static String tool = Sorter.getTool();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInventoryClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (!(p.getInventory().getItemInMainHand().getType().equals(Material.getMaterial(tool)))){
            return;
        }
        if (!(event.getAction().equals(Action.LEFT_CLICK_BLOCK))){
            return;
        }
        Block chest = event.getClickedBlock();
        if (sorter.getServer().getPluginManager().isPluginEnabled("LockettePro")){
            if (isLocked(chest)){
                if (!(isUser(chest, p))){
                    return;
                }
            }
        }
        if (chest.getState() instanceof Chest){
            Inventory inventory = ((Chest) chest.getState()).getInventory();
            SorterInv(inventory);
            p.spawnParticle(Particle.HEART, chest.getLocation().add(0,1,0),1);
            event.setCancelled(true);
        }

        if (chest.getState() instanceof ShulkerBox){
            Inventory inventory = ((ShulkerBox) chest.getState()).getInventory();
            SorterInv(inventory);
            p.spawnParticle(Particle.HEART, chest.getLocation().add(0,1,0),1);
            event.setCancelled(true);
        }
        if (chest.getType().equals(Material.ENDER_CHEST)){
            Inventory inventory = p.getEnderChest();
            SorterInv(inventory);
            p.spawnParticle(Particle.HEART, chest.getLocation().add(0,1,0),1);
            event.setCancelled(true);
        }
    }

    private boolean empty(Inventory inventory){
        for(ItemStack it : inventory.getContents())
        {
            if(it != null) return false;
        }
        return true;
    }

    public void SorterInv(Inventory inventory){
        if (!(empty(inventory))){
            Inventory midv = Bukkit.createInventory(null, inventory.getSize());
            ItemStack[] itemStacks = inventory.getStorageContents();
            for (int i = 0; i < inventory.getSize(); i++) {
                if ((itemStacks[i] != null) && !(itemStacks[i].getType().equals(Material.AIR))){
                    midv.addItem(itemStacks[i]);
                }
            }
            ItemStack[] itemStacks1 = midv.getStorageContents();
            List<ItemStack> sorted_itemStacks = new ArrayList<>();
            List<ItemStack> sorted_itemStacks1 = new ArrayList<>();
            List<Integer> amount = new ArrayList<>();
            List<String> names = new ArrayList<>();

            // Sort by amount
            for (ItemStack itemStack:itemStacks1){
                if (!(itemStack==null || itemStack.getType().equals(Material.AIR))){
                    amount.add(itemStack.getAmount());
                }
            }
            Integer[] strArr = amount.parallelStream().toArray(Integer[]::new);
            int[] sortedIndices = IntStream.range(0, strArr.length)
                    .boxed().sorted(Comparator.comparing(i -> strArr[i]))
                    .mapToInt(ele -> ele).toArray();
            ArrayUtils.reverse(sortedIndices);
            for (Integer j:sortedIndices){
                if (!(itemStacks1[j]==null || itemStacks1[j].getType().equals(Material.AIR))){
                    sorted_itemStacks.add(itemStacks1[j]);
                }
            }
            //Sort by name
            for (ItemStack itemStack:sorted_itemStacks){
                if (!(itemStack==null || itemStack.getType().equals(Material.AIR))){
                    if (itemStack.getItemMeta().hasDisplayName()){
                        names.add(itemStack.getItemMeta().getDisplayName());
                    }else {
                        names.add(itemStack.getType().name());
                    }
                }
            }

            String[] strArr1 = names.parallelStream().toArray(String[]::new);
            //Bukkit.broadcastMessage(String.join("#", strArr1));
            int[] sortedIndices1 = IntStream.range(0, strArr1.length)
                    .boxed().sorted(Comparator.comparing(i -> strArr1[i]))
                    .mapToInt(ele -> ele).toArray();
            for (Integer j:sortedIndices1){
                if (!(sorted_itemStacks.get(j)==null || sorted_itemStacks.get(j).getType().equals(Material.AIR))){
                    sorted_itemStacks1.add(sorted_itemStacks.get(j));
                }
            }

            inventory.setStorageContents(sorted_itemStacks1.parallelStream().toArray(ItemStack[]::new));
        }
    }

}
