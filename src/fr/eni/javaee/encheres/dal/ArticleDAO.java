package fr.eni.javaee.encheres.dal;

import java.util.List;

import fr.eni.javaee.encheres.ModelException;
import fr.eni.javaee.encheres.bo.Article;

public interface ArticleDAO extends DAO<Article>{
	
	public List<Article> selectByStatus() throws ModelException;

}
