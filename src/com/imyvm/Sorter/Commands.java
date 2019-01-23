package com.imyvm.Sorter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Commands implements CommandExecutor {

    private Sorter plugin;

    public Commands(Sorter pl){
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player)){
            return false;
        }
        if (args.length==0 && label.equalsIgnoreCase("si")){
            if (!sender.hasPermission("sorter.inventory")){
                sender.sendMessage("You don't have the permission!");
                return false;
            }
            Player p = (Player) sender;
            Inventory midv = Bukkit.createInventory(null, 36);
            midv.setStorageContents(p.getInventory().getStorageContents());
            plugin.sorterListener.SorterInv(midv);
            p.getInventory().setStorageContents(midv.getStorageContents());
            p.updateInventory();
            p.sendMessage("Inventory Sorted!");
        }
        return true;
    }
}
