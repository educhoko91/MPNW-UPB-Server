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
	
	private War warMock;
	private War warMock2;
	private WarInfo warInfoMock;
	private UnitAndPlace unitAndPlaceMock;
	private UnitAndPlace unitAndPlaceMock2;
	private UnitAndPlace unitAndPlaceMock3;
	private UnitAndPlace unitAndPlaceMock4;
	private UnitAndPlace unitAndPlaceMock5;
	private UnitAndPlace unitAndPlaceMock6;
	private UnitAndPlace unitAndPlaceMock7;

	@Before
	public void setUp(){
		warMock = EasyMock.createMock(War.class);
		warMock2 = EasyMock.createMock(War.class);
		warInfoMock = EasyMock.createMock(WarInfo.class);
		unitAndPlaceMock = EasyMock.createMock(UnitAndPlace.class);
		unitAndPlaceMock2 = EasyMock.createMock(UnitAndPlace.class);
		unitAndPlaceMock3 = EasyMock.createMock(UnitAndPlace.class);
		unitAndPlaceMock4 = EasyMock.createMock(UnitAndPlace.class);
		unitAndPlaceMock5= EasyMock.createMock(UnitAndPlace.class);
		unitAndPlaceMock6 = EasyMock.createMock(UnitAndPlace.class);
		unitAndPlaceMock7 = EasyMock.createMock(UnitAndPlace.class);
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
	Test para verificar que la funcion createWar() podra crear una guerra siempre
	y cuando esta reciba parametros correctos
	
	--Encontrado error: el metodo createWar() acepta solicitudes cuyos parametros
	son vacios. Aunque en la red y el cliente se bloquean dichas solicitudes,
	la funcion createWar() no es suficientemente robusta.
	V0.05
	**/
	
	@Test
	public void testCreateWar2(){
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
	Test para verificar que la funcion createWar() no pueda generar warIds que no esten 
	por debajo de 0 o por encima de 1000 
	
	--Encontrado error: el metodo createWar() puede llegar a generar warIds que no estan
	dentro de sus limites
	V0.05
	**/
	
	@Test (timeout = 10000)
	public void testCreateWar3(){
		GameEngineModule game = new GameEngineModule();
		int [] nwar = new int[1200];
		for (int i=0;i<1200;i++){
			nwar[i] = game.createWar("war", "des");
		}
		
		for (int i=0;i<1200;i++){
			System.out.print(nwar[i] + ",");
		}
	}
	
	/**
	Test para verificar que la funcion regArmy() registrara de forma adecuada al
	ejercito en una guerra existente.
	
	--Error encontrado: el metodo regArmy() puede aceptar un nombre de ejercito
	vacio o nulo.
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
	
	/////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testRegArmy2(){
		GameEngineModule game = new GameEngineModule();
		String warName = "war";
		String warDesc = "desc";
		int warID = game.createWar(warName, warDesc);
		String armyName = "army";
		List<UnitAndPlace> list = new ArrayList<>();
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
	--Error encontrado: el metodo startWar() no esta conciente de cuantos ejercitos pueden registrarse
	en una guerra, dando paso a que se puedan registrar más ejercitos de lo que esta permitido
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
		
		EasyMock.reset(unitAndPlaceMock);
		EasyMock.reset(unitAndPlaceMock2);
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testStartWar2(){
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
		
		try {
			game.startWar(warID);
		} catch (WarDoesNotExistException e) {
			e.printStackTrace();
		} catch (WarAlreadyStartedException e) {
			e.printStackTrace();
		} catch (WarAlreadyFinishedException e) {
			e.printStackTrace();
		}
		
		EasyMock.reset(unitAndPlaceMock);
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testStartWar3(){
		GameEngineModule game = new GameEngineModule();
		int warID = game.createWar("WarExample", "DescExample");
		EasyMock.expect(unitAndPlaceMock.getUnitName()).andReturn("Tank");
		EasyMock.expect(unitAndPlaceMock.getRow()).andReturn(2);
		EasyMock.expect(unitAndPlaceMock.getCol()).andReturn(3);
		EasyMock.replay(unitAndPlaceMock);
		List<UnitAndPlace> list = new ArrayList<>();
		list.add(unitAndPlaceMock);
		
		EasyMock.expect(unitAndPlaceMock2.getUnitName()).andReturn("Tank");
		EasyMock.expect(unitAndPlaceMock2.getRow()).andReturn(2);
		EasyMock.expect(unitAndPlaceMock2.getCol()).andReturn(3);
		EasyMock.replay(unitAndPlaceMock2);
		List<UnitAndPlace> list2 = new ArrayList<>();
		list2.add(unitAndPlaceMock2);
		
		EasyMock.expect(unitAndPlaceMock3.getUnitName()).andReturn("Tank");
		EasyMock.expect(unitAndPlaceMock3.getRow()).andReturn(2);
		EasyMock.expect(unitAndPlaceMock3.getCol()).andReturn(3);
		EasyMock.replay(unitAndPlaceMock3);
		List<UnitAndPlace> list3 = new ArrayList<>();
		list3.add(unitAndPlaceMock3);
		
		EasyMock.expect(unitAndPlaceMock4.getUnitName()).andReturn("Tank");
		EasyMock.expect(unitAndPlaceMock4.getRow()).andReturn(2);
		EasyMock.expect(unitAndPlaceMock4.getCol()).andReturn(3);
		EasyMock.replay(unitAndPlaceMock4);
		List<UnitAndPlace> list4 = new ArrayList<>();
		list4.add(unitAndPlaceMock4);
		
		EasyMock.expect(unitAndPlaceMock5.getUnitName()).andReturn("Tank");
		EasyMock.expect(unitAndPlaceMock5.getRow()).andReturn(2);
		EasyMock.expect(unitAndPlaceMock5.getCol()).andReturn(3);
		EasyMock.replay(unitAndPlaceMock5);
		List<UnitAndPlace> list5 = new ArrayList<>();
		list5.add(unitAndPlaceMock5);
		
		EasyMock.expect(unitAndPlaceMock6.getUnitName()).andReturn("Tank");
		EasyMock.expect(unitAndPlaceMock6.getRow()).andReturn(2);
		EasyMock.expect(unitAndPlaceMock6.getCol()).andReturn(3);
		EasyMock.replay(unitAndPlaceMock6);
		List<UnitAndPlace> list6 = new ArrayList<>();
		list6.add(unitAndPlaceMock6);
		
		EasyMock.expect(unitAndPlaceMock7.getUnitName()).andReturn("Tank");
		EasyMock.expect(unitAndPlaceMock7.getRow()).andReturn(2);
		EasyMock.expect(unitAndPlaceMock7.getCol()).andReturn(3);
		EasyMock.replay(unitAndPlaceMock7);
		List<UnitAndPlace> list7 = new ArrayList<>();
		list7.add(unitAndPlaceMock7);
		try {
			game.regArmy(warID, "ArmyExample1", list);
			game.regArmy(warID, "ArmyExample2", list2);
			game.regArmy(warID, "ArmyExample3", list3);
			game.regArmy(warID, "ArmyExample4", list4);
			game.regArmy(warID, "ArmyExample5", list5);
			game.regArmy(warID, "ArmyExample6", list6);
			game.regArmy(warID, "ArmyExample7", list7);
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
		
		EasyMock.reset(unitAndPlaceMock);
		EasyMock.reset(unitAndPlaceMock2);
		EasyMock.reset(unitAndPlaceMock3);
		EasyMock.reset(unitAndPlaceMock4);
		EasyMock.reset(unitAndPlaceMock5);
		EasyMock.reset(unitAndPlaceMock6);
		EasyMock.reset(unitAndPlaceMock7);
	}	
	

}
