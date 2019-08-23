package com.SpringMVC2.DAOI;

import java.util.HashMap;
import java.util.Set;

import entity.Charity;

public interface CharityDAOI {


		boolean newCharity(Charity charity);

//		Charity findCharity(Charity inputCharity);

		HashMap<Charity, Double> charityRating();

		Charity findCharityByName(String charityName);

		Double rateByCharity(Charity charity);

		Charity findCharityById(int charityId);
		
		Set<Charity> findAllCharities();

		String editCharity(Charity charity);

	}
