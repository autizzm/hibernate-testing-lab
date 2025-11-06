package org.bsuir.dao;

import org.bsuir.config.HibernateConfig;
import org.bsuir.model.Employee;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class EmployeeDAO {
    private static HibernateConfig config = null;
    public EmployeeDAO() {
        config = HibernateConfig.getInstanceOfSeccionFactory();
    }
    public Employee getEmployeeFromXML(String filename) throws IOException, JAXBException {
        StringBuilder strbldr = new StringBuilder();
        ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
        for (String line : lines) {
            strbldr.append(line);
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (Employee) jaxbUnmarshaller.unmarshal(new StringReader(strbldr.toString()));
    }
    public Employee saveEmployee(Employee entity) {
        Session session = config.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Employee emp;
        try {
            session.persist(entity);
            emp = session.find(Employee.class, entity.getEmployeeId());
        } catch (ConstraintViolationException e) {
            throw e;
        } finally {
            transaction.commit();
            session.close();
        }
        return emp;
    }
    public Employee findEmployeeByID(Integer id) {
        Session session = config.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Employee employee = session.find(Employee.class, id);
        transaction.commit();
        session.close();
        return employee;
    }
    public Employee updateEmployee(Employee newEmpl, Integer id) {
        Session session = config.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Employee oldEmp;
        try {
            oldEmp = session.find(Employee.class, id);
            oldEmp.setEmplName(newEmpl.getEmplName());
            oldEmp.setEmplDob(newEmpl.getEmplDob());
            oldEmp.setEmplSalary(newEmpl.getEmplSalary());
        } catch (ConstraintViolationException e) {
            throw e;
        } finally {
            transaction.commit();
            session.close();
        }
        return oldEmp;
    }
    public List<Employee> getEmployees() {
        Session session = config.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<Employee> empl = session.createQuery("FROM Employee", Employee.class).list();
        transaction.commit();
        session.close();
        return empl;
    }
    public void removeEmployee(Integer id) {
        Session session = config.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Employee empl;
        try {
            empl = session.find(Employee.class, id);
            session.remove(empl);
        } catch (ConstraintViolationException e) {
            throw e;
        } finally {
            transaction.commit();
            session.close();
        }
    }
    public Employee getEmployeeFromJson(String filename) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd").create();
        Reader reader = null;
        Employee employee;
        try {
            reader = new FileReader(filename);
            employee = gson.fromJson(reader, Employee.class);
        } catch (FileNotFoundException ex) {
            throw ex;
        } finally {
            reader.close();
        }
        return employee;
    }
    public void dumpEmployeeToJson(String filename, Employee employee) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd").create();
        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            gson.toJson(employee, writer);
        } catch (Exception ex) {
            throw ex;
        } finally {
            writer.close();
        }
    }
}
