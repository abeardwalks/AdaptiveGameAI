package transposer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import controller.MoveChecker;

public class TranspositionGenerator {

	private MoveChecker mc;
	private char turn;
	
	private HashMap<String, String> states;
	private HashMap<String, Integer> nextStates;
	private HashMap<String, Integer> workingStateSet;
	
	private int depth;
	private boolean checkForTerminals;
	private boolean onlyTerminalsLeft;
	
	
	public TranspositionGenerator(){
		mc = new MoveChecker();
		turn = 'R';
		
		states = new HashMap<String, String>();
		states.put("NNNNNNNNNNNNNNNNNNNNNNNN", "");
		
		nextStates = new HashMap<String, Integer>();
		
		workingStateSet = new HashMap<String, Integer>();
		workingStateSet.put("NNNNNNNNNNNNNNNNNNNNNNNN", -1);
		
		depth = 0;
		checkForTerminals = false;
		onlyTerminalsLeft = false;
		
	}
	
	public void generateTable(){
		
		
		while(!onlyTerminalsLeft){
			Set<String> keys = workingStateSet.keySet();
			for (String state : keys) {
				Integer action = workingStateSet.get(state);
				nextStates(state, action);
			}
			depth++;
			if(depth == 18 && !checkForTerminals){
				checkForTerminals = true;
			}
		}
		
	}
	
	public void nextStates(String state, int action){
		ArrayList<String> nStates = new ArrayList<String>();
		
		if(action == 0 && depth < 18){
			nStates = allPossiblePlacements(state);
		}else if(action == 1){
			nStates = allPossibleRemovals(state);
		}
		
	}
	
	public ArrayList<String> allPossiblePlacements(String state){
		ArrayList<String> placements = new ArrayList<String>();
		
		
		
		return placements;
		
	}
	
	public ArrayList<String> allPossibleRemovals(String state){
		ArrayList<String> removals = new ArrayList<String>();
		
		
		
		return removals;
		
	}
	
	public ArrayList<String> allPossibleMoves(String state){
		ArrayList<String> moves = new ArrayList<String>();
		
		
		
		return moves;
		
	}
	
	public void copyNextStatesOver(){
		workingStateSet.clear();
		Set<String> nextStateKeys = nextStates.keySet();
		for (String state : nextStateKeys) {
			boolean found = false;
			for (String concreteState : states) {
				if(state.equals(concreteState)){
					found = true;
				}
			}
			if(!found){
				states.add(state);
				int action = nextStates.get(state);
				workingStateSet.put(state, action);
			}
		}
		nextStates.clear();
	}
	
	public void checkTerminals(){
		
	}
	
}
