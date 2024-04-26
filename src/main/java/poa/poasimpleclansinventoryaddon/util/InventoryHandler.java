package poa.poasimpleclansinventoryaddon.util;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import poa.poasimpleclansinventoryaddon.PoaSimpleClansInventoryAddon;
import poa.poasimpleclansinventoryaddon.util.holders.ClanInventoryHolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InventoryHandler {

    public static Map<Clan, InventoryHandler> dataMap = new HashMap<>();

    public static File folder;



    static {
        folder = new File(PoaSimpleClansInventoryAddon.INSTANCE.getDataFolder(), "Inventories");
        folder.mkdirs();

        long delay = PoaSimpleClansInventoryAddon.INSTANCE.getConfig().getLong("SaveFrequency") * 20;
        Bukkit.getScheduler().runTaskTimer(PoaSimpleClansInventoryAddon.INSTANCE, () -> {
            if (dataMap.values().isEmpty())
                return;

            for (InventoryHandler value : dataMap.values()) {
                value.setFile();
                value.saveAsync();
            }

        }, delay, delay);
    }


    public static InventoryHandler getInventoryHandler(Clan clan, boolean createIfNull) {
        if (dataMap.containsKey(clan))
            return dataMap.get(clan);

        if (!createIfNull)
            return null;

        return new InventoryHandler(clan);
    }

    public static InventoryHandler getInventoryHandler(OfflinePlayer player, boolean createIfNull) {
        ClanPlayer clanPlayer = PoaSimpleClansInventoryAddon.sc.getClanManager().getClanPlayer(player.getUniqueId());


        if (clanPlayer == null)
            return null;

        return getInventoryHandler(clanPlayer.getClan(), createIfNull);
    }

    @Getter
    Clan clan;
    @Getter
    Inventory inventory;
    FileConfiguration yml;
    File file;


    @SneakyThrows
    public InventoryHandler(Clan clan) {
        this.clan = clan;
        this.inventory = Bukkit.createInventory(new ClanInventoryHolder(), 54, MiniMessage.miniMessage().deserialize("<gold>" + clan.getName()));

        this.file = new File(folder, clan.getTag());
        this.file.createNewFile();

        this.yml = YamlConfiguration.loadConfiguration(this.file);
        this.yml.load(this.file);


        if (this.yml.isConfigurationSection("Items")) {
            for (int i = 0; i < 54; i++) {
                if (!this.yml.isSet("Items." + i))
                    continue;

                inventory.setItem(i, this.yml.getItemStack("Items." + i));
            }
        }

        dataMap.put(clan, this);
    }

    @SneakyThrows
    public void setFile() {
        for (int i = 0; i < 54; i++) {
            this.yml.set("Items." + i, null);
            ItemStack item = inventory.getItem(i);
            if (item == null || item.isEmpty() || item.getType().isAir())
                continue;

            this.yml.set("Items." + i, item);
        }

        this.yml.save(this.file);
    }


    public void saveAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(PoaSimpleClansInventoryAddon.INSTANCE, () -> {
            try {
                this.yml.save(this.file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SneakyThrows
    public void save() {
        this.yml.save(this.file);
    }


}
