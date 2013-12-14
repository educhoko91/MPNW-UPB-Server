package navalwar.server.gameengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import navalwar.server.gameengine.IGameEngineModule.ShotCodes;
import navalwar.server.gameengine.exceptions.ArmyTurnTimeoutException;
import navalwar.server.gameengine.exceptions.InvalidUnitNameException;
import navalwar.server.gameengine.exceptions.NotTurnOfArmyException;
import navalwar.server.gameengine.exceptions.PlaceNotFreeToPlaceUnitException;
import navalwar.server.gameengine.exceptions.UnitCoordinatesOutsideMatrixException;
import navalwar.server.gameengine.exceptions.WarAlreadyFinishedException;
import navalwar.server.gameengine.exceptions.WarAlreadyStartedException;
import navalwar.server.gameengine.exceptions.WarNotStartedException;
import navalwar.server.gameengine.units.Plane;
import navalwar.server.gameengine.units.Ship;
import navalwar.server.gameengine.units.Soldier;
import navalwar.server.gameengine.units.Tank;
import navalwar.server.gameengine.units.Unit;
import navalwar.server.network.IServerNetworkModule;


public class War {
	
	public static final long DELAY_TURN = 10000;  // 10s in milliseconds
	private static final int MAX_ARMY_ID = 1000;

	private IServerNetworkModule netModule;

	private int warID;
	private String name;
	private String desc;
	private Map<Integer, Army> armies;
	private int numArmies;
	private int turn;
	private Set<Integer> armiesInFight;
	private List<Integer> armiesWaitTurn;
	private Integer winnerID;
	
	public enum Status { REGISTERING, FIGHTING, FINISHED };
	private Status status;
	
	private Timer timer = new Timer();
	private TimerTask timerTask;
	private boolean timeoutTurn;

	private static Random rnd = new Random(System.currentTimeMillis());

	
	private class TurnTimeoutTimerTask extends TimerTask {
		@Override
		public void run() {
			notifyTimeoutTurn();
		}
	}
	
	public War(String name, String desc) {
		this.name = name;
		this.desc = desc;
		numArmies = 0;
		armies = new HashMap<Integer, Army>();
		armiesInFight = new HashSet<Integer>();
		armiesWaitTurn = new ArrayList<Integer>();
		status = Status.REGISTERING;
	}
	
	public int getWarID() { return warID; }
	public String getName() { return name; }
	public String getDesc() {return desc; }
	public int getNumArmies() { return numArmies; }
	public int getNumFightingArmies() { return armiesInFight.size(); }
	public Collection<Army> getArmies() { return armies.values(); }
	public Status getStatus() { return status; }
	
	public void setWarID(int warID) {
		this.warID = warID;
	}

	public int regArmy(String name, String[] units, int[] rows, int[] cols) throws WarAlreadyFinishedException, WarAlreadyStartedException, InvalidUnitNameException, PlaceNotFreeToPlaceUnitException, UnitCoordinatesOutsideMatrixException {
		if (status == Status.FIGHTING) throw new WarAlreadyStartedException(warID);
		if (status == Status.FINISHED) throw new WarAlreadyFinishedException(warID);
		
		Army field = new Army(name);
		int numUnits = units.length;
		
		for (int i = 0; i < numUnits; i++) {

			String unitName = units[i];
			int unitRow = rows[i];
			int unitCol = cols[i];
			
			Unit u = null;
			if (unitName.compareToIgnoreCase("Soldier") == 0) {
				u = new Soldier();
			}
			else if (unitName.compareToIgnoreCase("Tank") == 0) {
				u = new Tank();
			}
			else if (unitName.compareToIgnoreCase("Ship") == 0) {
				u = new Ship();
			}
			else if (unitName.compareToIgnoreCase("Plane") == 0) {
				u = new Plane();
			}
			else {
				throw new InvalidUnitNameException(name);
			}
			//Class classUnit = Class.forName("navalwar.server.units." + units[i]);
			//Unit u = (Unit) classUnit.getConstructor().newInstance();

			
			field.addUnit(u, unitRow, unitCol);
			
		}

		
		// assign unique ID to the army in the war
		int armyID = rnd.nextInt(MAX_ARMY_ID);
		while (armies.containsKey(armyID)) armyID = rnd.nextInt(MAX_ARMY_ID);
		field.setArmyID(armyID);

		// keep army in map
		armies.put(armyID, field);
		numArmies++;

		// update armies in fight and wait turn
		armiesInFight.add(armyID);
		armiesWaitTurn.add(armyID);
		
		return armyID;
	}
	
	public void startWar() throws WarAlreadyStartedException, WarAlreadyFinishedException {
		if (status == Status.FIGHTING) throw new WarAlreadyStartedException(warID);
		if (status == Status.FINISHED) throw new WarAlreadyFinishedException(warID);
		chooseNextTurn();
		status = Status.FIGHTING;
	}
	
	private void chooseNextTurn() {
		int k = rnd.nextInt(armiesWaitTurn.size());
		int warID = armiesWaitTurn.get(k);
		armiesWaitTurn.remove(k);
		if (armiesWaitTurn.size() == 0) {
			armiesWaitTurn.addAll(armiesInFight);
		}
		turn = warID;
		timerTask = new TurnTimeoutTimerTask();
		timer.schedule(timerTask, DELAY_TURN);
		timeoutTurn = false;
	}
	
	public int getNextTurn() throws WarAlreadyFinishedException, WarNotStartedException {
		if (status == Status.REGISTERING) throw new WarNotStartedException(warID);
		if (status == Status.FINISHED) throw new WarAlreadyFinishedException(warID);
		return turn;
	}
	
	public void notifyTurn() {
		netModule.turnArmy(warID, turn);
	}
	
	public void notifyTimeoutTurn() {
		timeoutTurn = true;
		netModule.turnArmyTimeout(warID, turn);
		chooseNextTurn();
	}
	
	public ShotCodes handleShot(int enemyID, int targetID, int row, int col) throws ArmyTurnTimeoutException, NotTurnOfArmyException, WarNotStartedException, WarAlreadyFinishedException {
		if (status == Status.REGISTERING) throw new WarNotStartedException(warID);
		if (status == Status.FINISHED) throw new WarAlreadyFinishedException(warID);
		if (enemyID != turn) throw new NotTurnOfArmyException(enemyID);
		if (timeoutTurn) throw new ArmyTurnTimeoutException(enemyID);

		// cancel timeout of turn
		timerTask.cancel();
		
		// shot
		ShotCodes res = armies.get(targetID).handleShot(enemyID, row, col);
		
		// if army destroyed, remove it from war
		if (res == ShotCodes.SHOT_IN_UNIT_AND_DESTROYED_AND_ARMY_DESTROYED) {
			armiesInFight.remove(targetID);
			// check if war finished
			if (armiesInFight.size() == 1) {
				// winner is last army
				winnerID = (Integer)(armiesInFight.toArray()[0]);
				status = Status.FINISHED;
				return ShotCodes.ARMY_DESTROYED_AND_WAR_FINISHED;
			}
		}

		// choose next army turn
		chooseNextTurn();
		return res;
	}

	public Army getArmy(int armyID) {
		return armies.get(armyID);
	}

}
