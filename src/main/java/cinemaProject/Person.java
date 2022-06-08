package cinemaProject;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "cinema_db.person")
public class Person {
	@Id
	@Column(name = "pid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int PersonID;
	
	@Column(name = "p_yearsold")
	private int YearsOld;
	
	@Column(name = "p_firstname")
	private String FirstName;
	
	@Column(name = "p_lastname")
	private String LastName;
	
	@Column(name = "p_status")
	private String Status;
	
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "cinema_db.person_film", 
        joinColumns = { @JoinColumn(name = "pid") }, 
        inverseJoinColumns = { @JoinColumn(name = "fid") }
    )
	private List<Film> FilmList = new ArrayList<Film>();
	
	public void addFilm(Film film) {
		FilmList.add(film);
	}
	public void removeFilm(Film film) {
		FilmList.remove(film);
	}
	public List<Film> GetFilmList() {
		return FilmList;
	}
	
	public int GetPersonID() {
		return PersonID;
	}
	public boolean SetPersonID(int personID) {
		this.PersonID = personID;
		return true;
	}
	
	
	public int GetYearsOld() {
		return YearsOld;
	}
	public boolean SetYearsOld(int yearsOld) {
		this.YearsOld = yearsOld;
		return true;
	}
	
	
	public String GetFirstName() {
		return FirstName;
	}
	public boolean SetFirstName(String firstName) {
		this.FirstName = firstName;
		return true;
	}
	
	
	public String GetLastName() {
		return LastName;
	}
	public boolean SetLastName(String lastName) {
		this.LastName = lastName;
		return true;
	}
	
	
	public String GetStatus() {
		return Status;
	}
	public boolean SetStatus(String status) {
		this.Status = status;
		return true;
	}
	
	public Person() {
		
	}
}
