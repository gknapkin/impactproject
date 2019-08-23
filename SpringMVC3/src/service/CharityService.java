package service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import entity.Category;
import entity.Charity;
import entity.User;

import com.SpringMVC2.DAOI.*;

public class CharityService implements CharityDAOI {

	//Persists new charity into database
	@Override
	public boolean newCharity(Charity charity) {
		boolean result = false;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(charity);
			em.getTransaction().commit();
			result = true;
		} catch (PersistenceException e) {
			System.out.println(e.getMessage());
		} finally {
			em.close();
			emf.close();
		}
		return result;
	}

//	//Finds charity if exists by looking at argument's charity id - sort of silly - should have just passed in ID
//	@Override
//	public Charity findCharity(Charity inputCharity) {
//		Charity foundCharity = null;
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
//		EntityManager em = emf.createEntityManager();
//		try {
//			foundCharity = em.find(Charity.class, inputCharity.getCharityId());
//		} catch (PersistenceException e) {
//			System.out.println(e.getMessage());
//		} finally {
//			em.close();
//			emf.close();
//		}
//		return foundCharity;
//	}

	//Finds charity by name 
	@Override
	public Charity findCharityByName(String charityName) {
		Charity foundCharity = null;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();

		try {
			Query query = em.createNamedQuery("CharityByName");
			query.setParameter("cName", charityName);

			List<Charity> tempList = query.getResultList();
			foundCharity = tempList.get(0);

		} catch (PersistenceException e) {
			System.out.println(e.getMessage());
		} finally {
			em.close();
			emf.close();
		}

		return foundCharity;
	}

	//Finds charity by charity ID
	@SuppressWarnings("null")
	@Override
	public Charity findCharityById(int charityId) {
		Charity foundCharity = null;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		Long charLong = (long) charityId;

		try {
			foundCharity = em.find(Charity.class, charLong);

		} catch (PersistenceException e) {
			System.out.println(e.getMessage());
		} finally {
			em.close();
			emf.close();
		}

		return foundCharity;
	}

	// Find charity + associated review - ended up not using this service at all
	@Override
	public HashMap<Charity, Double> charityRating() {

		HashMap<Charity, Double> charityRatingMap = new HashMap<Charity, Double>();

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();

		try {
			Query query = em.createNamedQuery("Charity and rating");
			List<Object[]> tempList = query.getResultList();

			for (Object[] e : tempList) {
				Charity one = (Charity) e[0];
				Double two = (double) e[1];
				charityRatingMap.put(one, two);

			}

		} catch (PersistenceException e) {
			System.out.println(e.getMessage());

		} finally {
			em.close();
			emf.close();
		}

		return charityRatingMap;

	}

	//This allowed for getting a rating for each charity - was tough because of the nature of ...
	//result list returned from executing query.
	@Override
	public Double rateByCharity(Charity charity) {
		Double returnRating = 0.0;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();

		try {
			Query query = em.createNamedQuery("rateByCharity");
			query.setParameter("cId", charity.getCharityId());

			List<Object[]> tempList = query.getResultList();

			for (Object[] e : tempList) {
				returnRating = (double) e[1];
			}

		} catch (PersistenceException e) {
			System.out.println(e.getMessage());

		} finally {
			em.close();
			emf.close();
		}

		return returnRating;

	}

	//Returns all charities regardless of category. Used for getting random charity
	@Override
	public Set<Charity> findAllCharities() {
		Set<Charity> charitySet = null;

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();

		try {
			Query query = em.createNamedQuery("allCharities");
			
			List<Charity> tempList = query.getResultList();
			
			charitySet = new HashSet(tempList);
			

		} catch (PersistenceException e) {
			System.out.println(e.getMessage());

		} finally {
			em.close();
			emf.close();

		}

		return charitySet;
	}
	
	//Edits charity - only thing that must remain the same is the charity ID - everything else is set to match argument object
	@Override
	public String editCharity(Charity charity) {
		String message = null;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		try {
		em.getTransaction().begin();
		Charity foundCharity = em.find(Charity.class, charity.getCharityId());
		foundCharity.setCategory(charity.getCategory());
		foundCharity.setCharityName(charity.getCharityName());
		foundCharity.setCharityImpact(charity.getCharityImpact());
		foundCharity.setCharityWeb(charity.getCharityWeb());
		em.getTransaction().commit();
		message = "success";
		} catch (PersistenceException e) {
			System.out.println(e.getMessage());

		} finally {
			em.close();
			emf.close();

		}
		
		return message;
	}

}
