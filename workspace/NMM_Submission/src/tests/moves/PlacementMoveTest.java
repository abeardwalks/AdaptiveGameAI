package tests.moves;

import static org.junit.Assert.*;

import move.PlacementMove;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PlacementMoveTest {

	private PlacementMove pmR, pmB;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		pmR = new PlacementMove("NNNNNNNNNNNNNNNNNNNNNNNN", 'R', 0);
		pmB = new PlacementMove("NNNNNNNNNNNNNNNNNNNNNNNN", 'B', 10);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetStatePostAction() {
		
		assertTrue(pmR.getStatePostAction().equals("RNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(pmR.getStatePostAction().equals("NNNNNNNNNNNNNNNNNNNNNNNN"));
		
		assertTrue(pmB.getStatePostAction().equals("NNNNNNNNNNBNNNNNNNNNNNNN"));
		assertFalse(pmB.getStatePostAction().equals("NNNNNNNNNNNNNNNNNNNNNNNN"));
		
	}

	@Test
	public void testGetPlacementIndex() {
		
		assertTrue(pmR.getPlacementIndex() == 0);
		assertFalse(pmR.getPlacementIndex() == 10);
		
		assertTrue(pmB.getPlacementIndex() == 10);
		assertFalse(pmB.getPlacementIndex() == 0);
		
	}

}
