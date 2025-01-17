package com.yourname.fishingplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.enchantments.Enchantment;

public class FishingPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();

        // Check if the player is using the Mega Duper Angel
        if (player.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD &&
            player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Mega Duper Angel")) {

            // Check if the player has caught a fish
            if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                // Randomly select a reward
                ItemStack reward = getRandomReward();
                player.getInventory().addItem(reward);
                player.sendMessage("Du hast ein seltenes Item gefangen: " + reward.getType().toString());
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("Angel")) {
                // Give the player the Mega Duper Angel
                ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
                fishingRod.addEnchantment(Enchantment.DURABILITY, 3);
                ItemMeta meta = fishingRod.getItemMeta();
                meta.setDisplayName("Mega Duper Angel");
                fishingRod.setItemMeta(meta);

                player.getInventory().addItem(fishingRod);
                player.sendMessage("Du hast die Mega Duper Angel erhalten!");
                return true;
            }
        }
        return false;
    }

    private ItemStack getRandomReward() {
        FileConfiguration config = getConfig();
        List<String> rewards = config.getStringList("rewards");

        // Randomly select a reward from the config
        String reward = rewards.get(new Random().nextInt(rewards.size()));
        return new ItemStack(Material.getMaterial(reward));
    }
}
