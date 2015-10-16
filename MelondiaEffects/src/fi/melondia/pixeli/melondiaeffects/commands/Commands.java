package fi.melondia.pixeli.melondiaeffects.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fi.melondia.pixeli.melondiaeffects.MelondiaEffects;

public class Commands implements CommandExecutor {

	private MelondiaEffects plugin;

	public Map<String, Long> cooldown = new HashMap<String, Long>();

	public Commands(MelondiaEffects plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to use this command");
			return false;
		}
		Player player = (Player) sender;

		if (command.getName().equalsIgnoreCase("meffect1")) {
			if (cooldown.containsKey(player.getName())) {
				long secondsLeft = ((cooldown.get(player.getName()) / 1000) + 5) - (System.currentTimeMillis() / 1000);
				if (secondsLeft > 0) {
					player.sendMessage("§aVoit käyttää tätä uudelleen " + secondsLeft + " sekunnin jälkeen.");
					return true;
				}
			}
			cooldown.put(player.getName(), System.currentTimeMillis());
			for (int i = 0; i < 20; i++) {
				float x = (float) -2 + (float) (Math.random() * ((2 - -2) + 1));
				float y = (float) -3 + (float) (Math.random() * ((3 - -3) + 1));
				float z = (float) -2 + (float) (Math.random() * ((2 - -2) + 1));

				FallingBlock fb = player.getWorld().spawnFallingBlock(player.getLocation(), Material.MELON_BLOCK, (byte) 0);
				plugin.getFallingBlockIDList().add(String.valueOf(fb.getBlockId()));
				fb.setDropItem(false);
				fb.setVelocity(new Vector(x, y, z).normalize());
			}
		}

		return true;
	}
}
