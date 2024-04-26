package poa.poasimpleclansinventoryaddon;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import poa.poasimpleclansinventoryaddon.commands.ClanInventoryCommand;
import poa.poasimpleclansinventoryaddon.events.InventoryOpen;
import poa.poasimpleclansinventoryaddon.util.InventoryHandler;

import java.io.File;

public final class PoaSimpleClansInventoryAddon extends JavaPlugin {

    public static PoaSimpleClansInventoryAddon INSTANCE;
    public static SimpleClans sc;

    public static FileConfiguration config;

    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();
        config = getConfig();

        PluginManager pm = getServer().getPluginManager();
        Plugin plug = pm.getPlugin("SimpleClans");
        if (plug != null) {
            sc = (SimpleClans) plug;
        }

        pm.registerEvents(new InventoryOpen(), this);

        getCommand("claninventory").setExecutor(new ClanInventoryCommand());

    }


    @Override
    public void onDisable() {
        if(InventoryHandler.dataMap.isEmpty())
            return;

        for (InventoryHandler value : InventoryHandler.dataMap.values()) {
            value.setFile();
            value.save();
        }
    }
}
