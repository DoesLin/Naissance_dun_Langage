package simulation.systeme.exception;

/**
 * Exception levee par l'echeancier dans le cas d'une incoherence
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class EcheancierException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public EcheancierException() {
		super();
	}
	
	public EcheancierException(Throwable cause) {
		super(cause);
	}
	
	public EcheancierException(String message, Throwable cause) {
		super(message, cause);
	}
}
