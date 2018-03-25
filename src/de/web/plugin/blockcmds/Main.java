package de.web.plugin.blockcmds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public void onEnable() {
		
		File dir = new File("plugins/BlockCommands/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File("plugins/BlockCommands/", "config.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				ArrayList<String> cmds = new ArrayList<String>();
				ArrayList<String> bcmd = new ArrayList<String>();
				ArrayList<String> alw = new ArrayList<>();
				alw.add("web44");
				bcmd.add("op");
				bcmd.add("deop");
				cmds.add("gm");
				cmds.add("gamemode");
				cfg.set("Prefix", "&eCrazyzone &8|");
				cfg.set("NoPerm", "&cYou do not have permission to do that");
				cfg.set("BlockedMsg", "&cYou have executed a restricted Command");
				cfg.set("AntiBypass", true);
				cfg.set("AntiBypassMsg", "&cYou are not allowed to use ':'");
				cfg.set("BlockMsg", "&cThis Command is blocked");
				cfg.set("BlockMsgAlw", "&cYou have executed a blocked Command");
				cfg.set("Allowed", alw);
				cfg.set("Blocked", bcmd);
				cfg.set("Restricted", cmds);
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Bukkit.getPluginManager().registerEvents(this, this);
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("bcc")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
								
				if (args.length != 3 && args.length != 2) {
					p.sendMessage(getPrefix() + " §cSyntax§c: §c/§cbcc §crestricted§c|§cblocked§c|§cplayer §cadd§c|§cremove§c|§clist §c[§cArgument§c]");
					return true;
				}
				
				
				if (args[1].equalsIgnoreCase("add")) {
					if (!isAllowed(p.getName())) {
						p.sendMessage(getPrefix() + " " + getNoPermMessage());
						return true;
					}
					if (args.length != 3) {
						p.sendMessage(getPrefix() + " §cBenutzung§c: §c/§cbcc §crestricted§c|§cblocked§c|§cplayer §cadd§c|§cremove§c|§clist §c[§cArgument§c]");
						return true;
					}
					if (args[0].equalsIgnoreCase("restricted")) {
						
						if (listed(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""))) {
							p.sendMessage(getPrefix() + " §cThis Command is already restricted");
							return true;
						}
						
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml"));
						ArrayList<String> list = (ArrayList<String>) cfg.getStringList("Restricted");
						list.add(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""));
						cfg.set("Restricted", list);
						try {
							cfg.save(new File("plugins/BlockCommands/", "config.yml"));
							p.sendMessage(getPrefix() + " §c" + args[2].toLowerCase().replaceAll(Pattern.quote("/"), "") + " §ewas added to §cRestricted");
						} catch (IOException e) {
							p.sendMessage(getPrefix() + " §cError");
						}
					} else if (args[0].equalsIgnoreCase("blocked")) {
						
						if (isBlocked(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""))) {
							p.sendMessage(getPrefix() + " §cThis Command is already blocked");
							return true;
						}
						
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml"));
						ArrayList<String> list = (ArrayList<String>) cfg.getStringList("Blocked");
						list.add(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""));
						cfg.set("Blocked", list);
						try {
							cfg.save(new File("plugins/BlockCommands/", "config.yml"));
							p.sendMessage(getPrefix() + " §c" + args[2].toLowerCase().replaceAll(Pattern.quote("/"), "") + " §ewas added to §cBlocked");
						} catch (IOException e) {
							p.sendMessage(getPrefix() + " §cError");
						}
					} else if (args[0].equalsIgnoreCase("player")) {
						
						if (isAllowed(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""))) {
							p.sendMessage(getPrefix() + " §cThis Player is already allowed");
							return true;
						}
						
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml"));
						ArrayList<String> list = (ArrayList<String>) cfg.getStringList("Allowed");
						list.add(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""));
						cfg.set("Allowed", list);
						try {
							cfg.save(new File("plugins/BlockCommands/", "config.yml"));
							p.sendMessage(getPrefix() + " §c" + args[2].toLowerCase().replaceAll(Pattern.quote("/"), "") + " §ewas added to §cAllowed");
						} catch (IOException e) {
							p.sendMessage(getPrefix() + " §cError");
						}
					} else {
						p.sendMessage(getPrefix() + " §cSyntax:§c: §c/§cbcc §crestricted§c|§cblocked§c|§cplayer §cadd§c|§cremove§c|§clist §c[§cArgument§c]");
						return true;
					}
				} else if (args[1].equalsIgnoreCase("remove")) {
					if (!isAllowed(p.getName())) {
						p.sendMessage(getPrefix() + " " + getNoPermMessage());
						return true;
					}
					if (args.length != 3) {
						p.sendMessage(getPrefix() + " §cSyntax§c: §c/§cbcc §crestricted§c|§cblocked§c|§cplayer §cadd§c|§cremove§c|§clist §c[§cArgument§c]");
						return true;
					}
					if (args[0].equalsIgnoreCase("restricted")) {
						
						if (!listed(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""))) {
							p.sendMessage(getPrefix() + " §cThis Command is not restricted");
							return true;
						}
						
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml"));
						ArrayList<String> list = (ArrayList<String>) cfg.getStringList("Restricted");
						list.remove(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""));
						cfg.set("Restricted", list);
						try {
							cfg.save(new File("plugins/BlockCommands/", "config.yml"));
							p.sendMessage(getPrefix() + " §c" + args[2].toLowerCase().replaceAll(Pattern.quote("/"), "") + " §ewas removed from §cRestricted");
						} catch (IOException e) {
							p.sendMessage(getPrefix() + " §cError");
						}
					} else if (args[0].equalsIgnoreCase("blocked")) {
						
						if (!isBlocked(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""))) {
							p.sendMessage(getPrefix() + " §cThis Command is not blocked");
							return true;
						}
						
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml"));
						ArrayList<String> list = (ArrayList<String>) cfg.getStringList("Blocked");
						list.remove(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""));
						cfg.set("Blocked", list);
						try {
							cfg.save(new File("plugins/BlockCommands/", "config.yml"));
							p.sendMessage(getPrefix() + " §c" + args[2].toLowerCase().replaceAll(Pattern.quote("/"), "") + " §ewas removed from §cBlocked");
						} catch (IOException e) {
							p.sendMessage(getPrefix() + " §cError");
						}
					} else if (args[0].equalsIgnoreCase("player")) {
						
						if (!isAllowed(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""))) {
							p.sendMessage(getPrefix() + " §cThis Player is not allowed");
							return true;
						}
						
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml"));
						ArrayList<String> list = (ArrayList<String>) cfg.getStringList("Allowed");
						list.remove(args[2].toLowerCase().replaceAll(Pattern.quote("/"), ""));
						cfg.set("Allowed", list);
						try {
							cfg.save(new File("plugins/BlockCommands/", "config.yml"));
							p.sendMessage(getPrefix() + " §c" + args[2].toLowerCase().replaceAll(Pattern.quote("/"), "") + " §ewas added to §cAllowed");
						} catch (IOException e) {
							p.sendMessage(getPrefix() + " §cError");
						}
					} else {
						p.sendMessage(getPrefix() + " §cSyntax§c: §c/§cbcc §crestricted§c|§cblocked§c|§cplayer §cadd§c|§cremove§c|§clist §c[§cArgument§c]");
						return true;
					}
				} else if (args[1].equalsIgnoreCase("list")) {
					if (!p.hasPermission("bc.list")) {
						p.sendMessage(getPrefix() + " " + getNoPermMessage());
						return true;
					}
					if (args[0].equalsIgnoreCase("restricted")) {
						p.sendMessage(getPrefix() + " §eRestricted§e: §c" + YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands", "config.yml")).getStringList("Restricted").toString());
					} else if (args[0].equalsIgnoreCase("blocked")) {
						p.sendMessage(getPrefix() + " §eBlocked§e: §c" + YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands", "config.yml")).getStringList("Blocked").toString());
					} else if (args[0].equalsIgnoreCase("player")) {
						p.sendMessage(getPrefix() + " §eAllowed§e: §c" + YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands", "config.yml")).getStringList("Allowed").toString());
					} else {
						p.sendMessage(getPrefix() + " §cSyntax§c: §c/§cbcc §crestricted§c|§cblocked§c|§cplayer §cadd§c|§cremove§c|§clist §c[§cArgument§c]");
						return true;
					}
				} else {
					p.sendMessage(getPrefix() + " §cSyntax§c: §c/§cbcc §crestricted§c|§cblocked§c|§cplayer §cadd§c|§cremove§c|§clist §c[§cArgument§c]");
					return true;
				}
				
			}
		}
		
		return true;
	}
	
	public static String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', (YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml")).getString("Prefix")));
	}
	
	public static String getNoPermMessage() {
		return ChatColor.translateAlternateColorCodes('&', (YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml")).getString("NoPerm")));
	}
	
	public static String getBlockedMessage() {
		return ChatColor.translateAlternateColorCodes('&', (YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml")).getString("BlockedMsg")));
	}
	
	public static boolean isAntibypassEnabled() {
		return YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml")).getBoolean("AntiBypass");
	}
	
	public static String getBypassMsg() {
		return ChatColor.translateAlternateColorCodes('&', (YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml")).getString("AntiBypassMsg")));
	}
	
	public static boolean listed(String cmd) {
		return (YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml")).getList("Restricted").contains(cmd));
	}
	
	public static boolean isBlocked(String cmd) {
		return (YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml")).getList("Blocked").contains(cmd));
	}
	
	public static String getBlockedMsg() {
		return ChatColor.translateAlternateColorCodes('&', (YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml")).getString("BlockMsg")));
	}
	
	public static boolean isAllowed(String player) {
		return (YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml")).getList("Allowed").contains(player.toLowerCase()));
	}
	
	public static String getBlockAlwMsg() {
		return ChatColor.translateAlternateColorCodes('&', (YamlConfiguration.loadConfiguration(new File("plugins/BlockCommands/", "config.yml")).getString("BlockMsgAlw")));
	}
	
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (e.getMessage().toLowerCase().split(" ")[0].replaceAll(Pattern.quote("/"), "").contains(":") && isAntibypassEnabled()) {
			e.setCancelled(true);
			p.sendMessage(getPrefix() + " " + getBypassMsg());
			Bukkit.getLogger().log(Level.WARNING, p.getName() + " tried to execute \"" + e.getMessage() + "\"");
			return;
		}
		
		if (isBlocked(e.getMessage().toLowerCase().split(" ")[0].replaceAll(Pattern.quote("/"), ""))) {
			if (isAllowed(p.getName())) {
				p.sendMessage(getPrefix() + " " + getBlockAlwMsg());
				e.setCancelled(false);
				Bukkit.getLogger().log(Level.INFO, p.getName() + " has executed \"" + e.getMessage() + "\"");
			} else {
				e.setCancelled(true);
				p.sendMessage(getPrefix() + " " + getBlockedMsg());
				Bukkit.getLogger().log(Level.WARNING, p.getName() + " tried to execute \"" + e.getMessage() + "\"");
			}
			return;
		}
		
		if (listed(e.getMessage().toLowerCase().split(" ")[0].replaceAll(Pattern.quote("/"), ""))) {
			if (p.hasPermission("bc.userestricted")) {
				p.sendMessage(getPrefix() + " " + getBlockedMessage());
				Bukkit.getLogger().log(Level.INFO, p.getName() + " has executed \"" + e.getMessage() + "\"");
			} else {
				p.sendMessage(getPrefix() + " " + getNoPermMessage());
				e.setCancelled(true);
				return;
			}
		}
	}
}
