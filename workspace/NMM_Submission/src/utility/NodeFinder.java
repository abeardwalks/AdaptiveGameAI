package utility;

import players.IntPair;

/**
 * NodeFinder is a utility class that translates coordinates into state string indexes and vice versa.
 * It is used mainly by the different mouse action listeners. 
 * 
 * @author Andrew White - BSc Software Engineering, 200939787.
 *
 */
public class NodeFinder {

	private int x0,x1,x2,x3,x4,x5,x6;
	private int y0,y1,y2,y3,y4,y5,y6;
	
	public NodeFinder(){
		initializeCordinates();
	}
	
	private void initializeCordinates() {
		x0 = 102;
		x1 = 375;
		x2 = 647;
		x3 = 193;
		x4 = 557;
		x5 = 285;
		x6 = 465;
		
		y0 = 7;
		y1 = 97;
		y2 = 188;
		y3 = 281;
		y4 = 371;
		y5 = 462;
		y6 = 552;
	}
	
	/**
	 * Translate a coordinate into a node index.
	 * 
	 * @param x	- the x coordinate.
	 * @param y - the y coordinate.
	 * @return  - the node index.
	 */
	public int getNode(int x, int y){
		if((x >= x0 && x <= (x0 + 40)) && (y >= y0 && y <= (y0 + 40))){
			return 0;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y0 && y <= y0 + 40)){
			return 1;
		} else if((x >= x2 && x <= x2 + 40) && (y >= y0 && y <= y0 + 40)){
			return 2;
		} else if((x >= x3 && x <= x3 + 40) && (y >= y1 && y <= y1 + 40)){
			return 3;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y1 && y <= y1 + 40)){
			return 4;
		} else if((x >= x4 && x <= x4 + 40) && (y >= y1 && y <= y1 + 40)){
			return 5;
		} else if((x >= x5 && x <= x5 + 40) && (y >= y2 && y <= y2 + 40)){
			return 6;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y2 && y <= y2 + 40)){
			return 7;
		} else if((x >= x6 && x <= x6 + 40) && (y >= y2 && y <= y2 + 40)){
			return 8;
		} else if((x >= x0 && x <= x0 + 40) && (y >= y3 && y <= y3 + 40)){
			return 9;
		} else if((x >= x3 && x <= x3 + 40) && (y >= y3 && y <= y3 + 40)){
			return 10;
		} else if((x >= x5 && x <= x5 + 40) && (y >= y3 && y <= y3 + 40)){
			return 11;
		} else if((x >= x6 && x <= x6 + 40) && (y >= y3 && y <= y3 + 40)){
			return 12;
		} else if((x >= x4 && x <= x4 + 40) && (y >= y3 && y <= y3 + 40)){
			return 13;
		} else if((x >= x2 && x <= x2 + 40) && (y >= y3 && y <= y3 + 40)){
			return 14;
		} else if((x >= x5 && x <= x5 + 40) && (y >= y4 && y <= y4 + 40)){
			return 15;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y4 && y <= y4 + 40)){
			return 16;
		} else if((x >= x6 && x <= x6 + 40) && (y >= y4 && y <= y4 + 40)){
			return 17;
		} else if((x >= x3 && x <= x3 + 40) && (y >= y5 && y <= y5 + 40)){
			return 18;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y5 && y <= y5 + 40)){
			return 19;
		} else if((x >= x4 && x <= x4 + 40) && (y >= y5 && y <= y5 + 40)){
			return 20;
		} else if((x >= x0 && x <= x0 + 40) && (y >= y6 && y <= y6 + 40)){
			return 21;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y6 && y <= y6 + 40)){
			return 22;
		} else if((x >= x2 && x <= x2 + 40) && (y >= y6 && y <= y6 + 40)){
			return 23;
		}
		return -1;
	}
	
	/**
	 * Takes a node value (an int) and translates it into a coordinate.
	 * 
	 * @param node - the node (index) of the state string.
	 * @return	   - the coordinate. 
	 */
	public IntPair getCordinates(int node){
		IntPair cordinates = null;
		switch (node) {
		case 0:
			cordinates = new IntPair(x0, y0);
			break;
		case 1:
			cordinates = new IntPair(x1, y0);
			break;
		case 2:
			cordinates = new IntPair(x2, y0);
			break;
		case 3:
			cordinates = new IntPair(x3, y1);
			break;
		case 4:
			cordinates = new IntPair(x1, y1);
			break;
		case 5:
			cordinates = new IntPair(x4, y1);
			break;
		case 6:
			cordinates = new IntPair(x5,  y2);
			break;
		case 7:
			cordinates = new IntPair(x1, y2);
			break;
		case 8:
			cordinates = new IntPair(x6, y2);
			break;
		case 9:
			cordinates = new IntPair(x0, y3);
			break;
		case 10:
			cordinates = new IntPair(x3, y3);
			break;
		case 11:
			cordinates = new IntPair(x5, y3);
			break;
		case 12:
			cordinates = new IntPair(x6, y3);
			break;
		case 13:
			cordinates = new IntPair(x4, y3);
			break;
		case 14:
			cordinates = new IntPair(x2, y3);
			break;
		case 15:
			cordinates = new IntPair(x5, y4);
			break;
		case 16:
			cordinates = new IntPair(x1, y4);
			break;
		case 17:
			cordinates = new IntPair(x6, y4);
			break;
		case 18:
			cordinates = new IntPair(x3, y5);
			break;
		case 19:
			cordinates = new IntPair(x1, y5);
			break;
		case 20:
			cordinates = new IntPair(x4, y5);
			break;
		case 21:
			cordinates = new IntPair(x0, y6);
			break;
		case 22:
			cordinates = new IntPair(x1, y6);
			break;
		case 23:
			cordinates = new IntPair(x2, y6);
			break;
		default:
			break;
		}
		return cordinates;
	}

}
