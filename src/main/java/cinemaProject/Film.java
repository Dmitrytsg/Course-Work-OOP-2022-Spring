package cinemaProject;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "cinema_db.film")
public class Film {
	@Id
	@Column(name = "fid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int FilmID;
	
	@Column(name = "f_name")
	private String Name;
	
	@Column(name = "f_genre")
	private String Genre;
	
	@Column(name = "f_releaseyear")
	private int ReleaseYear;
	
	@OneToMany(mappedBy = "film_id", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Session> SessionList = new ArrayList<Session>();
	
	@ManyToMany(mappedBy = "FilmList")
	private List<Person> PersonList = new ArrayList<Person>();
	
	public void addPerson(Person person) {
		PersonList.add(person);
		person.addFilm(this);
	}
	public void removePerson(Person person) {
		PersonList.remove(person);
		person.removeFilm(this);
	}
	
	public List<Person> GetActorList() {
		List<Person> ActorList = new ArrayList<Person>();
		for (Person person: PersonList) {
			if (person.GetStatus().contains("Actor")) { ActorList.add(person); }			
		}
		return ActorList;
	}
	
	public List<Person> GetProducerList() {
		List<Person> ProducerList = new ArrayList<Person>();
		for (Person person: PersonList) {
			if (person.GetStatus().contains("Producer")) { ProducerList.add(person); }			
		}
		return ProducerList;
	}
	
    public void addSession(Session session) {
    	SessionList.add(session);
    	session.SetFilmID(this);
    }
    public void removeSession(Session session) {
    	SessionList.remove(session);
    	session.removeFilm();;
    }
    public List<Session> GetSessionList() {
    	return this.SessionList;
    }
	
	public int GetFilmID() {
		return FilmID;
	}
	public boolean SetFilmID(int filmID) {
		this.FilmID = filmID;
		return true;
	}
	
	public String GetName() {
		return Name;
	}
	public boolean SetName(String name) {
		this.Name = name;
		return true;
	}
	
	public String GetGenre() {
		return Genre;
	}
	public boolean SetGenre(String genre) {
		this.Genre = genre;
		return true;
	}
	
	public int GetReleaseYear() {
		return ReleaseYear;
	}
	public boolean SetReleaseYear(int releaseYear) {
		this.ReleaseYear = releaseYear;
		return true;
	}
	
	public Film() {
		
	}
}
