package service;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import entity.Charity;
import entity.Review;
import com.SpringMVC2.DAOI.*;

public class ReviewService implements ReviewDAOI {
	
	//Persists new review into the database
	@Override
	public boolean newReview(Review review) {
		boolean result = false;		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();	
		try {
			em.getTransaction().begin();
			em.persist(review);
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
	
	//Returns all reviews associated with a charity - used on charity profile page
	@Override
	public List<Review> reviewsByCharity(long charityId){
		ArrayList <Review> reviewList = null;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();	
		
		try {
			Query query = em.createNamedQuery("reviewsByCharity");
			query.setParameter("cId", charityId);
			
		List<Review>tempList = query.getResultList();
		
		reviewList = new ArrayList<Review>(tempList);
			
			
		}catch(PersistenceException e) {
			System.out.println(e.getMessage());
		}finally {
			em.close();
			emf.close();
		}
		
		return reviewList;
		
	}

	//Returns last (5) reviews persisted into the database - used for main index view.
	@Override
	public ArrayList<Review> lastFiveReviews() {
		ArrayList <Review> reviewList = null;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			Query query = em.createNamedQuery("lastFiveReviews");
			query.setMaxResults(5);
			List <Review> tempList = query.getResultList();
			reviewList = new ArrayList<Review>(tempList);
			
		}catch (PersistenceException e) {
			
		}finally {
			em.close();
			emf.close();
		}

		return reviewList;
	}
	
	//Delete select review - needs both review ID and the user associated with review (for messaging) {kind of unnecessary}
	@Override
	public String deleteByUserNameAndId(String userName, int reviewId) {
		String message = null;
		Review review = new Review();
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("casestudy");
		EntityManager em = emf.createEntityManager();
		
		try {
			  review = em.find(review.getClass(), reviewId);

			  em.getTransaction().begin();
			  em.remove(review);
			  em.getTransaction().commit();
			
			
			
			
			message = "Review deleted for "+userName;
			
			
		}catch (PersistenceException e) {
			message = e.getMessage() + " Failed";
		}finally {
			em.close();
			emf.close();
		}
		
		
		
		return message;
	}

}
