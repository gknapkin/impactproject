package entity;

import entity.Category;
import service.CategoryService;
import service.CharityService;

import java.text.DecimalFormat;

import javax.persistence.*;

@Entity
@Table
@NamedQueries({
		@NamedQuery(query = "SELECT DISTINCT r.charity, AVG(r.rating) FROM Review r GROUP BY r.charity", name = "Charity and rating"),
		@NamedQuery(query = "SELECT DISTINCT r.charity.charityId, AVG(r.rating) FROM Review r  WHERE r.charity.charityId = :cId GROUP BY r.charity.charityId", name = "rateByCharity"),
		@NamedQuery(query = "SELECT c FROM Charity c WHERE c.charityName = :cName", name = "CharityByName"),
		@NamedQuery(query = "SELECT c FROM Charity c", name = "allCharities")})
public class Charity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long charityId;

	@Basic
	private String charityName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "catId")
	private Category category; // fk-cat

	@Basic
	private String charityImpact;

	@Basic
	private String charityWeb;
	
	@OneToOne
	@JoinColumn(name = "USERID")
	private User refUser;

	public User getUser() {
		return refUser;
	}

	public void setUser(User user) {
		this.refUser = user;
	}

	public String getCharityWeb() {
		return charityWeb;
	}

	public void setCharityWeb(String charityWeb) {
		this.charityWeb = charityWeb;
	}

	public long getCharityId() {
		return charityId;
	}

	public void setCharityId(long charityId) {
		this.charityId = charityId;
	}

	public String getCharityName() {
		return charityName;
	}

	public void setCharityName(String charityName) {
		this.charityName = charityName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getCharityImpact() {
		return charityImpact;
	}

	//JimmyRigged method - done for catspec/{} page to show each charity with its rating
	public String getCharityRating() {
		CharityService cS = new CharityService();
		
		DecimalFormat df = new DecimalFormat("#.##");

		String rating = df.format(cS.rateByCharity(this));

		return rating;

	}

	public void setCharityImpact(String charityImpact) {
		this.charityImpact = charityImpact;
	}

//	public Charity(String charityName, Category category, String charityImpact, String charityWeb) {
//		super();
//		this.charityName = charityName;
//		this.category = category;
//		this.charityImpact = charityImpact;
//		this.charityWeb = charityWeb;
//	}

	public Charity(String charityName, String category, String charityImpact, String charityWeb, User user) {
		super();
		CategoryService cS = new CategoryService();
		this.charityName = charityName;
		this.category = cS.findCategoryByName(category);
		this.charityImpact = charityImpact;
		this.charityWeb = charityWeb;
		this.refUser = user;
	}

	public Charity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Charity [charityId=" + charityId + ", charityName=" + charityName + ", category=" + category
				+ ", charityImpact=" + charityImpact + ", charityWeb=" + charityWeb + ", user=" + refUser + "]";
	}

	

}
