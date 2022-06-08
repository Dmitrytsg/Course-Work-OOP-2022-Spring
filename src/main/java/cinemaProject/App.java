//package cinemaProject;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import javax.persistence.*;
//import cinemaProject.*;
//
//public class App {
//	public static void main(String[] args) throws Exception {
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hb-persistence");
//		EntityManager em = emf.createEntityManager();
//
//		System.out.println("Start\n\n");
//
//		em.getTransaction().begin();
//
//
//
//		Film fm = em.find(Film.class, 3);
//
//		Session sn = em.find(Session.class, 5);
//
//		Session sn = new Session();
//		sn.SetFinishTime(16, 45);
//		sn.SetStartTime(15, 0);
//		sn.SetDate(2022, 5, 13);
//		sn.SetTicketCount(25);
//
//
//		//sn.SetFilmID(null);
//
//		System.out.println(fm.GetName());
//		System.out.println(fm.GetGenre());
//		System.out.println(fm.GetReleaseYear());
//
//		//fm.removeSession(sn);
//		//fm.addSession(sn);
//
//		//em.persist(fm);
//		//em.getTransaction().commit();
//
//		/*for (Session session : fm.GetSessionList()){
//			System.out.println(session.GetDate());
//	    }
//
//		System.out.println("\n\nFinish");
//	}
//
//}
