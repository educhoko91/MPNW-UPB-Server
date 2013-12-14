package navalwar.server.gameengine;

import java.util.List;

import navalwar.server.gameengine.exceptions.ArmyTurnTimeoutException;
import navalwar.server.gameengine.exceptions.InvalidUnitNameException;
import navalwar.server.gameengine.exceptions.NotTurnOfArmyException;
import navalwar.server.gameengine.exceptions.PlaceNotFreeToPlaceUnitException;
import navalwar.server.gameengine.exceptions.UnitCoordinatesOutsideMatrixException;
import navalwar.server.gameengine.exceptions.WarAlreadyFinishedException;
import navalwar.server.gameengine.exceptions.WarAlreadyStartedException;
import navalwar.server.gameengine.exceptions.WarDoesNotExistException;
import navalwar.server.gameengine.exceptions.WarNotStartedException;
import navalwar.server.gameengine.info.IArmyInfo;
import navalwar.server.gameengine.info.IWarInfo;
import navalwar.server.network.IServerNetworkModule;

/**
 * This interface must be implemented by Game Engine module of the server.
 * It provides methods that enable the network module of the server to
 * dialog with the game engine in order to manage wars, armies and the game
 * itself.
 * 
 * @author Alfredo Villalba
 *
 */
public interface IGameEngineModule {

	/**
	 * Returned constant codes
	 */
	public static enum ShotCodes {
		SHOT_IN_EMPTY_CELL,
		SHOT_IN_UNIT_BUT_STILL_OPERATIONAL,
		SHOT_IN_UNIT_AND_DESTROYED,
		SHOT_IN_ALREADY_SHOTEN_UNIT,
		SHOT_IN_UNIT_AND_DESTROYED_AND_ARMY_DESTROYED,
		ARMY_DESTROYED_AND_WAR_FINISHED
	}


	/**
	 * This method is called after obtaining creating a game engine module.
	 * It binds the game engine with a network module so that the game engine
	 * can communicate with the network module (the net module must proceed
	 * in a similar way).
	 * 
	 * @param net the network module
	 */
	public void bindNetModule(IServerNetworkModule net);

	/**
	 * This method is called by the network module of the server.
	 * It creates a war and returns the unitque ID of the war.
	 * 
	 * @param name name of the war
	 * @param desc description of the war
	 * @return unique ID of the war
	 */
	public int createWar(String name, String desc);

	
	/**
	 * This method is called by the network module of the server.
	 * It returns a list containing the IDs of existing wars.
	 * 
	 * @return list of wars' IDs
	 */
	public List<Integer> getWarsList();  

	
	/**
	 * This method is called by the network module of the server.
	 * It returns an object containing information about a specific war.
	 * 
	 * @param warID the ID of the war
	 * @return object with war information
	 */
	public IWarInfo getWarInfo(int warID);
	
	
	/**
	 * This method is called by the network module of the server.
	 * It returns a list containing the IDs of registered armies in a war.
	 * 
	 * @param warID the ID of the war
	 * @return the list containing the IDs of the registered armies in the war
	 */
	public List<Integer> getArmies(int warID);

	
	/**
	 * This method is called by the network module of the server.
	 * It returns an object containing information about a specific army registered
	 * in a specific war.
	 *  
	 * @param warID the ID of the war
	 * @param armyID the ID of the army
	 * @return object with army information
	 */

	public IArmyInfo getArmyInfo(int warID, int armyID);

	
	/**
	 * This method is called by the network module of the server.
	 * It registers an army into a war.
	 *  
	 * @param warID the ID of the war
	 * @param name the name of the army
	 * @param units an array containing the type of units (i.e. class names of classes in navalwar.server.gameengine.units)
	 * @param rows an array containing the rows of units 
	 * @param cols an array containing the cols of units 
	 * @return the ID of the army
	 * @throws WarDoesNotExistException
	 * @throws WarAlreadyFinishedException
	 * @throws WarAlreadyStartedException
	 * @throws InvalidUnitNameException
	 * @throws InvalidArmyDescriptionException
	 * @throws PlaceNotFreeToPlaceUnitException 
	 * @throws UnitCoordinatesOutsideMatrixException 
	 */
	public int regArmy(int warID, String name, String[] units, int[] rows, int[] cols)
			throws WarDoesNotExistException, WarAlreadyFinishedException, WarAlreadyStartedException,
			InvalidUnitNameException, PlaceNotFreeToPlaceUnitException, UnitCoordinatesOutsideMatrixException;
	

	/**
	 * This method is called by the network module of the server.
	 * It is called to start a war.
	 * 
	 * @param warID the ID of the war
	 * @throws WarDoesNotExistException
	 * @throws WarAlreadyStartedException
	 * @throws WarAlreadyFinishedException
	 */
	public void startWar(int warID)
			throws WarDoesNotExistException, WarAlreadyStartedException, WarAlreadyFinishedException;
	
	
	/**
	 * This method is called by the network module of the server.
	 * It is called to get the ID of winner army of a war that has finished.
	 * 
	 * @param warID the ID of the war
	 * @return the ID of the winner army
	 * @throws WarDoesNotExistException
	 * @throws WarAlreadyFinishedException
	 * @throws WarNotStartedException
	 */
	public int getNextTurn(int warID)
			throws WarDoesNotExistException, WarAlreadyFinishedException, WarNotStartedException;
	
	
	/**
	 * This method is called by the network module of the server.
	 * It is called when an army wants to attack another army , both participating in
	 * the same war.
	 * 
	 * @param warID the ID of the war
	 * @param attackArmyID the ID of the army that wants to attack
	 * @param targetArmyID the ID of the army that is being attacked
	 * @param row the row of the target cell in the attacked army 
	 * @param col the columns of the target cell in the attacked army
	 * @return see ShotCodes
	 * @throws WarDoesNotExistException
	 * @throws ArmyTurnTimeoutException
	 * @throws NotTurnOfArmyException
	 * @throws WarAlreadyFinishedException 
	 * @throws WarNotStartedException 
	 */
	public ShotCodes handleShot(int warID, int attackArmyID, int targetArmyID, int row, int col)
			throws WarDoesNotExistException, ArmyTurnTimeoutException, NotTurnOfArmyException,
			WarNotStartedException, WarAlreadyFinishedException;


}
