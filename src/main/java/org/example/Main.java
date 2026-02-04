package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.model.User;

public class Main {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("user");

    public static void main(String[] args) {

        User user = new User("Nick Cave", "nick.cave@gmail.com");

        //Persist
        persistUser(user);


        detachUser(user);

        // Merge
        user.setEmail("nickiiii.cave@gmail.com");
        reattachUser(user);

        deleteUser(user);

        emf.close();
    }

    // Persist metoda
    private static void persistUser(User user) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
            System.out.println("Korisnik uspješno persistan: \t" + user);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Detach metoda
    private static void detachUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User managedUser = em.find(User.class, user.getId());
            em.detach(managedUser);
            System.out.println("User detached: \t" + managedUser);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Merge
    private static void reattachUser(User user) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User mergedUser = em.merge(user);
            tx.commit();
            System.out.println("Korisnik ažuriran: \t" + mergedUser);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Delete metoda
    private static void deleteUser(User user) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User managedUser = em.find(User.class, user.getId());
            if (managedUser != null) {
                em.remove(managedUser);
                System.out.println("User deleted:\t " + managedUser);
            } else {
                System.out.println("Korisnik nije pronađen za potrebe Brisanja.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
