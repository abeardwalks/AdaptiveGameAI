package tests.model;

import static org.junit.Assert.*;
import interfaces.BoardFacadeInterface;

import model.Phase;
import model.board.Model;
import move.AbstractMove;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelMutatorTest {
	
	private BoardFacadeInterface blankModel;
	private BoardFacadeInterface moveReadyModel;
	private BoardFacadeInterface moveAnywhereModel;
	
	private AbstractMove pmR1, pmR2, pmR3, pmB1, pmB2, pmB3;	//placement
	private AbstractMove rmR1, rmR2, rmR3;						//removal
	private AbstractMove mmR1, mmR2, mmB1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		blankModel = new Model();
		
		pmR1 = new PlacementMove("NNNNNNNNNNNNNNNNNNNNNNNN", 'R', 0);
		pmB1 = new PlacementMove("RNNNNNNNNNNNNNNNNNNNNNNN", 'B', 7);
		
		pmR2 = new PlacementMove("RNNNNNNBNNNNNNNNNNNNNNNN", 'R', 1);
		pmB2 = new PlacementMove("RRNNNNNBNNNNNNNNNNNNNNNN", 'B', 8);
		
		pmR3 = new PlacementMove("RRNNNNNBBNNNNNNNNNNNNNNN", 'R', 2);
		pmB3 = new PlacementMove("RRRNNNNBBNNNNNNNNNNNNNNN", 'B', 9);
		
		rmR1 = new RemovalMove("RRRNNNNBBNNNNNNNNNNNNNNN", 'R', 7);
		
		moveReadyModel = new Model("RNRRRRNRNNNNNNNBBBBNBNNN", 0, 0, 6, 5, Phase.TWO, 1, false, 'M');
		
		mmR1 = new MovementMove("RNRRRRNRNNNNNNNBBBBNBNNN", 'R', 0, 23);
		mmR2 = new MovementMove("RNRRRRNRNNNNNNNBBBBNBNNN", 'R', 4, 1);
		
		rmR2 = new RemovalMove("RRRRNRNRNNNNNNNBBBBNBNNN", 'R' , 16);
		rmR3 = new RemovalMove("RRRRNRNRNNNNNNNBBBBNBNNN", 'R' , 18);
		
		moveAnywhereModel = new Model("RNRRRRNRNNNNNNNBBBNNNNNN", 0, 0, 6, 3, Phase.TWO, 2, false, 'M');
	
		mmB1 = new MovementMove("RNRRRRNRNNNNNNNBBBNNNNNN", 'B', 15, 1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecuteMove() {
		
		//----------Test Placement/Removals-----------
		blankModel.executeMove(pmR1);
		assertTrue(blankModel.validMove());
		assertTrue(blankModel.getState().equals("RNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(blankModel.getState().equals("NNNNNNNNNNNNNNNNNNNNNNNN"));
	
		blankModel.executeMove(pmR2);
		assertFalse(blankModel.validMove());
		assertTrue(blankModel.getState().equals("RNNNNNNNNNNNNNNNNNNNNNNN"));
		
		blankModel.executeMove(pmB1);
		assertTrue(blankModel.validMove());
		assertTrue(blankModel.getState().equals("RNNNNNNBNNNNNNNNNNNNNNNN"));
		
		blankModel.executeMove(pmR2);
		blankModel.executeMove(pmB2);
		assertTrue(blankModel.getState().equals("RRNNNNNBBNNNNNNNNNNNNNNN"));
		
		blankModel.executeMove(pmR3);
		assertTrue(blankModel.millMade());
		
		blankModel.executeMove(pmB3);
		assertFalse(blankModel.validMove());
		
		blankModel.executeMove(rmR1);
		assertTrue(blankModel.validMove());
		assertFalse(blankModel.millMade());
		assertTrue(blankModel.getState().equals("RRRNNNNNBNNNNNNNNNNNNNNN"));
		
		//----------Test Movements/Removals-------------
		moveReadyModel.executeMove(mmR1);
		assertFalse(moveReadyModel.validMove());
		
		moveReadyModel.executeMove(mmR2);
		assertTrue(moveReadyModel.validMove());
		assertTrue(moveReadyModel.millMade());
		assertTrue(moveReadyModel.getState().equals("RRRRNRNRNNNNNNNBBBBNBNNN"));
		
		moveReadyModel.executeMove(rmR2);			//Test that removal from Mill invalid when others available.
		assertFalse(moveReadyModel.validMove());
		
		moveReadyModel.executeMove(rmR3);
		assertTrue(moveReadyModel.validMove());
		assertTrue(moveReadyModel.getState().equals("RRRRNRNRNNNNNNNBBBNNBNNN"));
		
		//----------Test Move Anywhere----------------
		moveAnywhereModel.executeMove(mmB1);
		assertTrue(moveAnywhereModel.validMove());
		assertTrue(moveAnywhereModel.getState().equals("RBRRRRNRNNNNNNNNBBNNNNNN"));
		
	}

	@Test
	public void testSetTurn() {
		assertTrue(blankModel.getTurn() == 1);
		blankModel.setTurn();
		assertTrue(blankModel.getTurn() == 2);
		assertFalse(blankModel.getTurn() == 1);
	}

	@Test
	public void testSetPhase() {
		assertTrue(blankModel.getPhase() == Phase.ONE);
		blankModel.setPhase(Phase.TWO);
		assertFalse(blankModel.getPhase() == Phase.ONE);
		assertTrue(blankModel.getPhase() == Phase.TWO);
	}

	@Test
	public void testSetNextAction() {
		assertTrue(blankModel.getNextAction() == 'P');
		blankModel.setNextAction('M');
		assertTrue(blankModel.getNextAction() == 'M');
		assertFalse(blankModel.getNextAction() == 'P');
	}

}
