package fi.melondia.pixeli.melondiaeffects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerAction;

import fi.melondia.pixeli.melondiaeffects.commands.Commands;
import fi.melondia.pixeli.melondiaeffects.commands.gadget2;
import fi.melondia.pixeli.melondiaeffects.listeners.EventListener;
import fi.melondia.pixeli.melondiaeffects.tasks.MelonThrowTask;

public class MelondiaEffects extends JavaPlugin {

	private List<String> fallingBlockIDs = new ArrayList<String>();
	private List<String> snowballIDs = new ArrayList<String>();
	private MelondiaEffects instance;

	// ProtocolLib start
	private ProtocolManager protocolManager;
	private Map<String, BukkitRunnable> protocolLibTasks = new HashMap<String, BukkitRunnable>();
	private Map<String, Long> protocolLibCooldown = new HashMap<String, Long>();
	//ProtocolLib end


	@Override
	public void onEnable() {
		instance = this;
		this.getCommand("meffect1").setExecutor(new Commands(this));
		this.getCommand("meffect2").setExecutor(new gadget2(this));
		new EventListener(this);

		// ProtocolLib start
		protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.ENTITY_ACTION) {

			@Override
			public void onPacketReceiving(final PacketEvent event) {
				if (event.getPacket().getPlayerActions().getValues().get(0).equals(PlayerAction.STOP_SNEAKING)) {
					if (protocolLibTasks.containsKey(event.getPlayer().getName())) {
						protocolLibTasks.get(event.getPlayer().getName()).cancel();
						protocolLibTasks.remove(event.getPlayer().getName());
					}
				} else if (event.getPacket().getPlayerActions().getValues().get(0).equals(PlayerAction.START_SNEAKING)) {
					if (protocolLibCooldown.containsKey(event.getPlayer().getName())) {
						long secondsLeft = ((protocolLibCooldown.get(event.getPlayer().getName()) / 1000) + 1) - (System.currentTimeMillis() / 1000);
						if (secondsLeft > 0) {
							return;
						}
					}
					protocolLibCooldown.put(event.getPlayer().getName(), System.currentTimeMillis());
					if (protocolLibTasks.containsKey(event.getPlayer().getName())) {
						protocolLibTasks.get(event.getPlayer().getName()).cancel();
						protocolLibTasks.remove(event.getPlayer().getName());
					}
					BukkitRunnable runnable = new BukkitRunnable() {

						public void run() {
							ItemStack type = new ItemStack(Material.MELON);
							ItemMeta im = type.getItemMeta();
							im.setDisplayName(String.valueOf(new Random().nextInt(10000000)));
							type.setItemMeta(im);
							Item item = (Item) event.getPlayer().getWorld().dropItem(event.getPlayer().getEyeLocation(), type);
							float x = (float) -.2 + (float) (Math.random() * ((.2 - -.2) + .1));
							float y = (float) -.3 + (float) (Math.random() * ((.3 - -.3) + .1));
							float z = (float) -.2 + (float) (Math.random() * ((.2 - -.2) + .1));
							item.setVelocity(new Vector(x, y, z).normalize().multiply(1));
							item.setPickupDelay(Integer.MAX_VALUE);
							item.getWorld().playSound(item.getLocation(), Sound.CHICKEN_EGG_POP, 2, 0);
							new MelonThrowTask(item, instance).runTaskTimer(plugin, 1L, 1L);
						}
					};
					runnable.runTaskTimer(plugin, 0L, 20L);
					protocolLibTasks.put(event.getPlayer().getName(), runnable);
				}
			}
		});
		// ProtocolLib end
	}

	@Override
	public void onDisable() {
		
	}
	public List<String> getFallingBlockIDList() {
		return this.fallingBlockIDs;
	}
	
	public List<String> getsnowballIDs() {
		return this.snowballIDs;
	}
	
}
