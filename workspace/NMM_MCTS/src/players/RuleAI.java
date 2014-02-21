package players;

import java.util.ArrayList;
import java.util.Random;

import model.Phase;

import interfaces.IntPairInterface;
import interfaces.PlayerInterface;

public class RuleAI implements PlayerInterface {
	
	private char tokenColour;
	private char oppenentColour;
	private int tokensToPlace = 9;
	private int lastPlacement = -1;
	private Random ran = new Random();
	private String state;
	
	
	@Override
	public int placeToken(String state) {
		this.state = state;
		int placement = 0;
		int coinFlip = ran.nextInt(1);
		char[] stateArray = state.toCharArray();
		if(tokensToPlace == 9){
			if(stateArray[4] == 'N'){
				placement = 4;
			}else if(stateArray[10] == 'N'){
				placement = 10;
			}else if(stateArray[13] == 'N'){
				placement = 13;
			}else if(stateArray[19] == 'N'){
				placement = 19;
			}
		}else if(tokensToPlace == 8){
			if(lastPlacement == 4){
				if(stateArray[10] == oppenentColour && stateArray[13] == oppenentColour){
					if(coinFlip == 0){
						placement = 6;
					}else{
						placement = 8;
					}
				}else if(stateArray[10] == 'N' && stateArray[3] == 'N' && stateArray[18] == 'N' && stateArray[5] == 'N'){
					placement = 10;
				}else if(stateArray[13] == 'N' && stateArray[5] == 'N' && stateArray[3] == 'N' && stateArray[20] == 'N'){
					placement = 13;
				}
			}else if(lastPlacement == 10){
				if(stateArray[4] == oppenentColour && stateArray[19] == oppenentColour){
					if(coinFlip == 0){
						placement = 6;
					}else{
						placement = 15;
					}
				}else if(stateArray[4] == 'N' && stateArray[3] == 'N' && stateArray[18] == 'N' && stateArray[5] == 'N'){
					placement = 4;
				}else if(stateArray[19] == 'N' && stateArray[18] == 'N' && stateArray[21] == 'N' && stateArray[3] == 'N'){
					placement = 19;
				}
			}else if(lastPlacement == 13){
				if(stateArray[4] == oppenentColour && stateArray[19] == oppenentColour){
					if(coinFlip == 0){
						placement = 8;
					}else{
						placement = 17;
					}
				}else if(stateArray[4] == 'N' && stateArray[5] == 'N' && stateArray[3] == 'N' && stateArray[20] == 'N'){
					placement = 4;
				}else if(stateArray[19] == 'N' && stateArray[18] == 'N' && stateArray[20] == 'N' && stateArray[5] == 'N'){
					placement = 19;
				}
			}else if(lastPlacement == 19){
				if(stateArray[10] == oppenentColour && stateArray[13] == oppenentColour){
					if(coinFlip == 0){
						placement = 15;
					}else{
						placement = 17;
					}
				}else if(stateArray[10] == 'N' && stateArray[3] == 'N' && stateArray[18] == 'N' && stateArray[20] == 'N'){
					placement = 10;
				}else if(stateArray[13] == 'N' && stateArray[18] == 'N' && stateArray[5] == 'N' && stateArray[20] == 'N'){
					placement = 13;
				}
			}
		}else if(tokensToPlace == 7){
			ArrayList<int[]> enemyMills = checkForEnemyPotentialMills();
		}
		
		lastPlacement = placement;
		tokensToPlace--;
		return placement;
		
	}

