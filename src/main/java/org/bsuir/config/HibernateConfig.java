package org.bsuir.config;
import org.bsuir.model.Employee;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateConfig {
    private static HibernateConfig sc;
    final SessionFactory sessionFactory;
    private HibernateConfig() {
        sessionFactory = new Configuration().addAnnotatedClass(Employee.class).configure().buildSessionFactory();
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static HibernateConfig getInstanceOfSeccionFactory() {
        if (sc == null)
            sc = new HibernateConfig();
        return sc;
    }
}

