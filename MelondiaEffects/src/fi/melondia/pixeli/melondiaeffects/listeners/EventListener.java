package fi.melondia.pixeli.melondiaeffects.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fi.melondia.pixeli.melondiaeffects.MelondiaEffects;

public class EventListener implements Listener {

	private MelondiaEffects plugin;

	public EventListener(MelondiaEffects plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	//falling block melon
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onTransform(EntityChangeBlockEvent e) {
		if (e.getEntityType().equals(EntityType.FALLING_BLOCK)) {
			if (plugin.getFallingBlockIDList().contains(String.valueOf(((FallingBlock) e.getEntity()).getBlockId()))) {
				e.getEntity().remove();
				e.setCancelled(true);
				plugin.getFallingBlockIDList().remove(String.valueOf(((FallingBlock) e.getEntity()).getBlockId()));
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

	//invi alkaa tästä
	public void openGUI(Player player) {
		Inventory inv = Bukkit.createInventory(null, 45, "Vip valikko");

		//pet
		ItemStack pet = new ItemStack(Material.MONSTER_EGG, 1, (short) 50);
		ItemMeta petMeta = pet.getItemMeta();
		petMeta.setDisplayName(ChatColor.GREEN + "Avaa Lemmikit valikko");
		pet.setItemMeta(petMeta);

		//trails
		ItemStack trails = new ItemStack(Material.BLAZE_POWDER);
		ItemMeta trailsMeta = trails.getItemMeta();
		trailsMeta.setDisplayName(ChatColor.GREEN + "Avaa Trails valikko");
		trails.setItemMeta(trailsMeta);

		//gadgetit...
		ItemStack gadgets = new ItemStack(Material.REDSTONE_TORCH_ON);
		ItemMeta gadgetsMeta = gadgets.getItemMeta();
		gadgetsMeta.setDisplayName(ChatColor.GREEN + "Avaa gadgets valikko");
		gadgets.setItemMeta(gadgetsMeta);

		//Osta vip valikko (Avaa buycraftin valikon)
		ItemStack vip = new ItemStack(Material.GOLD_INGOT);
		ItemMeta vipMeta = vip.getItemMeta();
		vipMeta.setDisplayName(ChatColor.GREEN + "Lue lisää BuyCraft kaupassamme (Avaa valikon)");
		vip.setItemMeta(vipMeta);

		//Vip -info linkki
		ItemStack info = new ItemStack(Material.BOOK);
		ItemMeta infoMeta = info.getItemMeta();
		infoMeta.setDisplayName(ChatColor.GREEN + "Avaa linkki josta katsoa VIP pelaajien oikeuksia");
		info.setItemMeta(infoMeta);

		inv.setItem(11, pet);
		inv.setItem(14, trails);
		inv.setItem(32, vip);
		inv.setItem(34, info);

		player.openInventory(inv);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("vip valikko"))
			return;
		Player player = (Player) event.getWhoClicked();
		event.setCancelled(true);

		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta()) {
			player.closeInventory();
			return;
		}
		switch (event.getCurrentItem().getType()) {
			case MONSTER_EGG:
				//En tiedä toimiiko koska Monster egissä id...
				player.chat("/pet select");
				player.closeInventory();
				break;
			case GOLD_INGOT:
				//Tällähetkellä servulla käytössä komento /vip joka avaa buycraft valikon, se pitää vielä vaihtaa -->
				player.chat("/vips");
				player.closeInventory();
				break;
			case BOOK:
				player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.STRIKETHROUGH + "----------------------------------");
				player.sendMessage(ChatColor.DARK_AQUA + "== ");
				player.sendMessage(ChatColor.DARK_AQUA + "== " + ChatColor.GREEN + "Katso vippien oikeuksia oheisesta linkistä:");
				player.sendMessage(ChatColor.DARK_AQUA + "== " + ChatColor.GREEN + "www.Melondia.fi/vip <- Klikkaa lnikkiä");
				player.sendMessage(ChatColor.DARK_AQUA + "== ");
				player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.STRIKETHROUGH + "----------------------------------");
				player.closeInventory();
				break;
			case BLAZE_POWDER:
				player.chat("/trails");
				player.closeInventory();
				break;
			case REDSTONE_TORCH_ON:
				player.chat("/gadgets");
				player.closeInventory();
				break;
			default:
				player.closeInventory();
				break;

		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action a = event.getAction();
		ItemStack is = event.getItem();

		if (a == Action.PHYSICAL || is == null || is.getType() == Material.AIR)
			return;

	}

	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e) {
		if ((e.getMessage().equalsIgnoreCase("/vip"))) {
			openGUI(e.getPlayer());
		}

	}
}
