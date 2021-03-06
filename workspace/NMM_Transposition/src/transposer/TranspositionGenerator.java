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
	
	private TranposeWriter tw;
	private int sizeLastWrite;
	
	
	
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
		
		tw = new TranposeWriter();
		sizeLastWrite = 0;
		
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
			copyNextStatesOver();
			if((states.size()%100000) == 0){
				writeDate();
			}
		}
		
	}
	
	private void writeDate() {
//		int batchStart = states.size()/100000;
		
		int rangeStart = states.size() - sizeLastWrite;
		sizeLastWrite = states.size();
		
		Set<String> stateKeys = states.keySet();
		String[] keys = (String[]) stateKeys.toArray();
	
		ArrayList<String> stateStrings = new ArrayList<String>();
		
		while(rangeStart < states.size()){
			String sString = keys[rangeStart] + ": " + states.get(keys[rangeStart]);
			stateStrings.add(sString);
			rangeStart++;
		}
		
		tw.writeFile(stateStrings);
	}

	public void nextStates(String state, int action){
		ArrayList<String> nStates = new ArrayList<String>();
		
		if(action == 0 && depth < 18){
			nStates = allPossiblePlacements(state);
		}else if(action == 1){
			nStates = allPossibleRemovals(state);
		}else if(action == 0 && depth >= 18){
			nStates = allPossibleMoves(state);
		}
		
		String nxtStates = "";
		for (String string : nStates) {
			nxtStates += string + ", ";
			
		}
		
		states.put(state, nxtStates);
		if(turn == 'R'){
			turn = 'B';
		}else{
			turn = 'R';
		}
		
	}
	
	public ArrayList<String> allPossiblePlacements(String state){
		ArrayList<String> placements = new ArrayList<String>();
	
		mc.setState(state);
		
		int position = 0;
		while(position < 24){
			int result = mc.addToken(turn, position);
			if(result > -1){
				
			}
		}
		
		
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
			Set<String> concreteStateKeys = states.keySet();
			for (String concreteState : concreteStateKeys) {
				if(state.equals(concreteState)){
					found = true;
				}
			}
			if(!found){
				int action = nextStates.get(state);
				workingStateSet.put(state, action);
			}
		}
		nextStates.clear();
	}
	
	public void checkTerminals(){
		
	}
	
}
