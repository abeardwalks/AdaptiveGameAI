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

public class ModelGetterTest {

	private BoardFacadeInterface blankModel;
	private BoardFacadeInterface moveReadyModel;
	private BoardFacadeInterface moveAnywhereModel;
	private BoardFacadeInterface gameOver;
	
	@SuppressWarnings("unused")
	private AbstractMove pmR1, pmR2, pmR3, pmB1, pmB2, pmB3;	//placement
	@SuppressWarnings("unused")
	private AbstractMove rmR1, rmR2, rmR3;						//removal
	@SuppressWarnings("unused")
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
		
		gameOver = new Model("RNRRRRNRNNNNNNNBBNNNNNNN", 0, 0, 6, 2, Phase.TWO, 2, false, 'M');
	}
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidMove() {
		blankModel.executeMove(rmR1);
		assertFalse(blankModel.validMove());
		
		blankModel.executeMove(pmR1);
		assertTrue(blankModel.validMove());
		
		blankModel.executeMove(pmR1);
	}

	@Test
	public void testMillMade() {
		moveReadyModel.executeMove(mmR2);
		assertTrue(moveReadyModel.millMade());
		
		blankModel.executeMove(pmR1);
		assertFalse(blankModel.millMade());
	}

	@Test
	public void testGetState() {
		assertTrue(blankModel.getState().equals("NNNNNNNNNNNNNNNNNNNNNNNN"));
		blankModel.executeMove(pmR1);
		assertTrue(blankModel.getState().equals("RNNNNNNNNNNNNNNNNNNNNNNN"));
		assertFalse(blankModel.getState().equals("NNNNNNNNNNNNNNNNNNNNNNNN"));
	}

	@Test
	public void testGetPlayerOneToPlace() {
		assertTrue(blankModel.getPlayerOneToPlace() == 9);
		blankModel.executeMove(pmR1);
		assertTrue(blankModel.getPlayerOneToPlace() == 8);
	}

	@Test
	public void testGetPlayerTwoToPlace() {
		assertTrue(blankModel.getPlayerTwoToPlace() == 9);
		blankModel.executeMove(pmR1);
		assertTrue(blankModel.getPlayerOneToPlace() == 8);
		blankModel.executeMove(pmB1);
		assertTrue(blankModel.validMove());
		assertTrue(blankModel.getPlayerTwoToPlace() == 8);
	}

	@Test
	public void testGetPlayerOneRemaining() {
		assertTrue(moveReadyModel.getPlayerOneRemaining() == 6);
		assertTrue(blankModel.getPlayerOneRemaining() == 9);
	}

	@Test
	public void testGetPlayerTwoRemaining() {
		assertTrue(moveReadyModel.getPlayerTwoRemaining() == 5);
		assertTrue(blankModel.getPlayerTwoRemaining() == 9);
	}

	@Test
	public void testGetPhase() {
		assertTrue(blankModel.getPhase() == Phase.ONE);
		assertTrue(moveReadyModel.getPhase() == Phase.TWO);
		assertFalse(moveAnywhereModel.getPhase() == Phase.FOUR);
	}

	@Test
	public void testGetTurn() {
		assertTrue(blankModel.getTurn() == 1);
		blankModel.executeMove(pmR1);
		assertFalse(blankModel.getTurn() == 1);
		assertTrue(blankModel.getTurn() == 2);
		
		moveReadyModel.executeMove(mmR2);
		assertTrue(moveReadyModel.getTurn() == 1);
	}

	@Test
	public void testGameWon() {
		assertFalse(moveReadyModel.gameWon());
		assertTrue(gameOver.gameWon());
	}

}
