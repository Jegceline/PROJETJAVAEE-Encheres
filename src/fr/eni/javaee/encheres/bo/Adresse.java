package fr.eni.javaee.encheres.bo;

public class Adresse {
	
	private String rue;
	private Integer codePostal;
	private String ville;
	
	
	public Adresse() {
	}
	public Adresse(String rue, Integer codePostal, String ville) {
		super();
		this.rue = rue.trim();
		this.codePostal = codePostal;
		this.ville = ville.trim();
	}
	
	
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue.trim();
	}
	public Integer getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(Integer codePostal) {
		this.codePostal = codePostal;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville.trim();
	}
	@Override
	public String toString() {
		return "Adresse [rue=" + rue + ", codePostal=" + codePostal + ", ville=" + ville + "]";
	}
	
	

}
