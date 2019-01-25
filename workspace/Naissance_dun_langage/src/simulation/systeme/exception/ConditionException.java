package simulation.systeme.exception;

/**
 * Exception levee a l'interieur d'une implementation de la classe abstraite Condition
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class ConditionException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ConditionException() {
		super();
	}
	
	public ConditionException(Throwable cause) {
		super(cause);
	}
	
	public ConditionException(String message, Throwable cause) {
		super(message, cause);
	}
}
