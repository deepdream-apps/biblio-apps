package fr.cmci.biblio.security.enums;

public enum RoleU {
	Usager (1, "Usager"), Administrateur (2, "Administrateur"), Operateur (3, "Operateur"),  Editeur (4, "Editeur") ;
	
	private int id ;
	private String libelle ;
	
	private RoleU (int id, String libelle) {
		this.id = id ;
		this.libelle = libelle ;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public static String getLibelle (int id) {
		if(Usager.getId() == id) return Usager.getLibelle() ;
		else if(Editeur.getId() == id) return Editeur.getLibelle() ; 
		else if(Operateur.getId() == id) return Operateur.getLibelle() ; 
		else if(Administrateur.getId() == id) return Administrateur.getLibelle() ; 
		return null ;
	}
}
