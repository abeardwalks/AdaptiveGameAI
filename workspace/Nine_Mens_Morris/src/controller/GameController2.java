package controller;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import board.MorrisBoard2;

import view.ApplicationView;
import view.PlayingView;
import view.SetupView;
import interfaces.GameStateInterface;
import interfaces.PlayerInterface;

public class GameController2 {
	
	private GameStateInterface gs;
	private ApplicationView primaryView;
	private Thread thread;
	private PlayingView gameView;
	private SetupView setupView;
	private PlayerInterface p1;
	private PlayerInterface p2;
	
	public GameController2(){
		
		primaryView = new ApplicationView();
		List<PlayerInterface> players1 = getPlayers();
		List<PlayerInterface> players2 = getPlayers();
		gs = new MorrisBoard2();
		
		
		
	}
	
	private List<PlayerInterface> getPlayers() {
		List<PlayerInterface> players = new ArrayList<PlayerInterface>();
		File dir = new File("bin" + System.getProperty("file.separator") + "players" );
		for(String file : dir.list()){
			PlayerInterface player = null;
			if(!file.contains("$")){
				try {
					player = loadPlyaer("players." + file.substring(0, file.length() - 6));
				} catch (Throwable e) {}
			}
			
			if (player != null){
				players.add(player);
			}
		}
		return players;
	}

	private PlayerInterface loadPlyaer(String playerClass) {
		PlayerInterface player = null;
		try {
			Class<?> theClass = Class.forName(playerClass);
			Constructor<?>[] cons = theClass.getConstructors();
			player = (PlayerInterface) cons[0].newInstance();
		} catch (Throwable e) {}
		return player;
	}

}
