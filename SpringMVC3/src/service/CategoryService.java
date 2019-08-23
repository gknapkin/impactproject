package service;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import entity.Category;
import entity.Charity;

import com.SpringMVC2.DAOI.*;

public class CategoryService implements CategoryDAOI {

	//Creates a category - currently only written for mainrunner - unaccessed in controller
	@Override
	public boolean newCategory(Category category) {
		boolean result = false;		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();	
		try {
			em.getTransaction().begin();
			em.persist(category);
			em.getTransaction().commit();
			result = true;			
		}catch (PersistenceException e){
			System.out.println(e.getMessage());
		}finally {
			em.close();
			emf.close();			
		}	
		return result;	
	}
	
	//return all categories - query allows the list to be sorted in a-z order.
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Category> getAllCategories(){
		ArrayList <Category> catList = null;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			Query query = em.createNamedQuery("Find all Categories");
			List<Category> tempList = query.getResultList();
			if (tempList != null) {
			catList = new ArrayList<Category>(tempList);
			}
		}catch (PersistenceException e) {
			System.out.println(e.getMessage());
		}finally {
			em.close();
			emf.close();
		}
		return catList;
	}
	
	//find all charities by category
	@Override
	public Set<Charity> getCharitiesByCat(int catId){
		Set <Charity> charityList = null;
		HashSet<Charity> catSet = null;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			Query query = em.createNamedQuery("AllCharitiesInCat");
			query.setParameter("cId", catId);
			List<Charity> tempList = query.getResultList();
			if (tempList != null) {
			catSet = new HashSet<Charity>(tempList);
			}
		}catch (PersistenceException e) {
			System.out.println(e.getMessage());
		}finally {
			em.close();
			emf.close();
		}
		return catSet;
	}
	
	
	//find category...used for creating charities
	@Override
	public Category findCategoryById(int id) {
		Category foundCategory = null;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			foundCategory = em.find(Category.class, id);
		}catch (PersistenceException e) {
			System.out.println(e.getMessage());
		}finally {
			em.close();
			emf.close();
		}
			
		return foundCategory;		
	}
	
	//Looking for a category by name
	@Override
	public Category findCategoryByName(String catName) {
		Category foundCategory = null;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			Query query = em.createNamedQuery("CatByName");
			query.setParameter("cName", catName);
			foundCategory = (Category) query.getSingleResult();
		}catch (PersistenceException e) {
			System.out.println(e.getMessage());
		}finally {
			em.close();
			emf.close();
		}
			
		return foundCategory;		
	}
	
	
	//edit existing category - unused in main controller
	@Override
	public boolean editCategory(Category category) {
		boolean result = false;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			em.getTransaction().begin();
			Category foundCategory = em.find(category.getClass(), category.getCatId());
			foundCategory.setCatName(category.getCatName());
			em.persist(foundCategory);
			em.getTransaction().commit();
			
		} catch (PersistenceException e) {
			System.out.println(e.getMessage());
		} finally {
			em.close();
			emf.close();
		}	
		return result;
	}
	

}
