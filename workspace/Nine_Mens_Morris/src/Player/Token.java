package Player;
import java.awt.Color;

/**
 * A Token represents a playing piece that can placed on the board.
 * They have a colour and a position, represented by x and y cordinates. 
 * 
 * @author Andy
 *
 */
public class Token {
	
	private int x, y;
	
	private final Color c;
	
	public Token (Color c){
		x = 0;
		y = 0;
		
		this.c = c;
	}
	
	public Token(int x, int y, Color c){
		this.x = x;
		this.y = x;
		this.c = c;
	}
	
	/**
	 * Sets the position of this token.
	 * 
	 * @param x - x position.
	 * @param y - y position.
	 */
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the x position.
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * @return the y position.
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * @return the colour of the token.
	 */
	public Color getColour(){
		return c;
	}

}
