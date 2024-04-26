package poa.poasimpleclansinventoryaddon.events;

import net.sacredlabyrinth.phaed.simpleclans.events.FrameOpenEvent;
import net.sacredlabyrinth.phaed.simpleclans.ui.InventoryDrawer;
import net.sacredlabyrinth.phaed.simpleclans.ui.SCFrame;
import net.sacredlabyrinth.phaed.simpleclans.ui.frames.ClanDetailsFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import poa.poasimpleclansinventoryaddon.PoaSimpleClansInventoryAddon;
import poa.poasimpleclansinventoryaddon.modified.ClanDetailsFameExtended;

public class InventoryOpen implements @NotNull Listener {




    @EventHandler
    public void onInventoryOpen(FrameOpenEvent e){
        SCFrame frame = e.getFrame();
        if(frame instanceof ClanDetailsFameExtended)
            return;
        if(frame instanceof ClanDetailsFrame f){
            e.setCancelled(true);
            InventoryDrawer.open(new ClanDetailsFameExtended(f, e.getPlayer(), PoaSimpleClansInventoryAddon.sc.getClanManager().getClanByPlayerUniqueId(e.getPlayer().getUniqueId())));




            //Bukkit.getScheduler().runTaskLater(PoaSimpleClansInventoryAddon.INSTANCE, () -> InventoryDrawer.open(f), 1L);

        }
    }

}
