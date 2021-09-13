package fr.cmci.biblio.security.enums;

public enum StatutU {
	Valide (1, "Valide"), Expire (2, "Expire"), Suspendu (3, "Suspendu") ;
	
	private int id ;
	private String libelle ;
	
	private StatutU (int id, String libelle) {
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
		if(Valide.getId() == id) return Valide.getLibelle() ;
		else if(Expire.getId() == id) return Expire.getLibelle() ; 
		else if(Suspendu.getId() == id) return Suspendu.getLibelle() ; 
		return null ;
	}
}
