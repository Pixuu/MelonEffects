package fi.melondia.pixeli.melondiaeffects.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

import fi.melondia.pixeli.melondiaeffects.MelondiaEffects;

public class gadget2 implements CommandExecutor {

	public List<Player> playerOnDeny = new ArrayList<Player>();

	public gadget2(MelondiaEffects plugin) {
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to use this command");
			return false;
		}
		Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("meffect2")) {
			if (!playerOnDeny.contains(player)) {

				Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2))
						.toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
				Snowball snow = player.getWorld().spawn(loc, Snowball.class);
				snow.setShooter(player);
				snow.setVelocity(player.getEyeLocation().getDirection().multiply(2));
			}
		}

		return true;
	}
		
}
