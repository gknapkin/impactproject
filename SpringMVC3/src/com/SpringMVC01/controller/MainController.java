package com.SpringMVC01.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import service.CategoryService;
import service.CharityService;
import service.ReviewService;
import service.UserService;
import entity.Category;
import entity.Charity;
import entity.Review;
import entity.User;

@SessionAttributes(value = { "userCurrent"})
@Controller
public class MainController {

	//Index/homepage required reviewservice to populate with most recent reviews at bottom of page
	@RequestMapping(value = { "/", "/index" })
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		
		ReviewService rS = new ReviewService();
		
		ArrayList<Review> lastFiveReviews = (ArrayList<Review>) rS.lastFiveReviews();
		
		mav.addObject("lastReviews", lastFiveReviews);
		return mav; // name of view
	}

	//About page is pretty static - just a quick blurb and shoutouts with some links to outside the site
	@RequestMapping("/about") // This is URI URL from the page
	public ModelAndView getAbout() {
		return new ModelAndView("about"); // This is the file name of the view
	}

	//Gets view for user registration
	@RequestMapping("/registration") // This is URI URL from the page
	public ModelAndView getReg() {
		return new ModelAndView("registration"); // This is the file name of the view
	}

	//Gets view for user signin
	@RequestMapping(value = { "/signin" })
	public ModelAndView getSignIn() {
		return new ModelAndView("signin");
	}

	//Handles the registration form for users - directs to profile view
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public ModelAndView getMyForm(@RequestParam("usernameInput") String name,
			@RequestParam("passwordInput") String pass) {
		UserService uS = new UserService();
		ModelAndView mav = new ModelAndView("formmessage");	
		if (uS.credCheck(name, pass)) {
			User user = uS.findUser(name, pass);
			mav.addObject("userCurrent", user);
			ArrayList<Review> reviewList = uS.reviewsByUserId(user.getUserId());
			mav.addObject("reviewsList", reviewList);
		} else {
			mav.setViewName("signin");
		}
		return mav;
	}

	//Directs loggedin user to profile page, but for those not loggedin it directs to sign in
	@RequestMapping(value = "/myprofile")
	public ModelAndView getMyProfile(HttpServletRequest request) {		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("userCurrent");
		ModelAndView mav = null;
		
		if (user!=null) {
			mav = new ModelAndView("formmessage");		
		} else {
			return getSignIn();
		}
		UserService uS = new UserService();	
		ArrayList<Review> reviewList = uS.reviewsByUserId(user.getUserId());
		System.out.println(reviewList.size());
		mav.addObject("reviewsList", reviewList);	
		return mav;

	}
	
	//Directs to page for editing user profile
	@RequestMapping(value = "/profileedit")
	public ModelAndView editProfile(@SessionAttribute(name="userCurrent") User user) {
		return new ModelAndView("profileedit");
	}
	
	
	//Directs to page for editing charity profile
	@RequestMapping(value = "/charity/editOrg")
	public ModelAndView getEditOrgPage(@RequestParam("charity") String charityId) {
		ModelAndView mav = new ModelAndView("charityedit");
		CharityService cS = new CharityService();
		int charId = Integer.parseInt(charityId);
		Charity charity = cS.findCharityById(charId);
		mav.addObject("charity", charity);
		
		CategoryService catS = new CategoryService();
		ArrayList<Category> catSet = catS.getAllCategories();

		if (!catSet.isEmpty()) {
			mav.addObject("listOfCategories", catSet);
		}
		return mav;
	}
	
	//Handles form for commiting an edit on charity
	@RequestMapping(value = "/charity/editCharity", method = RequestMethod.POST)
	public ModelAndView charityEdit(@RequestParam("charityName") String charityName,
			@RequestParam("charityId") String charityId,
			@RequestParam("category") String category,
			@RequestParam("charityWeb") String charityWeb,
			@RequestParam("charityImpact") String charityImpact) {
		ModelAndView mav = new ModelAndView();
		CharityService cS = new CharityService();
		Charity charity = cS.findCharityById(Integer.parseInt(charityId));
		CategoryService catServ = new CategoryService();
		Category chosenCategory = catServ.findCategoryByName(category);
		charity.setCharityName(charityName);
		charity.setCategory(chosenCategory);
		charity.setCharityImpact(charityImpact);
		charity.setCharityWeb(charityWeb);
		String message = cS.editCharity(charity);
		mav.addObject("message", message);
		mav = getCharityPage(charity.getCharityName());
		
		return mav;
		
	}
	

	
	
	
	
	
	//Handles form for editing user profile, sets session attribute to updated user 
	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	public ModelAndView editUser(HttpServletRequest request,
			@RequestParam("userName") String name,
			@RequestParam("userEmail") String email,
			@RequestParam("userPassword") String pass) {
		UserService uS = new UserService();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("userCurrent");
		user.setUserName(name);
		user.setUserEmail(email);
		user.setUserPassword(pass);
		uS.editUser(user);
		
	    session.setAttribute("userCurrent", user);

		return getMyProfile(request);
		
	}
	
	
	//Handles form for registering a new user, sets session attribute to user object
	@RequestMapping(value = "/regUser", method = RequestMethod.POST)
	public ModelAndView regUser(HttpServletRequest request, @ModelAttribute("userKey") User user) {
		UserService uS = new UserService();
		ModelAndView mav = new ModelAndView("registration");
		if (uS.newUser(user)) {		
			request.getSession().setAttribute("userCurrent", user);
			mav = getMyProfile(request);	
		} else {
			mav.addObject("viewMessage", "Failed to register! Try Again!");		
		}
		return mav;
	}
	
	
	//Handles form for leaving a review for a charity - redirects back to charity page afterwards
	@RequestMapping(value="/charity/leaveReview", method = RequestMethod.POST)
	public ModelAndView leaveReview(@SessionAttribute("userCurrent") User user,
			@RequestParam("charity") String charityName,
			@RequestParam("comment") String comment,
			@RequestParam("rating") int rating) {
		
		ReviewService rS = new ReviewService();
		CharityService cS = new CharityService();
		Charity charity = cS.findCharityByName(charityName);
		
		Review review = new Review(user,charity,comment,rating);
		rS.newReview(review);
		ModelAndView mav = new ModelAndView();
		mav.addObject("review", review);
		mav.addObject("charityCurrent", charity);
		mav = getCharityPage(charity.getCharityName());
//		
//		ArrayList <Review> reviewsList = new ArrayList <Review>(rS.reviewsByCharity(charity.getCharityId()));
//		DecimalFormat df = new DecimalFormat("#.##");
//		String rating2 = df.format(cS.rateByCharity(charity));
//		mav.addObject("charityRating", rating2);
//		mav.addObject("charityReviews", reviewsList);

		return mav;
	}
	

	//Directs user to random charity - just picks first charity from a set
	@RequestMapping(value = "/surprise")
	public ModelAndView surprise() {		
		CharityService cS = new CharityService();	
		Set <Charity> charitySet = cS.findAllCharities();
		System.out.println(charitySet.iterator());
		if (!charitySet.iterator().hasNext()) {
			return index();
		}else {
		return getCharityPage(charitySet.iterator().next().getCharityName());
		}
	}
	
	//Delete review then return back to user profile
	@RequestMapping(value = "/deleteReview", method = RequestMethod.POST)
	public ModelAndView deleteReview(HttpServletRequest request, @SessionAttribute("userCurrent") User user,
			@RequestParam("reviewId") int reviewId) {
		ReviewService rS = new ReviewService();	
		String message = rS.deleteByUserNameAndId(user.getUserName(), reviewId);
		ModelAndView mav = getMyProfile(request);
		mav.addObject("message", message);	
		return mav;
	}
	
	//Handles charity registration - requires a signed in user - then directs to charity profile page
	@RequestMapping(value = "/regOrg", method = RequestMethod.POST)
	public ModelAndView regOrg(@RequestParam("charityName") String charityName,
			@RequestParam("category") String category,
			@RequestParam("charityWeb") String charityWeb,
			@RequestParam("charityImpact") String charityImpact,
			@SessionAttribute("userCurrent") User user) {
		CharityService cS = new CharityService();	
		Charity charity = new Charity(charityName, category,  charityImpact, charityWeb, user);
		cS.newCharity(charity);
		return getCharityPage(charity.getCharityName());
		
	}

	//Directs to a view with all the charity categories in alphabetical order (done in services)
	@RequestMapping("/allcat")
	public ModelAndView getAllCategories() {
		CategoryService catS = new CategoryService();
		ArrayList<Category> catSet = catS.getAllCategories();
		ModelAndView mav = new ModelAndView("allcat");
		if (!catSet.isEmpty()) {
			mav.addObject("listOfCategories", catSet);
		}
		return mav;
	}

	//Logout function - basically ends the session, removing the session attribute usercurrent. 
	//This directs to a logout view, which just redirects to index in order to refresh nav bar.
	@RequestMapping("/logout")
	public ModelAndView userLogOut(SessionStatus status) {
		status.setComplete();

		ModelAndView mav = new ModelAndView();
//		mav.addObject("userCurrent", "x");
//		
//		mav.clear();
		mav.setViewName("logout");
		mav.addObject("userCurrent", null);
		return mav;
	}

	//Shows a view for all charities in a certain category in no particular order (set)
	@RequestMapping(value = "/catspec/{catId}", method = RequestMethod.GET)
	public ModelAndView getCatSpecPage(@PathVariable int catId) {
		CategoryService catS = new CategoryService();
		Set<Charity> charitySet = catS.getCharitiesByCat(catId);
		ModelAndView mav = new ModelAndView("catspec");
		if (!charitySet.isEmpty()) {
			mav.addObject("listOfCharities", charitySet);
		}
		return mav;
	}
	
	//Directs to the charity's page - complete with reviews, rating etc, shows charity admin
	@RequestMapping(value= "/charity/{charityName}", method = RequestMethod.GET)
	public ModelAndView getCharityPage(
			@PathVariable String charityName) {
		ModelAndView mav = new ModelAndView("charity");
		CharityService cS = new CharityService();
		Charity charity = cS.findCharityByName(charityName);
		ReviewService rS = new ReviewService();
		ArrayList <Review> reviewsList = new ArrayList <Review>(rS.reviewsByCharity(charity.getCharityId()));	
		DecimalFormat df = new DecimalFormat("#.##");	
		String rating = df.format(cS.rateByCharity(charity));	
		mav.addObject("charityCurrent", charity);
		mav.addObject("charAdmin", charity.getUser().getUserId());
		mav.addObject("charityRating", rating);
		mav.addObject("charityReviews", reviewsList);
		return mav;
	}

	//Directs to charity landing page - a place for users to create charity profile etc 
	@RequestMapping(value="/organization")
	public ModelAndView getOrgLanding() {
		ModelAndView mav = new ModelAndView("organization");		
		CategoryService catS = new CategoryService();
		ArrayList<Category> catSet = catS.getAllCategories();
		if (!catSet.isEmpty()) {
			mav.addObject("listOfCategories", catSet);
		}	
		return mav;
	}
	
}
