package poa.poasimpleclansinventoryaddon.commands;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poa.poasimpleclansinventoryaddon.PoaSimpleClansInventoryAddon;
import poa.poasimpleclansinventoryaddon.util.InventoryHandler;

import javax.swing.plaf.InsetsUIResource;
import java.util.ArrayList;
import java.util.List;

public class ClanInventoryCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            SimpleClans scPlugin = PoaSimpleClansInventoryAddon.sc;
            Clan clan = null;
            boolean open = false;
            if (args.length == 0) {
                ClanPlayer clanPlayer = scPlugin.getClanManager().getClanPlayer(player);

                if (clanPlayer == null) {
                    player.sendRichMessage("<red>You are not in a clan");
                    return false;
                }

                clan = clanPlayer.getClan();
                if(PoaSimpleClansInventoryAddon.config.getBoolean("OnlyTrustedCanAccess")){
                    if(clanPlayer.isTrusted())
                        open = true;
                }
                else
                    open = true;
            } else {
                if (!player.hasPermission("poa.simpleclans.inventory.other")) {
                    player.sendRichMessage("<red>You do not have permission poa.claninventory.other");
                    return false;
                }
                if(args.length < 2){
                    player.sendRichMessage("<red>/claninventory <player/clan> <input>");
                    return false;
                }
                switch (args[0].toLowerCase()){
                    case "player" -> {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                        ClanPlayer clanPlayer = scPlugin.getClanManager().getClanPlayer(target.getUniqueId());

                        if (clanPlayer == null) {
                            player.sendRichMessage("<red>" + target.getName() + " is not in a clan");
                            return false;
                        }

                        clan = clanPlayer.getClan();
                    }
                    case "clan" -> {
                        clan = PoaSimpleClansInventoryAddon.sc.getClanManager().getClan(args[1]);
                    }
                }
                
                open = true;
            }
            if (clan == null)
                return false;

            InventoryHandler inventoryHandler = InventoryHandler.getInventoryHandler(clan, true);


            if (open)
                player.openInventory(inventoryHandler.getInventory());
            else
                player.sendRichMessage("<red>You cannot open this");

        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("poa.simpleclans.inventory.other"))
            return null;
        List<String> tr = new ArrayList<>();
        List<String> list = new ArrayList<>();

        if(args.length == 1)
            list = List.of("Player", "Clan");

        else if (args.length == 2){
            if(args[0].equalsIgnoreCase("clan"))
                list = PoaSimpleClansInventoryAddon.sc.getClanManager().getClans().stream().map(Clan::getTag).toList();
            else if (args[0].equalsIgnoreCase("player"))
                list = Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).toList();
        }

        for (String s : list) {
            if(s.toLowerCase().startsWith(args[args.length-1].toLowerCase()))
                tr.add(s);
        }

        return tr;
    }
}