	private ArrayList<int[]> checkForEnemyPotentialMills() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int[] getMillPotential(int position){
		
		int emptyNode = -1;
		int fullNode = -1;
		boolean potentialMill = false;
		
		switch(position){
		case 0:
			if((state.charAt(1) == 'N' && state.charAt(2) == oppenentColour)){
				potentialMill = true;
				emptyNode = 1;
				fullNode = 2;
			}else if((state.charAt(9) == 'N' && state.charAt(21) == oppenentColour)){
				potentialMill = true;
				emptyNode = 9;
				fullNode = 21;
			}else if((state.charAt(1) == oppenentColour && state.charAt(2) == 'N')){
				potentialMill = true;
				emptyNode = 2;
				fullNode = 1;
			}else if((state.charAt(9) == oppenentColour && state.charAt(21) == 'N')){
				potentialMill = true;
				emptyNode = 21;
				fullNode = 9;
			}
			break;
				
		case 1:
			if((state.charAt(0) == 'N' && state.charAt(2) == oppenentColour)){
				potentialMill = true;
				emptyNode = 0;
				fullNode = 2;
			}else if((state.charAt(9) == 'N' && state.charAt(21) == oppenentColour)){
				potentialMill = true;
				emptyNode = 9;
				fullNode = 21;
			}else if((state.charAt(1) == oppenentColour && state.charAt(2) == 'N')){
				potentialMill = true;
				emptyNode = 2;
				fullNode = 1;
			}else if((state.charAt(9) == oppenentColour && state.charAt(21) == 'N')){
				potentialMill = true;
				emptyNode = 21;
				fullNode = 9;
			}
			break;
		case 2:
			if((state.charAt(1) == toMatch && state.charAt(0) == toMatch)
					|| (state.charAt(14) == toMatch && state.charAt(23) == toMatch)){
						return true;
					}
						break;
		case 3:
			if((state.charAt(4) == toMatch && state.charAt(5) == toMatch)
					|| (state.charAt(10) == toMatch && state.charAt(18) == toMatch)){
						return true;
					}
						break;
		case 4:
			if((state.charAt(1) == toMatch && state.charAt(7) == toMatch)
					|| (state.charAt(3) == toMatch && state.charAt(5) == toMatch)){
						return true;
					}
						break;
		case 5:
			if((state.charAt(4) == toMatch && state.charAt(3) == toMatch)
					|| (state.charAt(13) == toMatch && state.charAt(20) == toMatch)){
						return true;
					}
						break;
		case 6:
			if((state.charAt(7) == toMatch && state.charAt(8) == toMatch)
					|| (state.charAt(11) == toMatch && state.charAt(15) == toMatch)){
						return true;
					}
						break;
		case 7:
			if((state.charAt(4) == toMatch && state.charAt(1) == toMatch)
					|| (state.charAt(6) == toMatch && state.charAt(8) == toMatch)){
						return true;
					}
						break;
		case 8:
			if((state.charAt(6) == toMatch && state.charAt(7) == toMatch)
					|| (state.charAt(12) == toMatch && state.charAt(17) == toMatch)){
						return true;
					}
						break;
		case 9:
			if((state.charAt(0) == toMatch && state.charAt(21) == toMatch)
					|| (state.charAt(10) == toMatch && state.charAt(11) == toMatch)){
						return true;
					}
						break;
		case 10:
			if((state.charAt(9) == toMatch && state.charAt(11) == toMatch)
					|| (state.charAt(3) == toMatch && state.charAt(18) == toMatch)){
						return true;
					}
						break;
		case 11:
			if((state.charAt(6) == toMatch && state.charAt(15) == toMatch)
					|| (state.charAt(9) == toMatch && state.charAt(10) == toMatch)){
						return true;
					}
						break;
		case 12:
			if((state.charAt(8) == toMatch && state.charAt(17) == toMatch)
					|| (state.charAt(13) == toMatch && state.charAt(14) == toMatch)){
						return true;
					}
						break;
		case 13:
			if((state.charAt(5) == toMatch && state.charAt(20) == toMatch)
					|| (state.charAt(12) == toMatch && state.charAt(14) == toMatch)){
						return true;
					}
						break;
		case 14:
			if((state.charAt(2) == toMatch && state.charAt(23) == toMatch)
					|| (state.charAt(12) == toMatch && state.charAt(13) == toMatch)){
						return true;
					}
						break;
		case 15:
			if((state.charAt(11) == toMatch && state.charAt(6) == toMatch)
					|| (state.charAt(16) == toMatch && state.charAt(17) == toMatch)){
						return true;
					}
						break;
		case 16:
			if((state.charAt(19) == toMatch && state.charAt(22) == toMatch)
					|| (state.charAt(15) == toMatch && state.charAt(17) == toMatch)){
						return true;
					}
						break;
		case 17:
			if((state.charAt(15) == toMatch && state.charAt(16) == toMatch)
					|| (state.charAt(8) == toMatch && state.charAt(12) == toMatch)){
						return true;
					}
						break;
		case 18:
			if((state.charAt(3) == toMatch && state.charAt(10) == toMatch)
					|| (state.charAt(19) == toMatch && state.charAt(20) == toMatch)){
						return true;
					}
						break;
		case 19:
			if((state.charAt(16) == toMatch && state.charAt(22) == toMatch)
					|| (state.charAt(18) == toMatch && state.charAt(20) == toMatch)){
						return true;
					}
						break;
		case 20:
			if((state.charAt(18) == toMatch && state.charAt(19) == toMatch)
					|| (state.charAt(5) == toMatch && state.charAt(13) == toMatch)){
						return true;
					}
						break;
		case 21:
			if((state.charAt(9) == toMatch && state.charAt(0) == toMatch)
					|| (state.charAt(22) == toMatch && state.charAt(23) == toMatch)){
						return true;
					}
						break;
		case 22:
			if((state.charAt(21) == toMatch && state.charAt(23) == toMatch)
					|| (state.charAt(19) == toMatch && state.charAt(16) == toMatch)){
						return true;
					}
						break;
		case 23:
			if((state.charAt(21) == toMatch && state.charAt(22) == toMatch)
					|| (state.charAt(2) == toMatch && state.charAt(14) == toMatch)){
						return true;
					}
						break;
	}
	return null;
}





	@Override
	public int removeToken(String state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IntPairInterface moveToken(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Rule Based AI";
	}

	@Override
	public void setTokenColour(char c) {
		tokenColour = c;
		if(c == 'R'){
			oppenentColour = 'B';
		}else{
			oppenentColour = 'R';
		}
	}

	@Override
	public char getTokenColour() {
		return tokenColour;
	}

}
