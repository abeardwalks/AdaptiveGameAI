package tests.model;

import static org.junit.Assert.*;
import interfaces.BoardFacadeInterface;

import model.board.Model;
import move.AbstractMove;
import move.PlacementMove;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelMutatorTest {
	
	private BoardFacadeInterface model;
	
	private AbstractMove pmR1, pmR2, pmR3, pmB1, pmB2, pmB3;	//placement
	private AbstractMove rmR1, rmB1;							//removal
	private AbstractMove mmR1, mmR2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		model = new Model();
		
		pmR1 = new PlacementMove("NNNNNNNNNNNNNNNNNNNNNNNN", 'R', 0);
		pmB1 = new PlacementMove("RNNNNNNNNNNNNNNNNNNNNNNN", 'B', 7);
		
		pmR2 = new PlacementMove("RNNNNNNBNNNNNNNNNNNNNNNN", 'R', 1);
		pmB2 = new PlacementMove("RRNNNNNBNNNNNNNNNNNNNNNN", 'B', 8);
		
		pmR3 = new PlacementMove("RRNNNNNBBNNNNNNNNNNNNNNN", 'R', 3);
		pmB3 = new PlacementMove("RRNNNNNBBNNNNNNNNNNNNNNN", 'R', 9);
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecuteMove() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTurn() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetPhase() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetNextAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTrappedPlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetGamesToPlay() {
		fail("Not yet implemented");
	}

}
