package tests.model;

import static org.junit.Assert.*;
import interfaces.BoardFacadeInterface;

import model.board.Model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelTest {
	
	BoardFacadeInterface model;
	private String startStateString;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		model = new Model();
		startStateString = "NNNNNNNNNNNNNNNNNNNNNNNN";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testModelStringIntIntIntIntPhaseIntBooleanChar() {
		fail("Not yet implemented");
	}

	@Test
	public void testExecuteMove() {
		fail("Not yet implemented");
	}

	@Test
	public void testValidMove() {
		fail("Not yet implemented");
	}

	@Test
	public void testMillMade() {
		fail("Not yet implemented");
	}

	@Test
	public void testUndo() {
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
	public void testReset() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTrappedPlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetState() {
		assertTrue(model.getState().equals(startStateString));
	}

	@Test
	public void testGetPlayerOneToPlace() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlayerTwoToPlace() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlayerOneRemaining() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlayerTwoRemaining() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPhase() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTurn() {
		fail("Not yet implemented");
	}

	@Test
	public void testGameWon() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNextAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRewards() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintDetails() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllPossibleMoves() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetPlayers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlayerOne() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlayerTwo() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetGamesPlayed() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetGamesToPlay() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlayerOneWins() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlayerTwoWins() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetGamesToPlay() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNumberOfDraws() {
		fail("Not yet implemented");
	}

	@Test
	public void testPlayerOneWin() {
		fail("Not yet implemented");
	}

	@Test
	public void testPlayerTwoWin() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetHistory() {
		fail("Not yet implemented");
	}

	@Test
	public void testGameOver() {
		fail("Not yet implemented");
	}

}
