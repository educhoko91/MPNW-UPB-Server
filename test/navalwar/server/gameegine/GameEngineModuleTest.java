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

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;



public class GameEngineModuleTest {
	
	private WarInfo warInfoMock;
	private UnitAndPlace unitAndPlaceMock;
	private UnitAndPlace unitAndPlaceMock2;

	@Before
	public void setUp(){
		warInfoMock = EasyMock.createMock(WarInfo.class);
		unitAndPlaceMock = EasyMock.createMock(UnitAndPlace.class);
		unitAndPlaceMock2 = EasyMock.createMock(UnitAndPlace.class);
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
	indicados al ejecutarla, mediante un parametro previamente generado por
	createWar().
	
	--Encontrado error: el metodo createWar() acepta solicitudes cuyos parametros
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
	ejercito en una guerra existente.
	
	--Error encontrado: el metodo regArmy() puede aceptar un nombre vacio o nulo.
	--Error encontrado: el metodo regArmy() permite registrar a un ejercito cuya
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
		String unitName = "Tank";
		int x = 1;
		int y = 1;
		EasyMock.expect(unitAndPlaceMock.getUnitName()).andReturn(unitName);
		EasyMock.expect(unitAndPlaceMock.getRow()).andReturn(x);
		EasyMock.expect(unitAndPlaceMock.getCol()).andReturn(y);
		EasyMock.replay(unitAndPlaceMock);
		List<UnitAndPlace> list = new ArrayList<>();
		list.add(unitAndPlaceMock);
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
		EasyMock.reset(unitAndPlaceMock);
	}
	
	/**
	Test para verificar que la funcion startWar() iniciara la guerra de forma adecuada
	
	--Error encontrado: el metodo startWar() no pide en ningun momento un parametro el cual
	identificara al creador de la guerra, lo cual significa que la guerra podra ser iniciada
	por cualquier jugador que se haya registrado en la misma. Solo el jugador que creo la 
	guerra es el unico que debería iniciarla.
	--Error encontrado: el metodo startWar() puede iniciar el juego con solo un jugador registrado
	V0.07
	**/
	
	@Test
	public void testStartWar(){
		GameEngineModule game = new GameEngineModule();
		int warID = game.createWar("WarExample", "DescExample");
		EasyMock.expect(unitAndPlaceMock.getUnitName()).andReturn("Plane");
		EasyMock.expect(unitAndPlaceMock.getRow()).andReturn(2);
		EasyMock.expect(unitAndPlaceMock.getCol()).andReturn(3);
		EasyMock.replay(unitAndPlaceMock);
		List<UnitAndPlace> list = new ArrayList<>();
		list.add(unitAndPlaceMock);
		try {
			game.regArmy(warID, "ArmyExample", list);
		} catch (WarDoesNotExistException e) {
			e.printStackTrace();
		} catch (WarAlreadyFinishedException e) {
			e.printStackTrace();
		} catch (WarAlreadyStartedException e) {
			e.printStackTrace();
		} catch (InvalidUnitNameException e) {
			e.printStackTrace();
		} catch (PlaceNotFreeToPlaceUnitException e) {
			e.printStackTrace();
		} catch (UnitCoordinatesOutsideMatrixException e) {
			e.printStackTrace();
		}
		
		EasyMock.expect(unitAndPlaceMock2.getUnitName()).andReturn("Tank");
		EasyMock.expect(unitAndPlaceMock2.getRow()).andReturn(4);
		EasyMock.expect(unitAndPlaceMock2.getCol()).andReturn(5);
		EasyMock.replay(unitAndPlaceMock2);
		List<UnitAndPlace> list2 = new ArrayList<>();
		list2.add(unitAndPlaceMock2);
		
		try {
			game.regArmy(warID, "ArmyExample2", list2);
		} catch (WarDoesNotExistException e) {
			e.printStackTrace();
		} catch (WarAlreadyFinishedException e) {
			e.printStackTrace();
		} catch (WarAlreadyStartedException e) {
			e.printStackTrace();
		} catch (InvalidUnitNameException e) {
			e.printStackTrace();
		} catch (PlaceNotFreeToPlaceUnitException e) {
			e.printStackTrace();
		} catch (UnitCoordinatesOutsideMatrixException e) {
			e.printStackTrace();
		}
		
		try {
			game.startWar(warID);
		} catch (WarDoesNotExistException e) {
			e.printStackTrace();
		} catch (WarAlreadyStartedException e) {
			e.printStackTrace();
		} catch (WarAlreadyFinishedException e) {
			e.printStackTrace();
		}
	}

}
