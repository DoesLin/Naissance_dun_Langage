package simulation.ihm;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import simulation.ihm.bean.LemmeMemorise;
import simulation.ihm.bean.Parametre;
import simulation.ihm.element.GrapheSysteme;
import simulation.ihm.portee.Portee;
import simulation.ihm.portee.PorteeIndividu;
import simulation.ihm.portee.PorteeSysteme;
import simulation.systeme.Individu;
import simulation.systeme.Systeme;
import simulation.systeme.evenement.enumeration.IssueEvenement;
import simulation.systeme.evenement.enumeration.TypeEvenement;
import simulation.systeme.lexique.Lemme;
import simulation.systeme.lexique.OccurrenceLemme;
import simulation.systeme.temps.Date;

/**
 * Controleur de l'application
 * 
 * Il permet de fournir a l'IHM les informations du systeme a travers les classes PorteeIndividu et PorteeSysteme 
 * et s'occupe interpreter les actions de l'utilisateur
 * 
 * @author Charles MECHERIKI Yongda LIN
 *
 */
public class Controleur implements Initializable {

	// Boites de choix
	@FXML
	private ChoiceBox<Portee> choixPortee;
	private ArrayList<Portee> portees;
	private Portee portee;

	@FXML
	private ChoiceBox<TypeEvenement> choixTypeEvenementOccurrences;
	private TypeEvenement typeEvenementOccurrences;

	@FXML
	private ChoiceBox<IssueEvenement> choixIssueEvenementOccurrences;
	private IssueEvenement issueEvenementOccurrences;

	// Graphe
	@FXML
	private Pane grapheSysteme;

	// Diagrammes
	@FXML
	private PieChart diagrammeLexique;

	@FXML
	private Pane conteneurDiagrammeEvolutionLexique;
	@FXML
	private StackedBarChart<String, Number> diagrammeEvolutionLexique;

	@FXML
	private Pane conteneurDiagrammeEvolutionOccurrences;
	@FXML
	private StackedBarChart<String, Number> diagrammeEvolutionOccurrences;

	private final int ecartementBarresDiagrammes = 64;

	// Textes
	@FXML
	private TextFlow texteEvenements;

	// Tables et comparateurs
	@FXML
	private TableView<Parametre> tableParametres;

	@FXML
	private TableView<LemmeMemorise> tableLexique;
	private Comparator<LemmeMemorise> comparateurTableLexique;

	@FXML
	private TableView<OccurrenceLemme> tableOccurrences;
	private Comparator<OccurrenceLemme> comparateurTableOccurrences;

	// Dates
	private final int nombreDatesReperes = 6;
	private ArrayList<Date> datesRepere;
	private Date dateDetailLexique;
	private Date dateDetailOccurrences;

	// Fleches
	private Polygon flecheGaucheDetailDiagrammeLexique;
	private Polygon flecheDroiteDetailDiagrammeLexique;
	private Polygon flecheGaucheDetailDiagrammeOccurrences;
	private Polygon flecheDroiteDetailDiagrammeOccurrences;

	// Informations fleches diagrammes
	private final double yFleches = 248;
	private final double xPremiereFlecheGauche = 208;
	private final double xPremiereFlecheDroite = 243;
	private final double xEntreFleches = 86;
	private final Double[] pointsFlecheGauche = new Double[] { 0.0, 0.0, 12.0, 6.0, 0.0, 12.0 };
	private final Double[] pointsFlecheDroite = new Double[] { 12.0, 0.0, 0.0, 6.0, 12.0, 12.0 };


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initialiserPortees();
		initialiserDates();
		initialiserTypesEvenement();
		initialiserIssuesEvenement();

		creerGrapheSysteme();
		creerFlechesDetailsDiagrammes();

		creerDiagrammeLexique();
		creerDiagrammeEvolutionLexique();
		remplirTableLexique();

		creerDiagrammeEvolutionOccurrences();
		remplirTableOccurrences();

