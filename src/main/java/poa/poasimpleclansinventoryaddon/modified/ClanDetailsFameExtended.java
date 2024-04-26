package poa.poasimpleclansinventoryaddon.modified;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCComponentImpl;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCFrame;
import net.sacredlabyrinth.phaed.simpleclans.ui.frames.ClanDetailsFrame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poa.poasimpleclansinventoryaddon.PoaSimpleClansInventoryAddon;
import poa.poasimpleclansinventoryaddon.util.InventoryHandler;

import java.util.List;

public class ClanDetailsFameExtended extends ClanDetailsFrame {


    public ClanDetailsFameExtended(@Nullable SCFrame parent, @NotNull Player viewer, @NotNull Clan clan) {
        super(parent, viewer, clan);
    }

    @Override
    public void createComponents(){
        super.createComponents();
        SCComponentImpl item = new SCComponentImpl("§fInventory", List.of("§7Clan's Inventory"), Material.CHEST, 53);
        item.setListener(ClickType.LEFT, () -> {
            Player player = getViewer();
            ClanPlayer clanPlayer = PoaSimpleClansInventoryAddon.sc.getClanManager().getClanPlayer(player);
            Clan clan = clanPlayer.getClan();
            if(clan == null) {
                player.closeInventory();
                player.sendRichMessage("<red>No clan found");
                return;
            }

            if(PoaSimpleClansInventoryAddon.config.getBoolean("OnlyTrustedCanAccess")){
                if(!clanPlayer.isTrusted()){
                    player.closeInventory();
                    player.sendRichMessage("<red>Only trusted players can open this");
                    return;
                }
            }

            player.openInventory(InventoryHandler.getInventoryHandler(clan, true).getInventory());
        });

        item.setPermission(ClickType.LEFT, "poa.simpleclans.inventory");
        this.add(item);
    }


}
