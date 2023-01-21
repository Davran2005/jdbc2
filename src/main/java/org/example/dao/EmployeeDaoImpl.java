package org.example.dao;

import org.example.comfig.Util;
import org.example.model.Employee;
import org.example.model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {


    public void createEmployee() {
        String query ="""
                    create table if not exists employee(
                        id serial primary key,
                        first_name varchar,
                        last_name varchar,
                        age int,
                        email varchar,
                        job_id int references job(id));
                    """;
        try {
            Connection connection= Util.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
            System.out.println("таблица успешно создан");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public void addEmployee(Employee employee) {
        String query = """
                insert into employee(first_name,last_name,age, email,job_id)
                values (?,?,?,?,?);
                """;
        try {
            Connection connection= Util.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setInt(5, employee.getJobId());
            preparedStatement.executeUpdate();
            System.out.println("Успушно");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropTable() {
        String query = """
                drop table employee CASCADE; """;

        try {
            Connection connection= Util.getConnection();

            Statement statement = connection.createStatement();
            statement.execute(query);
            System.out.println("Успешно удалена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanTable() {
        String query = """
               truncate employee;
                """;
        try {
            Connection connection= Util.getConnection();

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Успешно");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateEmployee(Long id, Employee employee) {
        try {
            Connection connection= Util.getConnection();
            PreparedStatement preparedStatement= connection.prepareStatement("UPDATE employee set first_name=?,last_name=?,age=?, email=?,job_id=? where id=?;");
           preparedStatement.setString(1,employee.getFirstName());
           preparedStatement.setString(2,employee.getLastName());
           preparedStatement.setInt(3,employee.getAge());
           preparedStatement.setString(4,employee.getEmail());
           preparedStatement.setInt(5,employee.getJobId());
           preparedStatement.setLong(6,id);
           preparedStatement.executeUpdate();
            System.out.println("Успешно обнавился");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Employee> getAllEmployees() {
        List<Employee> list =new ArrayList<>();
         String query = """
                 select * from employee;
                 """;
        try {
            Connection connection= Util.getConnection();

            Statement statement = connection.createStatement();

            ResultSet resultSet=statement.executeQuery(query);
            while (resultSet.next()){
                list.add(new Employee(resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                        ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public Employee findByEmail(String email) {
        Employee employee = new Employee();
        String query="select * from employee where email=?;";
        try {
            Connection connection= Util.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            preparedStatement.executeQuery();
            ResultSet resultSet= preparedStatement.getResultSet();
            while (resultSet.next()){
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setAge( resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJobId(resultSet.getInt("job_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employee;
    }

    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee,Job> employeeJobMap = new HashMap<>();
        String sql = "select * from employee as e join job j on j.id = e.job_id where e.id = ?; ";
        try (Connection connection= Util.getConnection();){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,employeeId);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()){
                employeeJobMap.put(new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")),
                        new Job(
                        resultSet.getLong("id"),
                        resultSet.getString("position"),
                        resultSet.getString("profession"),
                        resultSet.getString("description"),
                        resultSet.getInt("experience")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return employeeJobMap;
    }

    public List<Employee> getEmployeeByPosition(String position) {
        List<Employee>employees = new ArrayList<>();

        String sql = "select * from employee as e join " +
                "job j on j.id = e.job_id where j.position = ?;";
        try {
            Connection connection= Util.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,position);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()){
                employees.add(new Employee(resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return employees;
    }
}
