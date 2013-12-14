package navalwar.server.gameengine.exceptions;

public class InvalidUnitNameException extends Exception {

	private String name;
	
	public InvalidUnitNameException(String name) {
		this.name = name;
	}
	
	public String getName() { return name; }
	
	public String toString() {
		return super.toString() + " name: " + name + "\n";
	}


}
