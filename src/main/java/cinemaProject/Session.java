package cinemaProject;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "cinema_db.session")
public class Session {
	@Id
	@Column(name = "sid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int SessionID;
	
	@ManyToOne(fetch = FetchType.LAZY, optional=true, cascade=CascadeType.ALL)
	@JoinColumn(name="film_id")
	private Film film_id = new Film();
	
	@Column(name = "s_date")
	private Calendar Date;
	
	@Column(name = "s_starttime")
	private Calendar StartTime;
	
	@Column(name = "s_finishtime")
	private Calendar FinishTime;
	
	@Column(name = "s_ticketcount")
	private int TicketCount;
	
	
	
	public int GetSessionID() {
		return SessionID;
	}
	public boolean SetSessionID(int sessionID) {
		this.SessionID = sessionID;
		return true;
	}
	
	public Film GetFilmID() {
		return film_id;
	}
	public boolean SetFilmID(Film filmID) {
		this.film_id = filmID;
		return true;
	}
	public void removeFilm() {
		this.film_id = null;
	}
	
	
	public Date GetDate() {
		return Date.getTime();
	}
	
	public boolean SetDate(int year, int month, int day) throws Exception {
		if (String.valueOf(year).length() < 1 | String.valueOf(year).length() > 4 | year == 0) { throw new Exception("The length of the Year field must be 4"); }
		Date = new GregorianCalendar();
		Date.set(Calendar.YEAR, year);
		Date.set(Calendar.MONTH, (month-1));
		Date.set(Calendar.DAY_OF_MONTH, day);
		
		if (StartTime != null) {
			StartTime.set(Calendar.YEAR, year);
			StartTime.set(Calendar.MONTH, (month-1));
			StartTime.set(Calendar.DAY_OF_MONTH, day);
		}
		
		if (FinishTime != null) {
			FinishTime.set(Calendar.YEAR, year);
			FinishTime.set(Calendar.MONTH, (month-1));
			FinishTime.set(Calendar.DAY_OF_MONTH, day);
		}
		
		return true;
	}
	
	
	public Date GetStartTime() {
		return StartTime.getTime();
	}
	public boolean SetStartTime(int hour, int minute) {
		if (Date == null) { StartTime = new GregorianCalendar(11,11,11); }
		else { StartTime = new GregorianCalendar(this.Date.get(Calendar.YEAR),this.Date.get(Calendar.MONTH),this.Date.get(Calendar.DAY_OF_MONTH)); }
		
		StartTime.set(Calendar.HOUR_OF_DAY, hour);
		StartTime.set(Calendar.MINUTE, minute);
		return true;
	}
	
	public Date GetFinishTime() {
		return FinishTime.getTime();
	}
	public boolean SetFinishTime(int hour, int minute) {
		if (Date == null) { FinishTime = new GregorianCalendar(11,11,11); }
		else {FinishTime = new GregorianCalendar(this.Date.get(Calendar.YEAR),this.Date.get(Calendar.MONTH),this.Date.get(Calendar.DAY_OF_MONTH)); }

		FinishTime.set(Calendar.HOUR_OF_DAY, hour);
		FinishTime.set(Calendar.MINUTE, minute);
		return true;
	}
	
	public int GetTicketCount() {
		return TicketCount;
	}
	public boolean SetTicketCount(int ticketCount) {
		this.TicketCount = ticketCount;
		return true;
	}
	

	public ArrayList<Integer> GetSessionTime() {
		ArrayList<Integer> res = new ArrayList<Integer>();
		res.add(0, this.FinishTime.get(Calendar.HOUR_OF_DAY) - this.StartTime.get(Calendar.HOUR_OF_DAY));
		res.add(1, this.FinishTime.get(Calendar.MINUTE) + (60 - this.StartTime.get(Calendar.MINUTE)));
		return res;
	}
	
	public Session() {
		
	}
}
