package navalwar.server.gameengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import navalwar.server.gameengine.War.Status;
import navalwar.server.gameengine.exceptions.ArmyTurnTimeoutException;
import navalwar.server.gameengine.exceptions.InvalidUnitNameException;
import navalwar.server.gameengine.exceptions.NotTurnOfArmyException;
import navalwar.server.gameengine.exceptions.PlaceNotFreeToPlaceUnitException;
import navalwar.server.gameengine.exceptions.UnitCoordinatesOutsideMatrixException;
import navalwar.server.gameengine.exceptions.WarAlreadyFinishedException;
import navalwar.server.gameengine.exceptions.WarAlreadyStartedException;
import navalwar.server.gameengine.exceptions.WarDoesNotExistException;
import navalwar.server.gameengine.exceptions.WarNotStartedException;
import navalwar.server.gameengine.info.ArmyInfo;
import navalwar.server.gameengine.info.IArmyInfo;
import navalwar.server.gameengine.info.IWarInfo;
import navalwar.server.gameengine.info.WarInfo;
import navalwar.server.gameengine.units.Unit;
import navalwar.server.network.IServerNetworkModule;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

public class GameEngineModule implements IGameEngineModule {
	
	
	private static final int MAX_WAR_ID = 1000;
	
	IServerNetworkModule net = null;

	private Map<Integer, War> wars;

	private static Random rnd = new Random(System.currentTimeMillis());
	
	
	
	//--------------------------------------------
	// Constructors & singleton pattern
	//--------------------------------------------

	private GameEngineModule() {
		wars = new HashMap<Integer, War>();
	}
	
	private static GameEngineModule instance = null;
	public static GameEngineModule getInstance() {
		if (instance == null) instance = new GameEngineModule();
		return instance;
	}
	
	//--------------------------------------------
	// IGameEngineModule methods
	//--------------------------------------------
	
	
	public synchronized int createWar(String name, String desc) {
		War war = new War(name, desc);
	
		// assign unique ID to war
		int warID = rnd.nextInt(MAX_WAR_ID);
		while (wars.containsKey(warID)) warID = rnd.nextInt();
		war.setWarID(warID);
		
		wars.put(warID, war);
		
		return warID;
	}

	public synchronized List<Integer> getWarsList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		Set<Integer> keys = wars.keySet();
		for(int warID : keys) {
			list.add(warID);
		}
		return list;
	}

	public synchronized int regArmy(int warID, String name, String[] units, int[] rows, int[] cols) throws WarDoesNotExistException, WarAlreadyFinishedException, WarAlreadyStartedException, InvalidUnitNameException, PlaceNotFreeToPlaceUnitException, UnitCoordinatesOutsideMatrixException {
		// check if war exists
		if (!wars.containsKey(warID)) throw new WarDoesNotExistException(warID);

		War war = wars.get(warID);

		// register army
		int armyID = war.regArmy(name, units, rows, cols);
		return armyID;
	}

	
	public synchronized ShotCodes handleShot(int warID, int attackArmyID, int targetArmyID,
			int row, int col) throws WarDoesNotExistException, ArmyTurnTimeoutException, NotTurnOfArmyException, WarNotStartedException, WarAlreadyFinishedException {
		// check if war exists
		if (!wars.containsKey(warID)) throw new WarDoesNotExistException(warID);
		
		War war = wars.get(warID);
		ShotCodes code = war.handleShot(attackArmyID, targetArmyID, row, col);
		return code;
	}

	public synchronized IWarInfo getWarInfo(int warID) {
		// check if war exists
		if (!wars.containsKey(warID))
			return null;
		
		War war = wars.get(warID);
		WarInfo wi = new WarInfo(war);
		return wi;
	}
	

	public synchronized IArmyInfo getArmyInfo(int warID, int armyID) {
		// check if war exists
		if (!wars.containsKey(warID))
			return null;
		War war = wars.get(warID);

		// check if army exists
		Army army = war.getArmy(armyID);
		if (army == null) return null;

		// construct and return army info
		ArmyInfo ai = new ArmyInfo(army);
		
		return null;
	}

	public synchronized void startWar(int warID) throws WarDoesNotExistException, WarAlreadyStartedException, WarAlreadyFinishedException {
		// check if war exists
		if (!wars.containsKey(warID)) throw new WarDoesNotExistException(warID);

		// start war
		War war = wars.get(warID);
		war.startWar();
	}

	public synchronized List<Integer> getArmies(int warID) {
		// check if war exists
		if (!wars.containsKey(warID))
			return null;
		War war = wars.get(warID);

		// construct and return list of armies IDs
		ArrayList<Integer> armiesIDs = new ArrayList<Integer>();
		for(Army army : war.getArmies()) armiesIDs.add(army.getArmyID());
		return armiesIDs;
	}


	public int getNextTurn(int warID) throws WarDoesNotExistException, WarAlreadyFinishedException, WarNotStartedException {
		// check if war exists
		if (!wars.containsKey(warID)) throw new WarDoesNotExistException(warID);
		War war = wars.get(warID);
		return war.getNextTurn();
	}


	public void bindNetModule(IServerNetworkModule net) {
		this.net = net;
	}

}