		remplirTexteEvenements();
		remplirTableParametres();
	}

	/**
	 * Lance les listeners associes aux differentes ChoiceBox de l'IHM
	 */
	public void lancerListeners() {
		choixPortee.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Portee>() {
			@Override
			public void changed(ObservableValue<? extends Portee> observable, Portee anciennePortee,
					Portee nouvellePortee) {
				portee = choixPortee.getSelectionModel().getSelectedItem();
				creerDiagrammeLexique();
				creerDiagrammeEvolutionLexique();
				remplirTableLexique();
				remplirTexteEvenements();
				remplirTableParametres();
				creerDiagrammeEvolutionOccurrences();
				remplirTableOccurrences();
			}
		});

		choixTypeEvenementOccurrences.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<TypeEvenement>() {
					@Override
					public void changed(ObservableValue<? extends TypeEvenement> observable, TypeEvenement ancienType,
							TypeEvenement nouveauType) {
						typeEvenementOccurrences = choixTypeEvenementOccurrences.getSelectionModel().getSelectedItem();
						creerDiagrammeEvolutionOccurrences();
						remplirTableOccurrences();
					}
				});

		choixIssueEvenementOccurrences.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<IssueEvenement>() {
					@Override
					public void changed(ObservableValue<? extends IssueEvenement> observable,
							IssueEvenement ancienneIssue, IssueEvenement nouvelleIssue) {
						issueEvenementOccurrences = choixIssueEvenementOccurrences.getSelectionModel()
								.getSelectedItem();
						creerDiagrammeEvolutionOccurrences();
						remplirTableOccurrences();
					}
				});
	}

	/**
	 * Initialise la ChoiceBox portees avec la portee relative au systeme et celles relatives aux individus
	 */
	public void initialiserPortees() {
		portees = new ArrayList<Portee>();

		for (Individu individu : Systeme.obtenirIndividus()) {
			portees.add(new PorteeIndividu(individu));
		}
		portees.add(new PorteeSysteme());

		choixPortee.getItems().addAll(FXCollections.observableArrayList(portees));
		choixPortee.getSelectionModel().selectLast();
		portee = choixPortee.getSelectionModel().getSelectedItem();
	}

	/**
	 * Initialise les dates reperes et les dates de details des diagrammes en fonction de la date de l'horloge du systeme
	 */
	public void initialiserDates() {
		datesRepere = new ArrayList<Date>();

		int valeurDateActuelle = Systeme.lireDateHorloge().lireValeur();
		if (valeurDateActuelle <= nombreDatesReperes) {
			for (int indice = 0; indice < nombreDatesReperes; indice++) {
				datesRepere.add(Date.depuisValeur(indice));
			}
		} else {
			for (int indice = 0; indice < nombreDatesReperes - 1; indice++) {
				datesRepere.add(Date.depuisValeur(indice * (valeurDateActuelle / (nombreDatesReperes - 1))));
			}
			datesRepere.add(Systeme.lireDateHorloge());
		}

		dateDetailLexique = Systeme.lireDateHorloge();
		dateDetailOccurrences = Systeme.lireDateHorloge();
	}

	/**
	 * Cree les fleches de details des diagrammes
	 */
	public void creerFlechesDetailsDiagrammes() {
		int indiceDateDetail = datesRepere.indexOf(Systeme.lireDateHorloge());

		flecheGaucheDetailDiagrammeLexique = new Polygon();
		flecheGaucheDetailDiagrammeLexique.getPoints().addAll(pointsFlecheGauche);
		flecheGaucheDetailDiagrammeLexique.getStyleClass().add("fleche");
		flecheGaucheDetailDiagrammeLexique.setLayoutX(xPremiereFlecheGauche + indiceDateDetail * xEntreFleches);
		flecheGaucheDetailDiagrammeLexique.setLayoutY(yFleches);

		flecheDroiteDetailDiagrammeLexique = new Polygon();
		flecheDroiteDetailDiagrammeLexique.getPoints().addAll(pointsFlecheDroite);
		flecheDroiteDetailDiagrammeLexique.getStyleClass().add("fleche");
		flecheDroiteDetailDiagrammeLexique.setLayoutX(xPremiereFlecheDroite + indiceDateDetail * xEntreFleches);
		flecheDroiteDetailDiagrammeLexique.setLayoutY(yFleches);

		flecheGaucheDetailDiagrammeOccurrences = new Polygon();
		flecheGaucheDetailDiagrammeOccurrences.getPoints().addAll(pointsFlecheGauche);
		flecheGaucheDetailDiagrammeOccurrences.getStyleClass().add("fleche");
		flecheGaucheDetailDiagrammeOccurrences.setLayoutX(xPremiereFlecheGauche + indiceDateDetail * xEntreFleches);
		flecheGaucheDetailDiagrammeOccurrences.setLayoutY(yFleches);

		flecheDroiteDetailDiagrammeOccurrences = new Polygon();
		flecheDroiteDetailDiagrammeOccurrences.getPoints().addAll(pointsFlecheDroite);
		flecheDroiteDetailDiagrammeOccurrences.getStyleClass().add("fleche");
		flecheDroiteDetailDiagrammeOccurrences.setLayoutX(xPremiereFlecheDroite + indiceDateDetail * xEntreFleches);
		flecheDroiteDetailDiagrammeOccurrences.setLayoutY(yFleches);

		conteneurDiagrammeEvolutionLexique.getChildren().add(flecheGaucheDetailDiagrammeLexique);
		conteneurDiagrammeEvolutionLexique.getChildren().add(flecheDroiteDetailDiagrammeLexique);
		conteneurDiagrammeEvolutionOccurrences.getChildren().add(flecheGaucheDetailDiagrammeOccurrences);
		conteneurDiagrammeEvolutionOccurrences.getChildren().add(flecheDroiteDetailDiagrammeOccurrences);
	}

	/**
	 * Initialise la ChoiceBox contenant les types d'evenement
	 */
	public void initialiserTypesEvenement() {
		choixTypeEvenementOccurrences.getItems().addAll(FXCollections.observableArrayList(TypeEvenement.values()));
		choixTypeEvenementOccurrences.getSelectionModel().selectFirst();
		typeEvenementOccurrences = choixTypeEvenementOccurrences.getSelectionModel().getSelectedItem();
	}

	/**
	 * Initialise la ChoiceBox contenant les issues d'evenement
	 */
	public void initialiserIssuesEvenement() {
		choixIssueEvenementOccurrences.getItems().addAll(FXCollections.observableArrayList(IssueEvenement.values()));
		choixIssueEvenementOccurrences.getSelectionModel().selectFirst();
		issueEvenementOccurrences = choixIssueEvenementOccurrences.getSelectionModel().getSelectedItem();
	}

	/**
	 * Cree le graphe du systeme et associe a ses sommets le changement de portee adequat
	 */
	public void creerGrapheSysteme() {
		GrapheSysteme graphe = new GrapheSysteme(portees, grapheSysteme.getPrefWidth(), grapheSysteme.getPrefHeight());

		HashMap<Portee, Circle> sommets = graphe.genererSommets();
		sommets.get(portee).getStyleClass().add("focus");
		for (Portee portee : sommets.keySet()) {
			sommets.get(portee).addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					Portee anciennePortee = choixPortee.getSelectionModel().getSelectedItem();
					if (sommets.get(anciennePortee).getStyleClass().contains("focus")) {
						sommets.get(anciennePortee).getStyleClass().remove("focus");
					}
					sommets.get(portee).getStyleClass().add("focus");
					choixPortee.getSelectionModel().select(portee);
					remplirTableLexique();
				}
			});
		}

		grapheSysteme.getChildren().addAll(sommets.values());
		grapheSysteme.getChildren().addAll(graphe.genererArcs());
		grapheSysteme.getChildren().addAll(graphe.genererLegendes());
		grapheSysteme.getChildren().addAll(graphe.genererDelais());
	}

	/**
	 * Remplie le TextFlow texteEvenements contenant les evenements relatifs a la portee actuelle
	 */
	public void remplirTexteEvenements() {
		ArrayList<OccurrenceLemme> listeOccurrences = portee.obtenirListeOccurrences(Systeme.lireDateHorloge(),
				TypeEvenement.QUELCONQUE, IssueEvenement.QUELCONQUE);

		ObservableList<Text> textesEvenements = FXCollections.observableArrayList();
		textesEvenements.add(new Text(
				"===========================================================\n" + "\t\t\t\t\t\tDébut de la simulation\n"
						+ "===========================================================\n"));

		for (OccurrenceLemme occurrence : listeOccurrences) {
			String stringDebutEvenement = "";
			switch (occurrence.getTypeEvenement()) {
			case EMISSION:
				stringDebutEvenement += " ";
				break;
			case RECEPTION:
				stringDebutEvenement += "\t";
				break;
			case MEMORISATION:
				stringDebutEvenement += "\t\t";
				break;
			case ELIMINATION:
				stringDebutEvenement += "\t\t\t";
				break;
			case QUELCONQUE:
				break;
			}

			Text texteDebutEvenement = new Text(stringDebutEvenement);
			textesEvenements.add(texteDebutEvenement);

			Text texteTypeEvenement = new Text(occurrence.getTypeEvenement().toString());
			texteTypeEvenement.getStyleClass().add(occurrence.getTypeEvenement().toString().toLowerCase());
			texteTypeEvenement.getStyleClass().add("gras");
			textesEvenements.add(texteTypeEvenement);

			Text texteEntreTypeEtIssue = new Text();
			textesEvenements.add(texteEntreTypeEtIssue);

			Text texteIssue = new Text(" (" + occurrence.getIssueEvenement().toString() + ") ");
			texteIssue.getStyleClass().add(occurrence.getIssueEvenement().toString().toLowerCase());
			textesEvenements.add(texteIssue);

			Text texteEntreIssueEtIndividu = new Text(" par ");
			textesEvenements.add(texteEntreIssueEtIndividu);

			Text texteIndividu = new Text(occurrence.getIndividu().toString());
			texteIndividu.getStyleClass().add(occurrence.getIndividu().lireNomClasse());
			texteIndividu.getStyleClass().add("gras");
			textesEvenements.add(texteIndividu);

			Text texteEntreIndividuEtLemme = new Text(" de ");
			textesEvenements.add(texteEntreIndividuEtLemme);

			Text texteLemme = new Text(occurrence.getLemme().toString());
			texteLemme.getStyleClass().add(occurrence.getLemme().lireInitiateur().lireNomClasse());
			texteLemme.getStyleClass().add("gras");
			textesEvenements.add(texteLemme);

			Text texteEntreLemmeEtDate = new Text(" à la date ");
			textesEvenements.add(texteEntreLemmeEtDate);

			Text texteDate = new Text(occurrence.getDate().toString());
			texteDate.getStyleClass().add("gras");
			textesEvenements.add(texteDate);

			Text texteEntreDateEtID = new Text(" [ev. ");
			textesEvenements.add(texteEntreDateEtID);

			Text texteID = new Text(String.valueOf(occurrence.getID()));
			texteID.getStyleClass().add("gras");
			textesEvenements.add(texteID);

			Text texteFinEvenement = new Text("]\n");
			textesEvenements.add(texteFinEvenement);
		}

		textesEvenements.add(
			new Text("===========================================================\n"
					+ "\t\t\t\tFin de la simulation : " + Systeme.lireMessageFin() + "\n"
					+ "==========================================================="));

		texteEvenements.getChildren().clear();
		ObservableList<Node> evenements = texteEvenements.getChildren();
		evenements.addAll(textesEvenements);
	}

	/**
	 * Remplie la TableView tableParametres contenant les parametres relatifs la portee actuelle
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void remplirTableParametres() {
		ArrayList<Parametre> donneesTableParametres = portee.obtenirListeParametres();
		ObservableList<Parametre> obsListeParametres = FXCollections.observableArrayList(donneesTableParametres);

		((TableColumn) tableParametres.getColumns().get(0))
				.setCellValueFactory(new PropertyValueFactory<Parametre, String>("nom"));
		((TableColumn) tableParametres.getColumns().get(1))
				.setCellValueFactory(new PropertyValueFactory<Parametre, String>("valeur"));

		tableParametres.setItems(obsListeParametres);
	}

	/**
	 * Cree le diagramme representant la composition du lexique relatif a la portee actuelle
	 */
	public void creerDiagrammeLexique() {
		HashMap<Individu, Integer> donnees = portee
				.obtenirDonneesDiagrammeCompositionLexique(Systeme.lireDateHorloge());

		ObservableList<PieChart.Data> donneesDiagramme = FXCollections.observableArrayList();
		for (Individu individu : Systeme.obtenirIndividus()) {
			PieChart.Data donneeDiagramme = new PieChart.Data(individu.toString(), donnees.get(individu));
			donneesDiagramme.add(donneeDiagramme);
		}

		diagrammeLexique.getData().clear();
		diagrammeLexique.setData(donneesDiagramme);
		diagrammeLexique.setLabelsVisible(false);
		diagrammeLexique.setTitle("Composition finale du lexique [" + portee + "]");
	}

	/**
	 * Cree le diagramme representant l'evolution de la composition du lexique relatif a la portee actuelle
	 */
	public void creerDiagrammeEvolutionLexique() {
		diagrammeEvolutionLexique.getXAxis().setLabel("Date");
		diagrammeEvolutionLexique.getYAxis().setLabel("Taille");

		diagrammeEvolutionLexique.setTitle("Évolution du lexique [" + portee + "]");
		diagrammeEvolutionLexique.setCategoryGap(ecartementBarresDiagrammes);

		List<String> datesRepereString = datesRepere.stream().map(Date::toString).collect(Collectors.toList());
		((CategoryAxis) diagrammeEvolutionOccurrences.getXAxis())
				.setCategories(FXCollections.<String>observableArrayList(datesRepereString));

		HashMap<Date, HashMap<Individu, Integer>> donnees = portee
				.obtenirDonneesDiagrammeEvolutionCompositionLexique(datesRepere);

		ObservableList<XYChart.Data<String, Number>> donneesDiagrammeEvolutionLexique = null;
		ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();

		for (Individu individu : Systeme.obtenirIndividus()) {
			donneesDiagrammeEvolutionLexique = FXCollections.observableArrayList();
			for (Date date : datesRepere) {
				donneesDiagrammeEvolutionLexique
						.add(new Data<String, Number>(date.toString(), donnees.get(date).get(individu)));
			}
			XYChart.Series<String, Number> serie = new XYChart.Series<String, Number>();
			serie.setData(donneesDiagrammeEvolutionLexique);
			serie.setName(individu.toString());
			series.add(serie);
		}
		diagrammeEvolutionLexique.getData().clear();
		diagrammeEvolutionLexique.setData(series);

		for (Series<String, Number> _series : diagrammeEvolutionLexique.getData()) {
			for (XYChart.Data<String, Number> donnee : _series.getData()) {
				donnee.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						dateDetailLexique = Date.depuisValeur(Integer.parseUnsignedInt(donnee.getXValue()));
						int indiceDateDetailLexique = datesRepere.indexOf(dateDetailLexique);
						flecheGaucheDetailDiagrammeLexique
								.setLayoutX(xPremiereFlecheGauche + indiceDateDetailLexique * xEntreFleches);
						flecheDroiteDetailDiagrammeLexique
								.setLayoutX(xPremiereFlecheDroite + indiceDateDetailLexique * xEntreFleches);
						remplirTableLexique();
					}
				});
			}
		}
	}

	/**
	 * Cree le diagramme representant l'evolution des occurrences de lemmes relatif a la portee actuelle
	 */
	public void creerDiagrammeEvolutionOccurrences() {
		diagrammeEvolutionOccurrences.getXAxis().setLabel("Date");
		diagrammeEvolutionOccurrences.getYAxis().setLabel("Occurrences");

		diagrammeEvolutionOccurrences.setTitle("Évolution des occurrences [" + portee + " - " + typeEvenementOccurrences
				+ " - " + issueEvenementOccurrences + "]");
		diagrammeEvolutionOccurrences.setCategoryGap(ecartementBarresDiagrammes);

		List<String> datesRepereString = datesRepere.stream().map(Date::toString).collect(Collectors.toList());
		((CategoryAxis) diagrammeEvolutionOccurrences.getXAxis())
				.setCategories(FXCollections.<String>observableArrayList(datesRepereString));

		HashMap<Date, HashMap<Individu, Integer>> donnees = portee.obtenirDonneesDiagrammeEvolutionOccurrences(
				datesRepere, typeEvenementOccurrences, issueEvenementOccurrences);
		ObservableList<XYChart.Data<String, Number>> donneesDiagrammeEvolutionOccurrences = null;
		ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();
		for (Individu individu : Systeme.obtenirIndividus()) {
			donneesDiagrammeEvolutionOccurrences = FXCollections.observableArrayList();
			for (Date date : datesRepere) {
				donneesDiagrammeEvolutionOccurrences
						.add(new Data<String, Number>(date.toString(), donnees.get(date).get(individu)));
			}
			XYChart.Series<String, Number> serie = new XYChart.Series<String, Number>();
			serie.setData(donneesDiagrammeEvolutionOccurrences);
			serie.setName(individu.toString());
			series.add(serie);
		}
		diagrammeEvolutionOccurrences.getData().clear();
		diagrammeEvolutionOccurrences.setData(series);

		for (Series<String, Number> _series : diagrammeEvolutionOccurrences.getData()) {
			for (XYChart.Data<String, Number> donnee : _series.getData()) {
				donnee.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						dateDetailOccurrences = Date.depuisValeur(Integer.parseUnsignedInt(donnee.getXValue()));
						int indiceDateDetailOccurrences = datesRepere.indexOf(dateDetailOccurrences);
						flecheGaucheDetailDiagrammeOccurrences
								.setLayoutX(xPremiereFlecheGauche + indiceDateDetailOccurrences * xEntreFleches);
						flecheDroiteDetailDiagrammeOccurrences
								.setLayoutX(xPremiereFlecheDroite + indiceDateDetailOccurrences * xEntreFleches);
						remplirTableOccurrences();
					}
				});
			}
		}
	}
	
	/**
	 * Remplie la TableView tableLexique contenant les lemmes (memorises) composant le lexique final 
	 * relativement a la portee actuelle et la date selectionnee par l'utilisateur
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void remplirTableLexique() {
		ArrayList<LemmeMemorise> listeLemmesMemorises = portee.obtenirListeLemmesMemorises(dateDetailLexique);
		ObservableList<LemmeMemorise> obsListeOccurrences = FXCollections.observableArrayList(listeLemmesMemorises);

		((TableColumn) tableLexique.getColumns().get(0)).setCellFactory(column -> {
			return new TableCell<LemmeMemorise, Lemme>() {
				@Override
				protected void updateItem(Lemme item, boolean empty) {
					super.updateItem(item, empty);
					setAlignment(Pos.CENTER);
					if (empty) {
						setGraphic(null);
					} else {
						Rectangle rectangle = new Rectangle(0, 0, 14, 14);
						setGraphic(rectangle);
						rectangle.getStyleClass().add(item.lireInitiateur().lireNomClasse());
						rectangle.getStyleClass().add("origine");
					}
				}
			};
		});
		((TableColumn) tableLexique.getColumns().get(0))
				.setCellValueFactory(new PropertyValueFactory<LemmeMemorise, Lemme>("lemme"));
		((TableColumn) tableLexique.getColumns().get(1))
				.setCellValueFactory(new PropertyValueFactory<LemmeMemorise, Lemme>("lemme"));
		((TableColumn) tableLexique.getColumns().get(2))
				.setCellValueFactory(new PropertyValueFactory<LemmeMemorise, Individu>("emetteur"));
		((TableColumn) tableLexique.getColumns().get(3))
				.setCellValueFactory(new PropertyValueFactory<LemmeMemorise, Individu>("recepteur"));
		((TableColumn) tableLexique.getColumns().get(4))
				.setCellValueFactory(new PropertyValueFactory<LemmeMemorise, Date>("dateMemorisation"));

		tableLexique.setItems(obsListeOccurrences);

		if (comparateurTableLexique == null) {
			comparateurTableLexique = ((TableColumn) tableLexique.getColumns().get(4)).getComparator().reversed();
		}
		((TableColumn) tableLexique.getColumns().get(4)).setComparator(comparateurTableLexique);
		tableLexique.getSortOrder().add(((TableColumn) tableLexique.getColumns().get(4)));
	}

	/**
	 * Remplie la TableView tableOccurrences contenant les occurrences de lemme relatives a la portee actuelle 
	 * selon la date, le type d'evenement et l'issue d'evenement selectionnes par l'utilisateur
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void remplirTableOccurrences() {
		ArrayList<OccurrenceLemme> listeLemmesMemorises = portee.obtenirListeOccurrences(dateDetailOccurrences,	typeEvenementOccurrences, issueEvenementOccurrences);
		ObservableList<OccurrenceLemme> obsListeOccurrences = FXCollections.observableArrayList(listeLemmesMemorises);

		((TableColumn) tableOccurrences.getColumns().get(0)).setCellFactory(column -> {
			return new TableCell<OccurrenceLemme, Lemme>() {
				@Override
				protected void updateItem(Lemme item, boolean empty) {
					super.updateItem(item, empty);
					setAlignment(Pos.CENTER);
					if (empty) {
						setGraphic(null);
					}
					else {
						Rectangle rectangle = new Rectangle(0, 0, 14, 14);
						setGraphic(rectangle);
						rectangle.getStyleClass().add(item.lireInitiateur().lireNomClasse());
						rectangle.getStyleClass().add("origine");
					}
				}
			};
		});

		((TableColumn) tableOccurrences.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<OccurrenceLemme, Lemme>("lemme"));
		((TableColumn) tableOccurrences.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<OccurrenceLemme, Lemme>("lemme"));
		((TableColumn) tableOccurrences.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<OccurrenceLemme, Individu>("individu"));
		((TableColumn) tableOccurrences.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<OccurrenceLemme, TypeEvenement>("typeEvenement"));
		((TableColumn) tableOccurrences.getColumns().get(4)).setCellValueFactory(new PropertyValueFactory<OccurrenceLemme, IssueEvenement>("issueEvenement"));
		((TableColumn) tableOccurrences.getColumns().get(5)).setCellValueFactory(new PropertyValueFactory<OccurrenceLemme, Date>("date"));
		((TableColumn) tableOccurrences.getColumns().get(6)).setCellValueFactory(new PropertyValueFactory<OccurrenceLemme, Integer>("ID"));

		tableOccurrences.setItems(obsListeOccurrences);

		if (comparateurTableOccurrences == null) {
			comparateurTableOccurrences = ((TableColumn) tableOccurrences.getColumns().get(5)).getComparator().reversed();
		}
		((TableColumn) tableOccurrences.getColumns().get(5)).setComparator(comparateurTableOccurrences);
		tableOccurrences.getSortOrder().add(((TableColumn) tableOccurrences.getColumns().get(5)));
	}
}