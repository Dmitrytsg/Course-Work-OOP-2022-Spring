package cinemaProject;

import java.util.List;
import javax.persistence.*;

public class Data {
    //toGUI
    String[][] FilmList;
    String[][] SessionList;
    String[][] PersonList;

    //from DB ClassList
    List<Film> fmlist;
    List<Session> snlist;
    List<Person> prlist;

    public String[][] getFilmList(){
        return FilmList;
    }

    public String[][] getSessionList(){
        return SessionList;
    }

    public String[][] getPersonList(){
        return PersonList;
    }

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hb-persistence");
    EntityManager em = emf.createEntityManager();

    public List<Session> GetSnList(){
        return this.snlist;
    }

    public boolean deleteFilm(int i){

        em.getTransaction().begin();
        Film toDel = em.find(Film.class, fmlist.get(i).GetFilmID());
        em.remove(toDel);
        em.getTransaction().commit();

        fmlist.remove(i);
        FilmList = new String[fmlist.size()][4];
        int j = 0;
        for (Film fm:  fmlist){
            FilmList[j][0] = String.valueOf(fm.GetFilmID());
            FilmList[j][1] = fm.GetName();
            FilmList[j][2] = String.valueOf(fm.GetReleaseYear());
            FilmList[j][3] = fm.GetGenre();
            j++;
        }
        return true;
    }

    public boolean addFilm(String Name, String Genre, Integer ReleaseYear, int[] SelectedRows){

        em.getTransaction().begin();
        Film toAdd = new Film();

        toAdd.SetName(Name);
        toAdd.SetGenre(Genre);
        toAdd.SetReleaseYear(ReleaseYear);
        for (int pr : SelectedRows) {
            toAdd.addPerson(prlist.get(pr));
        }

        fmlist.add(toAdd);


        em.persist(toAdd);
        em.getTransaction().commit();

        FilmList = new String[fmlist.size()][4];
        int j = 0;
        for (Film fm:  fmlist){
            FilmList[j][0] = String.valueOf(fm.GetFilmID());
            FilmList[j][1] = fm.GetName();
            FilmList[j][2] = String.valueOf(fm.GetReleaseYear());
            FilmList[j][3] = fm.GetGenre();
            j++;
        }

        return true;
    }

    public Film getFilm(int i){
        return fmlist.get(i);
    }

    public boolean RewriteFilm(int i, String Name, String Genre, Integer ReleaseYear){

        em.getTransaction().begin();
        Film toRewrite = em.find(Film.class, fmlist.get(i).GetFilmID());

        toRewrite.SetName(Name);
        toRewrite.SetGenre(Genre);
        toRewrite.SetReleaseYear(ReleaseYear);

        fmlist.get(i).SetName(Name);
        fmlist.get(i).SetGenre(Genre);
        fmlist.get(i).SetReleaseYear(ReleaseYear);

        em.getTransaction().commit();

        FilmList = new String[fmlist.size()][4];
        int j = 0;
        for (Film fm:  fmlist){
            FilmList[j][0] = String.valueOf(fm.GetFilmID());
            FilmList[j][1] = fm.GetName();
            FilmList[j][2] = String.valueOf(fm.GetReleaseYear());
            FilmList[j][3] = fm.GetGenre();
            j++;
        }

        return true;
    }

    public boolean deleteSession(int i){

        em.getTransaction().begin();
        Session toDel = em.find(Session.class, snlist.get(i).GetSessionID());
        em.remove(toDel);
        em.getTransaction().commit();

        snlist.remove(i);
        SessionList = new String[snlist.size()][5];
        int j = 0;
        for (Session sn:  snlist){
            SessionList[j][0] = String.valueOf(sn.GetSessionID());
            SessionList[j][1] = String.valueOf(sn.GetDate());
            SessionList[j][2] = String.valueOf(sn.GetStartTime());
            SessionList[j][3] = String.valueOf(sn.GetFinishTime());
            SessionList[j][4] = String.valueOf(sn.GetTicketCount());
            j++;
        }
        return true;
    }

