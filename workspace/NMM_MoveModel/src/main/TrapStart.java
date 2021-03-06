package main;

import utility.MoveChecker;
import model.Phase;
import model.board.BoardModel;
import interfaces.BoardFacadeInterface;
import controller.MoveModelController;

public class TrapStart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String state = "NNNNNNRRBNRBRRRBRBBRBRBB";
		int playerOneToPlace = 0;
		int playerTwoToPlace = 0;
		int playerOneRemianing = 9;
		int playerTwoRemaining = 8;
		Phase phase = Phase.TWO;
		char turn = 1;
		char nextAction = 'P';
		
		BoardFacadeInterface model = new BoardModel(state, playerOneToPlace, playerTwoToPlace, playerOneRemianing, playerTwoRemaining, phase, turn, nextAction);
	
		MoveChecker mc = new MoveChecker(model);
		
		mc.printDetails();
		System.out.println(mc.playersCanMove());
		
	}

}
