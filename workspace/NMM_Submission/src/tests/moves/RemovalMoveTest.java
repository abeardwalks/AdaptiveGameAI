package tests.moves;

import static org.junit.Assert.*;

import move.RemovalMove;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RemovalMoveTest {
	
	private RemovalMove rmR, rmB;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		rmR = new RemovalMove("BNNNNNNNNNNNNNNNNNNNNNNN", 'R', 0);
		rmB = new RemovalMove("NNNNNNNNNNRNNNNNNNNNNNNN", 'B', 10);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetStatePostAction() {
		
		assertTrue(rmR.getStatePostAction().equals("NNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(rmR.getStatePostAction().equals("BNNNNNNNNNNNNNNNNNNNNNNN"));
		
		assertTrue(rmB.getStatePostAction().equals( "NNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(rmB.getStatePostAction().equals("NNNNNNNNNNBNNNNNNNNNNNNN"));
		
	}

	@Test
	public void testGetRemovalIndex() {
		
		assertTrue(rmR.getRemovalIndex() == 0);
		assertFalse(rmR.getRemovalIndex() == 10);
		
		assertTrue(rmB.getRemovalIndex() == 10);
		assertFalse(rmB.getRemovalIndex() == 0);
		
	}

}
