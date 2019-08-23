package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import entity.Review;
import entity.User;
import com.SpringMVC2.DAOI.*;

public class UserService implements UserDAOI {
	
	@Override //Service Method that adds a new user to the database, takes input of user and returns boolean to verify if transaction succeeded
	public boolean newUser(User user){
		boolean result = false;	
		if (user.getUserName().isEmpty()||user.getUserName().isEmpty()||user.getUserPassword().isEmpty()||user.getUserName().contains(" ")) {
			return result;
		}
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();	
		Date now = new Date();
		user.setJoinDate(now);
		try {
			em.getTransaction().begin();
			em.persist(user);
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
	
	
	// Only assumption is that user id is constant
	@Override
	public boolean editUser(User user) {
		boolean result = false;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			em.getTransaction().begin();
			User tempUser = em.find(user.getClass(), user.getUserId());
			tempUser.setUserEmail(user.getUserEmail());
			tempUser.setUserName(user.getUserName());
			tempUser.setUserPassword(user.getUserPassword());
			em.getTransaction().commit();
			result = true;
		}catch (PersistenceException e) {
			System.out.println(e.getMessage());
		}finally {
			em.close();
			emf.close();
		}
		
		
		
		return result;
		
	}


	//Only checks for credentials
	@Override
	public boolean credCheck(String username, String password) {
		boolean result = false;
		List <User> resultList = null;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			if (username.contains("@")&& username.contains(".")) {
				Query query1 = em.createNamedQuery("Verify1");
				query1.setParameter("uemail", username);
				query1.setParameter("upass", password);
				resultList = query1.getResultList();
			} else {
				Query query = em.createNamedQuery("Verify");
				query.setParameter("uname", username);
				query.setParameter("upass", password);
				resultList = query.getResultList();
			}
			for (User u : resultList) {
				System.out.println(u.toString());
			}
			
			if (resultList.isEmpty()) {
				result = false;
			}else {
				result = true;
			}
		}catch (PersistenceException e) {
			System.out.println(e.getMessage());
		}finally {
			em.close();
			emf.close();
		}	
		return result;	
	}
	
	//Same thing as credentials - but returns user (can do for both email and username)
	public User findUser(String userInput, String password) {
		User user = null;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			if (userInput.contains("@")&& userInput.contains(".")) {
				Query query1 = em.createNamedQuery("Verify1");
				query1.setParameter("uemail", userInput);
				query1.setParameter("upass", password);
				user = (User) query1.getSingleResult();
			} else {
				Query query = em.createNamedQuery("Verify");
				query.setParameter("uname", userInput);
				query.setParameter("upass", password);
				user = (User) query.getSingleResult();
			}
		}catch (PersistenceException e) {
			System.out.println(e.getMessage());
		}finally {
			em.close();
			emf.close();
		}	
		return user;	
	}
	
	//Returns all reviews by timestamp associated with this user
	public ArrayList<Review> reviewsByUserId(Long userId){
		ArrayList<Review> reviewList = null;
		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			Query query = em.createNamedQuery("reviewsById");
			query.setParameter("uId", userId);
			List<Review> tempList = query.getResultList();
			reviewList = new ArrayList(tempList);
			
			
		}catch(PersistenceException e) {
			System.out.println(e.getMessage());
		}finally {
			em.close();
			emf.close();
		}
			
		return reviewList;
	}
	
	

}
