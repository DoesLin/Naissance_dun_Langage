package simulation.systeme.exception;

/**
 * Exception levee a l'interieur d'une implementation de la classe abstraite StrategieSelection ou StrategieSuccession
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class StrategieException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public StrategieException() {
		super();
	}
	
	public StrategieException(Throwable cause) {
		super(cause);
	}
	
	public StrategieException(String message, Throwable cause) {
		super(message, cause);
	}
}
