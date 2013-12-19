package navalwar.server.gameegine;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import navalwar.server.gameengine.GameEngineModule;
import navalwar.server.gameengine.UnitAndPlace;
import navalwar.server.gameengine.War;
import navalwar.server.gameengine.exceptions.InvalidUnitNameException;
import navalwar.server.gameengine.exceptions.PlaceNotFreeToPlaceUnitException;
import navalwar.server.gameengine.exceptions.UnitCoordinatesOutsideMatrixException;
import navalwar.server.gameengine.exceptions.WarAlreadyFinishedException;
import navalwar.server.gameengine.exceptions.WarAlreadyStartedException;
import navalwar.server.gameengine.exceptions.WarDoesNotExistException;
import navalwar.server.gameengine.info.WarInfo;
import navalwar.server.network.IServerNetworkModule;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import sun.org.mozilla.javascript.internal.ast.NewExpression;

public class GameEngineModuleTest {
	
	private WarInfo warInfoMock;
	private War warMock;
	private UnitAndPlace unitAndPlaceMock;
	private List<UnitAndPlace> units;

	@Before
	public void setUp(){
		warMock = EasyMock.createMock(War.class);
		warInfoMock = EasyMock.createMock(WarInfo.class);
		unitAndPlaceMock = EasyMock.createMock(UnitAndPlace.class);
	}
	
	
	/**
	Test para verificar que todos los numeros generados por la funcion
	createWar() estan entre 1 y 1000. Si el test funciona una vez, 
	siempre funcionara.
	V0.05
	**/
	@Test
	public void testCreateWar() {
		GameEngineModule game = new GameEngineModule();
		String warName = "nameTest";
		String warDesc = "descTest";
		boolean result = true;
		int n = game.createWar(warName, warDesc);
		if (n < 0 || n > 1000){
			result = false;
		}
		assertEquals(true, result);
	}
	
	
	/**
	Test para verificar que la funcion getWarInfo() devolvera los valores
	indicados al ejecutarla, mediante un paramatro previamente generado por
	createWar().
	
	Encontrado error: el metodo createWar() acepta solicitudes cuyos parametros
	son vacios. Aunque en la red y el cliente se bloquean dichas solicitudes,
	la funcion createWar() no es suficientemente robusta.
	V0.05
	**/
	
	@Test
	public void testGetWarInfo(){
		GameEngineModule game = new GameEngineModule();
		String warName = "";
		String warDesc = "";
		int warID = game.createWar(warName, warDesc);
		EasyMock.expect(warInfoMock.getWarID()).andReturn(warID);
		EasyMock.replay(warInfoMock);
		assertEquals(warID, (game.getWarInfo(warID)).getWarID());
		assertEquals(warName, (game.getWarInfo(warID)).getName());
		assertEquals(warDesc, (game.getWarInfo(warID)).getDesc());
		EasyMock.reset(warInfoMock);
		}
	
	/**
	Test para verificar que la funcion regArmy() registrara de forma adecuada al
	ejercito en una guerra existente guerra.
	
	Error encontrado: el metodo regArmy() puede tener un nombre vacio o nulo.
	Error encontrado: el metodo regArmy() permite registrar a un ejercito cuya
	lista de unidades este vacia.
	V0.05
	**/
	
	@Test
	public void testRegArmy(){
		GameEngineModule game = new GameEngineModule();
		String warName = "war";
		String warDesc = "desc";
		int warID = game.createWar(warName, warDesc);
		String armyName = null;
		//UnitAndPlace unit1 = new UnitAndPlace("Soldier", 5, 5);
		//UnitAndPlace unit2 = new UnitAndPlace("Soldier", 9, 9);
		//UnitAndPlace unit3 = new UnitAndPlace("Soldier", 6, 6);
		//UnitAndPlace unit4 = new UnitAndPlace("Soldier", 7, 7);
		//UnitAndPlace unit5 = new UnitAndPlace("Soldier", 8, 8);
		//UnitAndPlace unit6 = new UnitAndPlace("Soldier", 1, 1);
		List<UnitAndPlace> list = new ArrayList<>();
		//list.add(unit1);
		//list.add(unit2);
		//list.add(unit3);
		//list.add(unit4);
		//list.add(unit5);
		//list.add(unit6);
		try {
			game.regArmy(warID, armyName, list);
		} catch (WarDoesNotExistException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (WarAlreadyFinishedException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (WarAlreadyStartedException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (InvalidUnitNameException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (PlaceNotFreeToPlaceUnitException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (UnitCoordinatesOutsideMatrixException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		//game.regArmy(warID, "", );
		
	}

}
