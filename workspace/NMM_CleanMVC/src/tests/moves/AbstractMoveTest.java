package tests.moves;

import static org.junit.Assert.*;

import move.AbstractMove;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AbstractMoveTest {
	
	private AbstractMove pmR, pmB, rmR, rmB, mmR, mmB;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		pmR = new PlacementMove("NNNNNNNNNNNNNNNNNNNNNNNN", 'R', 0);
		pmB = new PlacementMove("NNNNNNNNNNNNNNNNNNNNNNNN", 'B', 0);
		
		rmR = new RemovalMove("BNNNNNNNNNNNNNNNNNNNNNNN", 'R', 0);
		rmB = new RemovalMove("RNNNNNNNNNNNNNNNNNNNNNNN", 'B', 0);
		
		mmR = new MovementMove("RNNNNNNNNNNNNNNNNNNNNNNN", 'R', 0, 1);
		mmB = new MovementMove("BNNNNNNNNNNNNNNNNNNNNNNN", 'B', 0, 1);
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetStateActedOn(){
		
		assertTrue(pmR.getStateActedOn().equals("NNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(pmR.getStateActedOn().equals("NNNNNNNRNNNNNNNNNNNNNNNN"));
		
		assertTrue(pmB.getStateActedOn().equals("NNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(pmB.getStateActedOn().equals("NNNNNNNRNNNNNNNNNNNNNNNN"));
		
		assertTrue(rmR.getStateActedOn().equals("BNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(rmR.getStateActedOn().equals("NNNNNNNRNNNNNNNNNNNNNNNN"));
		
		assertTrue(rmB.getStateActedOn().equals( "RNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(rmB.getStateActedOn().equals("NNNNNNNRNNNNNNNNNNNNNNNN"));
		
		assertTrue(mmR.getStateActedOn().equals("RNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(mmR.getStateActedOn().equals("NNNNNNNRNNNNNNNNNNNNNNNN"));
		
		assertTrue(mmB.getStateActedOn().equals( "BNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(mmB.getStateActedOn().equals("NNNNNNNRNNNNNNNNNNNNNNNN"));
		
	}

	@Test
	public void testGetAction() {
		
		assertTrue(pmR.getAction() == 'P');
		assertFalse(pmR.getAction() == 'R');
		
		assertTrue(pmB.getAction() == 'P');
		assertFalse(pmB.getAction() == 'R');
		
		assertTrue(rmR.getAction() == 'R');
		assertFalse(rmR.getAction() == 'P');
		
		assertTrue(rmB.getAction() == 'R');
		assertFalse(rmB.getAction() == 'P');
		
		assertTrue(mmR.getAction() == 'M');
		assertFalse(mmR.getAction() == 'R');
		
		assertTrue(mmB.getAction() == 'M');
		assertFalse(mmB.getAction() == 'P');
		
	}

	@Test
	public void testGetPlayerColour() {
		
		assertTrue(pmR.getPlayerColour() == 'R');
		assertFalse(pmR.getPlayerColour() == 'B');
		
		assertTrue(pmB.getPlayerColour() == 'B');
		assertFalse(pmB.getPlayerColour() == 'R');
		
		assertTrue(rmR.getPlayerColour() == 'R');
		assertFalse(rmR.getPlayerColour() == 'B');
		
		assertTrue(rmB.getPlayerColour() == 'B');
		assertFalse(rmB.getPlayerColour() == 'R');
		
		assertTrue(mmR.getPlayerColour() == 'R');
		assertFalse(mmR.getPlayerColour() == 'B');
		
		assertTrue(mmB.getPlayerColour() == 'B');
		assertFalse(mmB.getPlayerColour() == 'R');
		
	}

	@Test
	public void testGetPlayerID() {
		assertTrue(pmR.getPlayerID() == 1);
		assertFalse(pmR.getPlayerID() == 2);
		
		assertTrue(pmB.getPlayerID() == 2);
		assertFalse(pmB.getPlayerID() == 1);
		
		assertTrue(rmR.getPlayerID() == 1);
		assertFalse(rmR.getPlayerID() == 2);
		
		assertTrue(rmB.getPlayerID() == 2);
		assertFalse(rmB.getPlayerID() == 1);
		
		assertTrue(mmR.getPlayerID() == 1);
		assertFalse(mmR.getPlayerID() == 2);
		
		assertTrue(mmB.getPlayerID() == 2);
		assertFalse(mmB.getPlayerID() == 1);
	}

}
