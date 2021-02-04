package fr.eni.javaee.encheres.dal;

import java.util.List;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;

public interface UtilisateurDAO extends DAO<Utilisateur>{
	
	public List<String> retrieveUsersEmails() throws ModelException;
	
	public List<String> retrieveUsersAlias() throws ModelException;

	public String retrieveUserPassword(String identifiant) throws ModelException;

	public Utilisateur retrieveUserInfo(String identifiant) throws ModelException;

	public Integer retrieveUserCredit(Integer noUtilisateur) throws ModelException;

	public void updateCredit(Enchere enchere, Enchere precedenteEnchere) throws ModelException;

}
