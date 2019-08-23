package com.SpringMVC2.DAOI;

import java.util.List;

import entity.Review;

public interface ReviewDAOI {

	

		boolean newReview(Review review);



		List<Review> reviewsByCharity(long charityId);
		
		List<Review> lastFiveReviews();



		String deleteByUserNameAndId(String userName, int reviewId);

	
}