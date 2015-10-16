package fi.melondia.pixeli.melondiaeffects.tasks;


import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fi.melondia.pixeli.melondiaeffects.MelondiaEffects;


public class MelonThrowTask extends BukkitRunnable {

	private Item item;
	private MelondiaEffects plugin;
	
	public MelonThrowTask(Item item, MelondiaEffects plugin) {
		this.item = item;
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public void run() {
		if(item.isOnGround()) {
			shootFirework(item.getLocation());
			for(int i = 0; i < 5; i++) {
				Location loc = item.getLocation();
				ItemStack type = new ItemStack(Material.MELON);
				ItemMeta im = type.getItemMeta();
				im.setDisplayName(String.valueOf(new Random().nextInt(10000000)));
				type.setItemMeta(im);
				final Item ite = loc.getWorld().dropItem(loc, type);
				float x = (float) -.2 + (float) (Math.random() * ((.2 - -.2) + .1));
			    float y = (float) -.3 + (float) (Math.random() * ((.3 - -.3) + .1));
			    float z = (float) -.2 + (float) (Math.random() * ((.2 - -.2) + .1));
			    ite.setVelocity(new Vector(x,y+0.5,z).normalize().multiply(0.5));
			    ite.setPickupDelay(Integer.MAX_VALUE);
				ite.getWorld().playSound(item.getLocation(), Sound.EXPLODE, 2, 0);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new BukkitRunnable() {
					public void run() {
						ite.remove();
					}
				}, 40L);
			}
			item.remove();
			cancel();
		}
	}
	private void  shootFirework(Location loc) {
		Firework fw = (Firework) loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fm = fw.getFireworkMeta();
		Random r = new Random();
		int c1i = r.nextInt(16) +  1;
		int c2i = r.nextInt(16) + 1;
		Color c1 = getColour(c1i);
		Color c2 = getColour(c2i);
		FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(c1).withFade(c2).with(Type.BALL_LARGE).trail(false).build();
		fm.addEffect(effect);
		fm.setPower(1);
		fw.setFireworkMeta(fm);
	}
	private Color getColour(int c) {
		switch (c) {
		default:
		case 1:
			return Color.AQUA;
		case 2:
			return Color.BLACK;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.FUCHSIA;
		case 5:
			return Color.GRAY;
		case 6:
			return Color.GREEN;
		case 7:
			return Color.LIME;
		case 8:
			return Color.MAROON;
		case 9:
			return Color.NAVY;
		case 10:
			return Color.OLIVE;
		case 11:
			return Color.PURPLE;
		case 12:
			return Color.RED;
		case 13:
			return Color.SILVER;
		case 14:
			return Color.TEAL;
		case 15:
			return Color.WHITE;
		case 16:
			return Color.YELLOW;
		}
	}
}

