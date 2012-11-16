package ru.BeYkeR_33.bkrchat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
 
public class main extends JavaPlugin {
	
	public static final Logger _log  = Logger.getLogger("Minecraft");
	static boolean usePEX = false;
	static boolean usePB = false;
 
	public void onEnable(){		
		//Спасибо DmitriyMX за функцию проверки пермишен плагина
		PluginManager pm = Bukkit.getPluginManager();
		if(pm.getPlugin("PermissionsEx") != null){
		   usePEX = true;
		}else if(pm.getPlugin("PermissionsBukkit") != null){
		   usePB = true;
		}else{
		   getLogger().warning("Permissions plugins not found!");
		}
		//И опять спасибо DmitriyMX за распаковку конфига. Туторы на его сайте DmitriyMX.ru 
		File fileConf = new File(getDataFolder(), "config.yml");
		if(!fileConf.exists()){
		    InputStream resourceAsStream = main.class.getResourceAsStream("/ru/BeYkeR_33/bkrchat/config.yml");
		    getDataFolder().mkdirs();
		    try {
		        FileOutputStream fos = new FileOutputStream(fileConf);
		        byte[] buff = new byte[65536];;
		        int n;
		        while((n = resourceAsStream.read(buff)) > 0){
		            fos.write(buff, 0, n);
		            fos.flush();
		        }
		        fos.close();
		        buff = null;
		      } catch (Exception e) {
		        e.printStackTrace();
	        }
	}    
		    getLogger().info("Сonfig loaded");
		
		FileConfiguration config = this.getConfig();
		getServer().getPluginManager().registerEvents(new Chat(config), this);
	}
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("chatversion")){
            sender.sendMessage("§aАвтор: BeYkeR_33," +
            		"§2Идея: DoGzzzб" +
            		"§7За основу плагина был взят плагин: RPchat");
            return true;
        }else if(command.getName().equalsIgnoreCase("mems")){
            File helpText = new File(getDataFolder(), "mems.txt");
            StringBuilder fullText = new StringBuilder();
            try {
                BufferedReader buffer = new BufferedReader(new FileReader(helpText));
                String line;
                while((line = buffer.readLine()) != null){
                    fullText.append(line).append("\n");
                }
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sender.sendMessage(fullText.toString());
            return true;
        }else if(command.getName().equalsIgnoreCase("github")){
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("update")){
                    sender.sendMessage("https://github.com/BeYkeRYkt/BkrChat");
                    return true;
                }
            }
        }        
        return false;
    }
	
	public static boolean hasPermission(Player player, String permission){
		   if(usePEX){
		       return PermissionsEx.getUser(player).has(permission);
		   }else if(usePB){
		       return player.hasPermission(permission);
		   }else{
		       return player.isOp();
		   }
		}
}