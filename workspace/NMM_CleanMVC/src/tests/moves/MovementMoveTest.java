package tests.moves;

import static org.junit.Assert.*;

import move.MovementMove;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MovementMoveTest {
	
	private MovementMove mmR, mmB;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		mmR = new MovementMove("RNNNNNNNNNNNNNNNNNNNNNNN", 'R', 0, 1);
		mmB = new MovementMove("NNNNNNNNNNBNNNNNNNNNNNNN", 'B', 10, 11);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetStatePostAction() {
		
		assertTrue(mmR.getStatePostAction().equals("NRNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(mmR.getStatePostAction().equals("BNNNNNNNNNNNNNNNNNNNNNNN"));
		
		assertTrue(mmB.getStatePostAction().equals( "NNNNNNNNNNNBNNNNNNNNNNNN"));
		assertFalse(mmB.getStatePostAction().equals("NNNNNNNNNNBNNNNNNNNNNNNN"));
		
	}

	@Test
	public void testGetFrom() {
		
		assertTrue(mmR.getFrom() == 0);
		assertFalse(mmR.getFrom() == 25);
		
		assertTrue(mmB.getFrom() == 10);
		assertFalse(mmB.getFrom() == -1);
		
	}

	@Test
	public void testGetTo() {
		
		assertTrue(mmR.getTo() == 1);
		assertFalse(mmR.getTo() == 0);
		
		assertTrue(mmB.getTo() == 11);
		assertFalse(mmB.getTo() == 10);
		
	}

}
