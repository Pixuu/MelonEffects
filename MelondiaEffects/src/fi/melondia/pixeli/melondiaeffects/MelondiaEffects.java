package fi.melondia.pixeli.melondiaeffects;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import fi.melondia.pixeli.melondiaeffects.commands.Commands;
import fi.melondia.pixeli.melondiaeffects.commands.gadget2;

public class MelondiaEffects extends JavaPlugin implements Listener {
	public List<String> lista = new ArrayList<String>();

	public void onEnable() {
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = getLogger();

		getCommand("meffect1").setExecutor(new Commands(this));
		getCommand("meffect2").setExecutor(new gadget2(this));

		logger.info(pdfFile.getName() + " on enabloitu (Versio" + pdfFile.getVersion() + ")");
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = getLogger();

		logger.info(pdfFile.getName() + " on disabloitu (Versio" + pdfFile.getVersion() + ")");
	}

	@EventHandler
	public void onTransform(EntityChangeBlockEvent e) {
		if (e.getEntityType().equals(EntityType.FALLING_BLOCK)) {
			if (lista.contains(String.valueOf(((FallingBlock) e.getEntity()).getBlockId()))) {
				e.getEntity().remove();
				e.setCancelled(true);
				lista.remove(String.valueOf(((FallingBlock) e.getEntity()).getBlockId()));
			}
		}
	}

	@EventHandler
	public void onInt(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = e.getPlayer();

			ItemStack is = player.getItemInHand();

			if (is.hasItemMeta()) {
				ItemMeta im = is.getItemMeta();

				if (im.hasDisplayName()) {
					if (im.getDisplayName().equalsIgnoreCase("Melon") && is.getType().equals(Material.MELON_BLOCK)) {
						player.performCommand("meffect1");
					}
					if (im.getDisplayName().equalsIgnoreCase("Melon2") && is.getType().equals(Material.MELON_BLOCK)) {
						player.performCommand("meffect2");
					}
				}
			}
		}
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent event) {
		Entity e = event.getEntity();
				
		Player player = (Player) e;
		player.sendMessage("e");
	}
	
}
