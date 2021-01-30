package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.ModelException;

public interface DAO<T> {

	public void insert(T objet) throws ModelException;

	public void delete(Integer nb) throws ModelException;

	public void update(T objet) throws ModelException;
	
	public T selectById(Integer number) throws ModelException;

}
