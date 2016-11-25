package com.uom.cse.bbm.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author T.Nila find the nearest distance
 *
 */
public class NearbyDonor {
	public List<Long> findNearbyDonors(String[] distance) {
		List<Long> distanceInLong = new ArrayList<Long>();
		if (!(distance.length == 0)) {

			for (int i = 0; i < distance.length; i++) {
				distanceInLong.add(Long.parseLong(distance[i]));
			}
			// use Collections.sort to sort the list
			Collections.sort(distanceInLong);
			return distanceInLong;
		} else
			return distanceInLong;

	}
}
