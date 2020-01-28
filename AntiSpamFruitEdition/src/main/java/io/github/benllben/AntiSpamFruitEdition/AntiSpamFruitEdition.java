package io.github.benllben.AntiSpamFruitEdition;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;


public final class AntiSpamFruitEdition extends JavaPlugin implements Listener{
	
	private int maxLoginThreshold = 4;		// Max number of logins in a given timeframe
	private int thersholdMinutes = 2;		// Timeframe for the max number of logins (minutes)
	
	private int banLengthMinutes = 5;		// Length of first ban (minutes)
	private int banLengthMultipier = 2;		// Each consecutive ban's length is multiplied by this number
	
	private String name;					// Lets just re-use one variable to save memory cleanup
	private int shouldBanValue = 0;			// Lets just re-use one variable to save memory cleanup
	
	Map<String, PlayerData> logins = new Hashtable<String, PlayerData>();	// Holds player logins and related data
	
	@Override
	public void onEnable() {
		// Required to receive onPlayerJoin events
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		// Store a reference to the player and cache name
		Player playerjoined = e.getPlayer();
		name = playerjoined.getName();	
		
		if (!logins.containsKey(name)) {							// Insert unseen player into list
			logins.put(name, new PlayerData(new Date()));
		} else if (logins.containsKey(name)) {						// Update known players login
			logins.get(name).insertTimestamp(new Date());
			
			// Check if this player should be banned
			shouldBanValue = logins.get(name).shouldBan(maxLoginThreshold, thersholdMinutes);
			
			// Player should be banned if returned value is more then 0 bans
			if (shouldBanValue > 0) {
				// Calculate the ban length based on number of bans
				// TODO - Replace with predefined values in array pending advice
				int banLength = banLengthMinutes + ((shouldBanValue - 1) * banLengthMultipier);
				
				// Generate the expiry date for the ban
				Date expiryDate = new Date();
				expiryDate = DateUtils.addMinutes(expiryDate, banLength);
				
				// Add player to bukkit's ban list
				Bukkit.getBanList(Type.NAME).addBan(name, "Excessive Relogging", expiryDate, "Console");
				// We must also kick the player as adding them to the ban list does not remove them
				playerjoined.kickPlayer("Banned for excessive relogging");
			}	
		} else {
			// Something weird must have happend, just ignore it for saftey.
		}
		return;
	}
}

