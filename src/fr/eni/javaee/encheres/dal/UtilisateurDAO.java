package fr.eni.javaee.encheres.dal;

import java.util.List;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;

public interface UtilisateurDAO extends DAO<Utilisateur>{
	
	public List<String> selectAllEmails() throws ModelException;
	
	public List<String> selectAllPseudo() throws ModelException;

	public String selectPassword(String identifiant) throws ModelException;

	public Utilisateur retrieveUserInfo(String identifiant) throws ModelException;

	public Integer selectCredit(Integer noUtilisateur) throws ModelException;

	public void updateCredit(Enchere enchere) throws ModelException;

}
