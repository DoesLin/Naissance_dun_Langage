package simulation.ihm.element;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import simulation.ihm.portee.Portee;
import simulation.ihm.portee.PorteeIndividu;
import simulation.systeme.Systeme;
import simulation.systeme.Voisin;

/**
 * Panel representant le systeme sous la forme d'un graphe
 * 
 * Chaque sommet (Circle) represente une portee, et chaque arc (Arrow) represente une relation
 * de voisinage entre deux individus, annotee de son delais respectif
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class GrapheSysteme extends Pane {
	private final int rayonSommet = 20;
	
	private ArrayList<Portee> portees;
	private double width;
	private double height;		
	private double theta;
	private double rayonGraphe;

	/**
	 * Initialise les valeurs de portees, largeur et hauteur du graphe du systeme
	 * 
	 * @param portees	les portees du systeme
	 * @param largeur	la largeur pour le panel
	 * @param hauteur	la hauteur pour le panel
	 */
	public GrapheSysteme(ArrayList<Portee> portees, double largeur, double hauteur) {
		this.portees = portees;
		this.width = largeur;
		this.height = hauteur;
		
		theta = 2 * Math.PI / (portees.size() - 1);
		rayonGraphe = Math.min(largeur / 2, hauteur / 2);
	}
	
	/**
	 * Genere les sommets du graphe (un par portee) et les renvoie
	 * 
	 * @return les sommets du graphe
	 */
	public HashMap<Portee, Circle> genererSommets() {
		HashMap<Portee, Circle> sommets = new HashMap<Portee, Circle>();
		
		int i = 0;
		for (Portee portee : portees) {
			if (portee.getClass() == PorteeIndividu.class) {
				PorteeIndividu porteeIndividu = (PorteeIndividu)portee;
				
				double x = width / 2 + (rayonGraphe - rayonSommet) * Math.cos(theta * i);
				double y = height / 2 + (rayonGraphe - rayonSommet) * Math.sin(theta * i);

				Circle sommet = new Circle(x, y, rayonSommet);
				sommet.getStyleClass().add("sommet");
				sommet.getStyleClass().add(porteeIndividu.obtenirIndividu().lireNomClasse());
				sommets.put(portee, sommet);
			}
			else {
				Circle sommetSysteme = new Circle(-rayonSommet, rayonSommet, rayonSommet);
				sommetSysteme.getStyleClass().add("sommet");
				sommetSysteme.getStyleClass().add("systeme");
				sommets.put(portees.get(i), sommetSysteme);
			}
			i++;
		}

		return sommets;
	}
	
	/**
	 * Genere et renvoie les arcs du graphe (un par relation de voisinage)
	 * 
	 * @return les arcs du graphe
	 */
	public ArrayList<Arrow> genererArcs() {
		ArrayList<Arrow> arcs = new ArrayList<Arrow>();

		for (Portee portee : portees) {
			if (portee.getClass() == PorteeIndividu.class) {
				PorteeIndividu porteeIndividu = (PorteeIndividu)portee;
				int individuID = porteeIndividu.obtenirIndividu().lireID();
				
				for (Voisin voisin : porteeIndividu.obtenirIndividu().obtenirVoisins()) {
					double x1, y1, x2, y2, newX1, newY1, newX2, newY2;
					int voisinID = voisin.obtenirIndividu().lireID();
					
					x1 = width / 2 + (rayonGraphe - 20) * Math.cos(theta * (individuID - 1));
					y1 = height / 2 + (rayonGraphe - 20) * Math.sin(theta * (individuID - 1));
					x2 = width / 2 + (rayonGraphe - 20) * Math.cos(theta * (voisinID - 1));
					y2 = height / 2 + (rayonGraphe - 20) * Math.sin(theta * (voisinID - 1));
					
					// On reduit la taille des arcs pour qu'ils n'entrent pas en collision avec les sommets 
					double pourcent = 0;
					if ((individuID + 1)  % Systeme.lireNombreIndividus() == voisinID % Systeme.lireNombreIndividus()
							|| individuID % Systeme.lireNombreIndividus() == (voisinID + 1)  % Systeme.lireNombreIndividus()) {
						pourcent = 0.80;
						newX1 = (1 - pourcent) * x2 + pourcent * x1;
						newY1 = (1 - pourcent) * y2 + pourcent * y1;
						newX2 = (pourcent) * x2 + (1 - pourcent) * x1;
						newY2 = (pourcent) * y2 + (1 - pourcent) * y1;
					}
					else {
						pourcent = 0.88;
						newX1 = (1 - pourcent) * x2 + pourcent * x1;
						newY1 = (1 - pourcent) * y2 + pourcent * y1;
						newX2 = pourcent * x2 + (1 - pourcent) * x1;
						newY2 = pourcent * y2 + (1 - pourcent) * y1;
					}
					
					Arrow arc = new Arrow(newX1, newY1, newX2, newY2);
					arcs.add(arc);
				}
			}
		}
		
		return arcs;
	}
	
	/**
	 * Genere et renvoie les legendes associees aux sommets du graphe
	 * 
	 * @return les legendes associees aux sommets du graphe
	 */
	public ArrayList<Node> genererLegendes() {
		ArrayList<Node> legendes = new ArrayList<Node>();

		int i = 0;
		for (Portee portee : portees) {
			if (portee.getClass() == PorteeIndividu.class) {
				PorteeIndividu porteeIndividu = (PorteeIndividu)portee;
				
				double x = width / 2 + (rayonGraphe + 48) * Math.cos(theta * i) - 16;
				double y = height / 2 + (rayonGraphe + 24) * Math.sin(theta * i) + 4;
				legendes.add(new Text(x, y, portee.toString()));

				Rectangle pastille = new Rectangle(x - 20, y - 11, 14, 14);
				pastille.getStyleClass().add(porteeIndividu.obtenirIndividu().lireNomClasse());
				
				legendes.add(pastille);
			}
			else {
				legendes.add(new Text(-42, -16, portee.toString()));
			}
			i++;
		}

		return legendes;
	}
	
	/**
	 * Genere et renvoie les delais associes aux arcs du graphe
	 * 
	 * @return les delais associes aux arcs du graphe
	 */
	public ArrayList<Node> genererDelais() {
		ArrayList<Node> delais = new ArrayList<Node>();

		for (Portee portee : portees) {
			if (portee.getClass() == PorteeIndividu.class) {
				PorteeIndividu porteeIndividu = (PorteeIndividu)portee;
				int individuID = porteeIndividu.obtenirIndividu().lireID();
				
				for (Voisin voisin : porteeIndividu.obtenirIndividu().obtenirVoisins()) {
					double x1, y1, x2, y2, newX2, newY2;
					int voisinID = voisin.obtenirIndividu().lireID();

					x1 = width / 2 + (rayonGraphe - 20) * Math.cos(theta * (individuID - 1)) - 4;
					y1 = height / 2 + (rayonGraphe - 20) * Math.sin(theta * (individuID - 1)) + 3;
					x2 = width / 2 + (rayonGraphe - 20) * Math.cos(theta * (voisinID - 1)) - 4;
					y2 = height / 2 + (rayonGraphe - 20) * Math.sin(theta * (voisinID - 1)) + 3;
					
					double pourcent = 0;
					if ((individuID + 1)  % Systeme.lireNombreIndividus() == voisinID % Systeme.lireNombreIndividus()
							|| individuID % Systeme.lireNombreIndividus() == (voisinID + 1)  % Systeme.lireNombreIndividus()) {
						
						pourcent = 0.67;
						newX2 = (pourcent) * x1 + (1 - pourcent) * x2;
						newY2 = (pourcent) * y1 + (1 - pourcent) * y2;
					}
					else {
						pourcent = 0.78;
						newX2 = pourcent * x1 + (1 - pourcent) * x2;
						newY2 = pourcent * y1 + (1 - pourcent) * y2;
					}
				
					String stringDelais = voisin.lireDelais().toString();
				
					Rectangle delaisBackground = new Rectangle(newX2 - 2, newY2 - 16, 7 * stringDelais.length() + 1, 20);
					delaisBackground.getStyleClass().add("delais-bg");
					
					Text delaisText = new Text(newX2, newY2, stringDelais);
					delaisText.getStyleClass().add("delais");
					delaisText.getStyleClass().add(porteeIndividu.obtenirIndividu().lireNomClasse());
					
					delais.add(delaisBackground);
					delais.add(delaisText);
				}
			}
		}

		return delais;
	}
}
