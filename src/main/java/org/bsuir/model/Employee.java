package org.bsuir.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;



@Entity
@Table(name = "employee")
@XmlType(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    @XmlTransient
    private int employeeId;
    @Column(name = "empl_name", nullable = false, length = 30)
    private String emplName;
    @Column(name = "empl_dob", nullable = false)
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Date emplDob;
    @Column(name = "empl_salary", precision = 10, scale = 2)
    private BigDecimal emplSalary;
    public Employee() {
    }
    public Employee(String emplName, Date emplDob, BigDecimal emplSalary) {
        this.emplName = emplName;
        this.emplDob = emplDob;
        this.emplSalary = emplSalary;
    }
    public Employee(int employeeId, String emplName, Date emplDob, BigDecimal emplSalary) {
        this.employeeId = employeeId;
        this.emplName = emplName;
        this.emplDob = emplDob;
        this.emplSalary = emplSalary;
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public String getEmplName() {
        return emplName;
    }
    public void setEmplName(String emplName) {
        this.emplName = emplName;
    }
    public Date getEmplDob() {
        return emplDob;
    }
    public void setEmplDob(Date emplDob) {
        this.emplDob = emplDob;
    }
    public BigDecimal getEmplSalary() {
        return emplSalary;
    }
    public void setEmplSalary(BigDecimal emplSalary) {
        this.emplSalary = emplSalary;
    }
    @Override
    public String toString() {
        return "Employee{" + "employeeId=" + employeeId + ", emplName='" + emplName + '\'' + ", emplDob=" + emplDob
                + ", emplSalary=" + emplSalary + '}';
    }
    @Override
    public int hashCode() {
        return Objects.hash(emplDob, emplName, emplSalary, employeeId);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Employee other = (Employee) obj;
        return Objects.equals(emplDob, other.emplDob) && Objects.equals(emplName, other.emplName)
                && Objects.equals(emplSalary, other.emplSalary) && employeeId == other.employeeId;
    }
}
