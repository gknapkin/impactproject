package entity;

import java.util.Set;
import entity.Charity;

import javax.persistence.*;

@Entity
@Table
@NamedQueries ({
@NamedQuery (query = "SELECT c FROM Category c ORDER BY c.catName ASC", name = "Find all Categories"),
@NamedQuery (query = "SELECT c FROM Category c WHERE c.catName = :cName", name = "CatByName"),
@NamedQuery (query = "SELECT c.charity FROM Category c WHERE c.catId = :cId", name = "AllCharitiesInCat")
})
public class Category {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int catId;
	
	@Basic
	@Column(unique=true)
	private String catName;
	
	@Basic
	private String catDesc;
	
	
	@OneToMany(mappedBy="category")
    private Set<Charity> charity;

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getCatDesc() {
		return catDesc;
	}

	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}

	@Override
	public String toString() {
		return "Category [catId=" + catId + ", catName=" + catName + ", catDesc=" + catDesc + "]";
	}

	public Category(String catName, String catDesc) {
		super();
		this.catName = catName;
		this.catDesc = catDesc;
	}

	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Set<Charity> getCharity() {
		return charity;
	}

	public void setCharity(Set<Charity> charity) {
		this.charity = charity;
	}
	
	
	
	
	
	
}
