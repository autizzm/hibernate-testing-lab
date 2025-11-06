package org.bsuir.dao;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.bsuir.config.HibernateConfig;
import org.bsuir.model.Employee;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeDAOTest {
    private static EmployeeDAO employeeDAO;
    private static HibernateConfig config = null;
    private static Deque<Integer> ids;
    @BeforeAll
    static void configure() {
        employeeDAO = new EmployeeDAO();
        ids = new ArrayDeque<>();
    }
    @Test
    @Order(1)
    void createValidEmployeeTest() {
        try {
            Employee empl = employeeDAO.getEmployeeFromXML("src/main/resources/files/emp1.xml");
            employeeDAO.saveEmployee(empl);
        } catch (IOException | JAXBException e) {
            fail(e.getLocalizedMessage());
        }
    }
    @Test
    @Order(2)
    void createInvalidEmployeeTest() {
        try {
            Employee empl = employeeDAO.getEmployeeFromXML("src/main/resources/files/emp2.xml");
            assertThrows(ConstraintViolationException.class, () -> employeeDAO.saveEmployee(empl));
        } catch (IOException | JAXBException e) {
            fail(e.getLocalizedMessage());
        }
    }

    // и тут половина такие, просто остальные работают
    @Disabled("Кто-то очень умни написал тест, выполнение которого зависит от данных из БД")
    @Test
    @Order(3)
    void updateEmployeeTest() {
        try {
            Employee empl = employeeDAO.getEmployeeFromXML("src/main/resources/files/emp3.xml");
            employeeDAO.updateEmployee(empl, 3);
        } catch (IOException | JAXBException e) {
            fail(e.getLocalizedMessage());
        }
    }
    @Test
    @Order(4)
    void employeeToXMLTest() {
        Employee emp = employeeDAO.findEmployeeByID(3);
        try {
            JAXBContext context = JAXBContext.newInstance(Employee.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/files/out.xml"));
            marshaller.marshal(emp, writer);
            writer.close();
        } catch (JAXBException | IOException e) {
            fail();
        }
    }
    @Test
    @Order(5)
    void toJsonTest() throws Exception {
        employeeDAO.dumpEmployeeToJson("src/main/resources/files/out.json", employeeDAO.findEmployeeByID(3));
    }
    @Test
    @Order(6)
    void jsonTest() throws IOException {
        employeeDAO.saveEmployee(employeeDAO.getEmployeeFromJson("src/main/resources/files/in.json"));
    }

    private static Stream<Employee> OKEmployees(){
        return Stream.of(new Employee("Jake", Date.valueOf(LocalDate.now().minusYears(20)), new BigDecimal(123000)),
                new Employee("Lewis", Date.valueOf(LocalDate.now().minusYears(37).minusDays(89)), new BigDecimal(10000000)),
                new Employee("Max", Date.valueOf(LocalDate.now().minusYears(33).minusMonths(2).minusDays(11)), new BigDecimal(5300000)));
    }
    private static Stream<Employee> incorrectEmployees(){
        return Stream.of(new Employee("", Date.valueOf(LocalDate.now().minusYears(20)), new BigDecimal(123000)), //empty name
                new Employee("Lewis", Date.valueOf(LocalDate.now().minusYears(4).minusDays(89)), new BigDecimal(10000000)), //too young
                new Employee("Max", Date.valueOf(LocalDate.now().minusYears(33).minusMonths(2).minusDays(11)), new BigDecimal(-5300000))); //negative salary
    }

    @Order(7)
    @ParameterizedTest
    @MethodSource("OKEmployees")
    void saveEmployeeTest__OKEmployees(Employee employee){
        Employee persistedEmployee = employeeDAO.saveEmployee(employee);
        assertNotNull(persistedEmployee);
        ids.push(persistedEmployee.getEmployeeId());
        Employee foundEmployee = employeeDAO.findEmployeeByID(persistedEmployee.getEmployeeId());

        assertEquals(persistedEmployee.getEmployeeId(), foundEmployee.getEmployeeId());
        assertEquals(persistedEmployee.getEmplName(), foundEmployee.getEmplName());
        assertEquals(persistedEmployee.getEmplDob(), foundEmployee.getEmplDob());
        assertEquals(persistedEmployee.getEmplSalary().doubleValue(), foundEmployee.getEmplSalary().doubleValue(), 0.000001);
    }

    @Order(8)
    @ParameterizedTest
    @MethodSource("incorrectEmployees")
    void saveEmployeeTest__incorrectEmployees(Employee employee){
        assertThrows(ConstraintViolationException.class, () -> employeeDAO.saveEmployee(employee));
    }

    @Order(9)
    @ParameterizedTest
    @MethodSource("OKEmployees")
    void updateEmployeeTest(Employee employee){
        Employee persistedEmployee = employeeDAO.saveEmployee(employee);
        assertNotNull(persistedEmployee);
        ids.push(persistedEmployee.getEmployeeId());

        Employee updatedEmployee = new Employee(ids.peek(),"Niko", Date.valueOf(LocalDate.now().minusYears(20)), new BigDecimal(123000));


        employeeDAO.updateEmployee(updatedEmployee, ids.peek());
        Employee foundEmployee = employeeDAO.findEmployeeByID(ids.peek());


        assertEquals(updatedEmployee.getEmployeeId(), foundEmployee.getEmployeeId());
        assertEquals(updatedEmployee.getEmplName(), foundEmployee.getEmplName());
        assertEquals(updatedEmployee.getEmplDob(), foundEmployee.getEmplDob());
        assertEquals(updatedEmployee.getEmplSalary().doubleValue(), foundEmployee.getEmplSalary().doubleValue(), 0.000001);
    }

    @Order(10)
    @ParameterizedTest
    @MethodSource("OKEmployees")
    void findEmployeeTest(Employee employee){
        Employee persistedEmployee = employeeDAO.saveEmployee(employee);
        assertNotNull(persistedEmployee);
        ids.push(persistedEmployee.getEmployeeId());
        Employee foundEmployee = employeeDAO.findEmployeeByID(persistedEmployee.getEmployeeId());

        assertEquals(persistedEmployee.getEmployeeId(), foundEmployee.getEmployeeId());
        assertEquals(persistedEmployee.getEmplName(), foundEmployee.getEmplName());
        assertEquals(persistedEmployee.getEmplDob(), foundEmployee.getEmplDob());
        assertEquals(0, persistedEmployee.getEmplSalary().compareTo(foundEmployee.getEmplSalary()));
    }

    @Order(11)
    @ParameterizedTest
    @MethodSource("OKEmployees")
    void removeEmployeeTest(Employee employee){
        Employee persistedEmployee = employeeDAO.saveEmployee(employee);
        assertNotNull(persistedEmployee);

        employeeDAO.removeEmployee(persistedEmployee.getEmployeeId());
        Employee deletedEmployee = employeeDAO.findEmployeeByID(persistedEmployee.getEmployeeId());


        assertNull(deletedEmployee);
    }

    @AfterEach
    void cleanPersistedEntities(){
        while(!ids.isEmpty()){
            employeeDAO.removeEmployee(ids.pop());
        }
    }
}