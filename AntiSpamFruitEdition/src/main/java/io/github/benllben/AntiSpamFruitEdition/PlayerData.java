package io.github.benllben.AntiSpamFruitEdition;

import java.util.*;


public class PlayerData {
	// List holds a sequence of login attempts
	public List<Date> timeStamps = new ArrayList<Date>();
	
	public int bans = 0;
	
	private Date lookup1;
	private Date lookup2;
	
	
	// Class constructor
	public PlayerData(Date timestamp) {
		this.timeStamps.add(timestamp);

	}
	
	
	// Adds new login to timestamp list
	public void insertTimestamp(Date timestamp) {
		this.timeStamps.add(timestamp);

	}
	
	
	/* Function ShouldBan
	 * Checks if the player should be banned based on the number of login
	 * attempts made in the defined time frame
	 * 
	 * Returns: Total number of bans inclusive of newly determined ban
	 * 			or returns 0 if no ban is needed
	 */
	public int shouldBan(int maxLogins, int timeFrame) {
		// Exit early if it makes no sense to check
		if (timeStamps.size() < maxLogins ) { return 0; }	
		
		// Get the most recent login timestamp and the related timestep maxLogin's ago
		lookup1 = timeStamps.get(timeStamps.size() - 1);
		lookup2 = timeStamps.get(timeStamps.size() - maxLogins);
		
		// Calculate the time between the two timestamps in seconds
		long diffInSec = (lookup1.getTime() - lookup2.getTime())/1000;
		
		// Return ban number if there were too many logins in set period
		if (diffInSec < (timeFrame * 60)) {
			bans++;
			return bans;
		}
		
		return 0;
	}
}
