package simulation.systeme;

import simulation.systeme.temps.Delais;

/**
 * Un voisin est un couple (Individu, Delais)
 * Un individu du systeme peut posseder un ou plusieurs voisins, excepte lui-meme et ne peut
 * emettre des lemmes (respectivement se faire succeder) que vers (respectivement par l'un de) ses voisins
 *
 * Le delais entre un individu et son voisin est le laps de temps qui separe l'emission d'un lemme
 * par le premier et la reception de celui-ci par le second, mais aussi le temps necessaire a un voisin
 * pour prendre la succession, i.e emettre a son tour un lemme
 *
 * Dans notre simulation, une relation de voisinage est unilaterale : si l'individu A a pour voisin l'individu B,
 * alors l'individu B n'a pas necessairement l'individu A comme voisin
 *
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Voisin {
	private Individu individu;
	private Delais delais;
	
	/**
	 * Cree un voisin avec l'individu et le delais donnes en parametre
	 * 
	 * @param individu	l'individu voisin
	 * @param delais	le delais pour atteindre l'individu voisin
	 */
	public Voisin(Individu individu, Delais delais) {
		this.individu = individu;
		this.delais = delais;
	}
	
	/**
	 * Renvoie l'individu voisin
	 * 
	 * @return l'individu voisin
	 */
	public Individu obtenirIndividu() {
		return individu;
	}
	
	/**
	 * Renvoie le delais pour atteindre l'individu voisin
	 * 
	 * @return le delais pour atteindre l'individu voisin
	 */
	public Delais lireDelais() {
		return delais;
	}
}
