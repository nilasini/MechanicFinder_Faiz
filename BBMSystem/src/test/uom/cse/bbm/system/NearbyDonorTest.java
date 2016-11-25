package test.uom.cse.bbm.system;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.uom.cse.bbm.system.NearbyDonor;

import static org.junit.Assert.assertEquals;

public class NearbyDonorTest {
	public ExpectedException thrown = ExpectedException.none();
	private String[] distance;

	@Test
	public void test() {

		NearbyDonor nearby = new NearbyDonor();
		List<Long> outputList = new ArrayList<Long>();
		List<Long> expectedList = new ArrayList<Long>();
		distance = new String[] { "3454", "4565", "2345" };
		expectedList.add(Long.parseLong("2345"));
		expectedList.add(Long.parseLong("3454"));
		expectedList.add(Long.parseLong("4565"));
		outputList = nearby.findNearbyDonors(distance);
		for (int i = 0; i < outputList.size(); i++) {
			assertEquals(expectedList.get(i), outputList.get(i));
		}
		distance = new String[] {};
		NearbyDonor nearby1 = new NearbyDonor();
		outputList = nearby1.findNearbyDonors(distance);
		Assert.assertTrue(outputList.isEmpty());

	}

}
