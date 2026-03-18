package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;
	
	

	public Village(String nom,int nbVillageoisMaximum,int nbEtals) {
		super();
		this.nom = nom;
		this.villageois = new Gaulois[nbVillageoisMaximum];
		this.marche = new Marche(nbEtals);
	}

	private static class Marche{
		private Etal[] etals;

		private  Marche(int nbEtals) {
			etals=new Etal[nbEtals];
			for(int i=0;i<nbEtals;i++) {
				etals[i]=new Etal();
			}
		}
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}
		private int trouverEtatLibre() {
			for(int i=0;i<etals.length;i++) {
				if(!etals[i].isEtalOccupe()) {
					return i;
				}
			}
			return -1;
		}
		private Etal[] trouverEtals(String Produit) {
			int produitRech=0;
			for(int i=0;i<etals.length;i++) {
				if(etals[i].contientProduit(Produit)) {
					produitRech++;
				}
			}
			Etal[] tabEtal= new Etal[produitRech];
			int j=0;
			for(int i=0;i<etals.length;i++) {
				if(etals[i].contientProduit(Produit)) {
					tabEtal[j]=etals[i];
					j++;
				}
			}
			
			return tabEtal;
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			for(int i=0;i<etals.length;i++) {
				if(etals[i].getVendeur()==gaulois) {
					return etals[i];
				}
			}
			return null;	
		}
		
		private String afficherMarche() {
			StringBuilder chaine=new StringBuilder();
			int nbEtalVide=0;
			for(int i=0;i<etals.length;i++) {
				if(etals[i].isEtalOccupe()) {
					chaine.append(etals[i].afficherEtal());
				}
				nbEtalVide++;
			}
			chaine.append("Il reste " + nbEtalVide + " étals non utilisés dans le marché.\n");
			return chaine.toString();
		}
		
	}
	

	

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() throws VillageSansChefException{
		if(chef==null) {
			throw new VillageSansChefException();
		}
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	public String installerVendeur(Gaulois vendeur, String produit,int nbProduit) {
		StringBuilder chaine= new StringBuilder();
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit  +" "+  produit + "\n");
		int etal=marche.trouverEtatLibre();
		if(etal!=-1) {
			marche.utiliserEtal(etal, vendeur, produit, nbProduit);
			chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n° " + (etal+1) + "\n");
		}
		else {
			chaine.append("plus d'étal libre");
		}
		
		return chaine.toString();
		
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		Etal[] etals = marche.trouverEtals(produit);
		if(etals.length==0) {
			chaine.append("Il n'y a pas de vendeur qui propose des "+ produit +" au marché \n");
		}
		else if(etals.length==1) {
			chaine.append("seul le vendeur "+ etals[0].getVendeur().getNom() + " propose des "+ produit + "au marché \n");
		}
		else {
			chaine.append("Les vendeurs qui proposent des " + produit + " sont :");
			for(int i=0;i<etals.length;i++) {
				chaine.append("-"+ etals[i].getVendeur().getNom());
			}
		}
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}
	
	public String partirVendeur(Gaulois vendeur) {
		Etal etals= marche.trouverVendeur(vendeur);
		if(etals!=null) {
			return etals.libererEtal();
		}
		return "";
	}
	
	public String afficherMarche() {
		StringBuilder chaine=new StringBuilder();
		chaine.append("le marché du village "+ nom +" possède plusieurs étals : "+ marche.afficherMarche() +"\n");
		return chaine.toString();
	}
}