    public boolean addSession(String Date, String StartTime, String FinishTime, Integer TicketCount,  int[] SelectedRows) throws Exception {

        String[] DateSplit = Date.split("-");
        String[] StartTimeSplit = StartTime.split(":");
        String[] FinishTimeSplit = FinishTime.split(":");

        em.getTransaction().begin();
        Session toAdd = new Session();

        toAdd.SetDate(Integer.parseInt(DateSplit[0]), Integer.parseInt(DateSplit[1]), Integer.parseInt(DateSplit[2]));
        toAdd.SetStartTime(Integer.parseInt(StartTimeSplit[0]), Integer.parseInt(StartTimeSplit[1]));
        toAdd.SetFinishTime(Integer.parseInt(FinishTimeSplit[0]), Integer.parseInt(FinishTimeSplit[1]));
        toAdd.SetTicketCount(TicketCount);
        toAdd.SetFilmID(fmlist.get(SelectedRows[0]));


        snlist.add(toAdd);

        em.persist(toAdd);
        em.getTransaction().commit();

        fmlist.get(SelectedRows[0]).addSession(toAdd);

        SessionList = new String[snlist.size()][5];
        int j = 0;
        for (Session sn:  snlist){
            SessionList[j][0] = String.valueOf(sn.GetSessionID());
            SessionList[j][1] = String.valueOf(sn.GetDate());
            SessionList[j][2] = String.valueOf(sn.GetStartTime());
            SessionList[j][3] = String.valueOf(sn.GetFinishTime());
            SessionList[j][4] = String.valueOf(sn.GetTicketCount());
            j++;
        }

        return true;
    }

    public boolean deletePerson(int i){

        em.getTransaction().begin();
        Person toDel = em.find(Person.class, prlist.get(i).GetPersonID());
        em.remove(toDel);
        em.getTransaction().commit();

        prlist.remove(i);
        PersonList = new String[prlist.size()][5];
        int j = 0;
        for (Person pr:  prlist){
            PersonList[j][0] = String.valueOf(pr.GetPersonID());
            PersonList[j][1] = pr.GetFirstName();
            PersonList[j][2] = pr.GetLastName();
            PersonList[j][3] = String.valueOf(pr.GetYearsOld());
            PersonList[j][4] = pr.GetStatus();
            j++;
        }
        return true;
    }

    public boolean addPerson(String firstname, String lastname, Integer yearsold, String status, int[] SelectedRows){

        em.getTransaction().begin();
        Person toAdd = new Person();

        toAdd.SetFirstName(firstname);
        toAdd.SetLastName(lastname);
        toAdd.SetYearsOld(yearsold);
        toAdd.SetStatus(status);
        for (int fm : SelectedRows) {
            toAdd.addFilm(fmlist.get(fm));
        }

        prlist.add(toAdd);


        em.persist(toAdd);
        em.getTransaction().commit();

        PersonList = new String[prlist.size()][5];
        int i = 0;
        for (Person pr:  prlist){
            PersonList[i][0] = String.valueOf(pr.GetPersonID());
            PersonList[i][1] = pr.GetFirstName();
            PersonList[i][2] = pr.GetLastName();
            PersonList[i][3] = String.valueOf(pr.GetYearsOld());
            PersonList[i][4] = pr.GetStatus();
            i++;
        }

        return true;
    }

    //Get Date from DB
    public Data(){

        em.getTransaction().begin();
        fmlist = em.createQuery("SELECT f FROM Film f").getResultList();
        snlist = em.createQuery("SELECT s FROM Session s").getResultList();
        prlist = em.createQuery("SELECT p FROM Person p").getResultList();

        FilmList = new String[fmlist.size()][4];
        SessionList = new String[snlist.size()][5];
        PersonList = new String[prlist.size()][5];

        if (fmlist == null) {
            System.out.println("NULL\n\n");
        } else {
            int i = 0;
            for (Film fm:  fmlist){
                FilmList[i][0] = String.valueOf(fm.GetFilmID());
                FilmList[i][1] = fm.GetName();
                FilmList[i][2] = String.valueOf(fm.GetReleaseYear());
                FilmList[i][3] = fm.GetGenre();
                i++;
            }
        }

        if (snlist == null) {
            System.out.println("NULL\n\n");
        } else {
            int i = 0;
            for (Session sn:  snlist){
                SessionList[i][0] = String.valueOf(sn.GetSessionID());
                SessionList[i][1] = String.valueOf(sn.GetDate());
                SessionList[i][2] = String.valueOf(sn.GetStartTime());
                SessionList[i][3] = String.valueOf(sn.GetFinishTime());
                SessionList[i][4] = String.valueOf(sn.GetTicketCount());
                i++;
            }
        }

        if (prlist == null) {
            System.out.println("NULL\n\n");
        } else {
            int i = 0;
            for (Person pr:  prlist){
                PersonList[i][0] = String.valueOf(pr.GetPersonID());
                PersonList[i][1] = pr.GetFirstName();
                PersonList[i][2] = pr.GetLastName();
                PersonList[i][3] = String.valueOf(pr.GetYearsOld());
                PersonList[i][4] = pr.GetStatus();
                i++;
            }
        }

        em.getTransaction().commit();
    }
}
