package ru.BeYkeR_33.bkrchat;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.configuration.file.FileConfiguration;


@SuppressWarnings("deprecation")
public class Chat implements Listener {
	
	private double RangeMain;
	private double RangeWhispering;
	private double RangeShout;
	private double RangeAction;
	private double RangeFace;
	private boolean DeathMessage;
	private double ConfigChance;
	public static main plugin;
	
	public Chat(FileConfiguration config) {
		this.RangeMain = config.getDouble("Range.main", this.RangeMain);
		this.RangeWhispering = config.getDouble("Range.whispering", this.RangeWhispering);
		this.RangeShout = config.getDouble("Range.shout", this.RangeShout);
		this.RangeAction = config.getDouble("Range.action", this.RangeAction);
		this.RangeFace = config.getDouble("Range.face", this.RangeFace);
		this.DeathMessage = config.getBoolean("disableDeathMessage", this.DeathMessage);
		this.ConfigChance = config.getDouble("ConfigChance", this.ConfigChance);

	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent join){
		Player player = join.getPlayer();
		{
			player.sendMessage("§4[BkrChat] - §a Chat powered by BeYkeR_33 .");
			return;
		}
	}
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
	  	Player player = event.getPlayer();
		String message = "§4[BkrChat] - %1$s: %2$s";		
		String chatMessage = event.getMessage();	
		boolean ranged = true; // я не знаю как это назвать, поэтому ranged (органичен ли чат)
		double range = RangeMain; 
		
		if (chatMessage.startsWith("^")) {			
				ranged = false;
				if (main.hasPermission(player,"bkrchat.global")){
				message = "§4[BkrChat] - %1$s [Глобальный]: %2$s";
				chatMessage = ChatColor.GOLD+chatMessage.substring(2);
			}
			else {
				player.sendMessage("§4У вас нет прав писать в глобальный чат");
				event.setCancelled(true);
			}			
		}			
		
		
			if (chatMessage.startsWith("!")) {
			range = RangeShout;
			message = "§4[BkrChat] - %1$s кричит: %2$s";
			//chatMessage = ChatColor.BOLD+chatMessage.substring(1); жирный смотрится плохо
			chatMessage = ChatColor.RED+chatMessage.substring(1);
			}
			
			if (chatMessage.startsWith("@")) {
			range = RangeWhispering;
			message = "§4[BkrChat] - %1$s шепчет: %2$s";
			chatMessage = ChatColor.ITALIC+chatMessage.substring(1);
			chatMessage = ChatColor.GRAY+chatMessage;			
			}
			
			if (chatMessage.startsWith("***")) {
			range = RangeAction;
			chatMessage = ChatColor.LIGHT_PURPLE+chatMessage.substring(3);
			double chance = Math.random()*100;
			String luck=ChatColor.RED+"(неудачно)"+ChatColor.LIGHT_PURPLE;
			if (chance<ConfigChance){
			luck = ChatColor.GREEN+"(удачно)"+ChatColor.LIGHT_PURPLE;
			}
			message = ChatColor.LIGHT_PURPLE+"**%1$s %2$s "+luck+" **";
			}
			
			if (chatMessage.startsWith("**")) {
			range = RangeAction;
			message = ChatColor.LIGHT_PURPLE+"**%1$s %2$s**";
			chatMessage = chatMessage.substring(2);	
			}
			
			if (chatMessage.startsWith("O_O")) {
				range = RangeFace;
				message = "§4[BkrChat] - %1$s показывает мем: §6O_O ";
				//chatMessage = ChatColor.BOLD+chatMessage.substring(1); жирный смотрится плохо
				chatMessage = ChatColor.GREEN+chatMessage.substring(1);
			}
			
			if (chatMessage.startsWith("troll")) {
				range = RangeFace;
				message = "§4[BkrChat] - %1$s показывает мем: §6*Trollface* ";
				//chatMessage = ChatColor.BOLD+chatMessage.substring(1); жирный смотрится плохо
				chatMessage = ChatColor.RED+chatMessage.substring(1);
			}
			
			if (chatMessage.startsWith("megusta")) {
				range = RangeFace;
				message = "§4[BkrChat] - %1$s показывает мем: §6*MeGusta* ";
				//chatMessage = ChatColor.BOLD+chatMessage.substring(1); жирный смотрится плохо
				chatMessage = ChatColor.RED+chatMessage.substring(1);
			}
			
			if (chatMessage.startsWith("facepalm")) {
				range = RangeFace;
				message = "§4[BkrChat] - %1$s показывает мем: §6*Facepalm* ";
				//chatMessage = ChatColor.BOLD+chatMessage.substring(1); жирный смотрится плохо
				chatMessage = ChatColor.RED+chatMessage.substring(1);
			}
			
		if (ranged)
		{	
			event.getRecipients().clear();
			event.getRecipients().addAll(this.getLocalRecipients(player, message, range));
		}
		event.setFormat(message);
		event.setMessage(chatMessage);	
	    }
	/*убираем сообщение о смерти*/
	 @EventHandler(priority = EventPriority.NORMAL)
	 public void onEntityDeath(EntityDeathEvent event) {
		 if (DeathMessage){
			 if (event instanceof PlayerDeathEvent) {
				 PlayerDeathEvent deathEvent = (PlayerDeathEvent) event;
				 deathEvent.setDeathMessage(null);
         	}
		 }
	 }
		
	 //способ органичения слышимости с одного распростаненого плага (ChatManager)
	protected List<Player> getLocalRecipients(Player sender, String message, double range) {
		Location playerLocation = sender.getLocation();
		List<Player> recipients = new LinkedList<Player>();
		double squaredDistance = Math.pow(range, 2);
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if (!recipient.getWorld().equals(sender.getWorld())) {
				continue;
			}
			if (playerLocation.distanceSquared(recipient.getLocation()) > squaredDistance) {
				continue;
			}
			recipients.add(recipient);
		}
		return recipients;		
	}
}
