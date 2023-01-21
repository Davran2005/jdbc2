package org.example.dao;

import org.example.comfig.Util;
import org.example.model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JobDaoImpl implements JobDao {


    public void createJobTable() {
        try {
            Connection connection=Util.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                    create  table  if not   exists job(
                      id serial primary key,
                      position varchar,
                      profession varchar,
                      description varchar,
                     experience int);
                      """);
            statement.close();
            System.out.println("таблица успешно создан");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addJob(Job job) {
String query = """
        insert into job (position,profession, description,experience)
        values (?,?,?,?);
        """;
        try {
            Connection connection=Util.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,job.getPosition());
            preparedStatement.setString(2,job.getProfession());
            preparedStatement.setString(3,job.getDescription());
            preparedStatement.setInt(4,job.getExperience());
            preparedStatement.executeUpdate();
            System.out.println("Успушно");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Job getJobById(Long jobId) {
        String query = """
                select * from job where id =?;
                """;
        Job job=new Job();

        try {
            Connection connection=Util.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,jobId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                if(!resultSet.getString("description").isEmpty()) {
                    job.setDescription(resultSet.getString("description"));
                }
                job.setExperience(resultSet.getInt("experience"));
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return job;
    }

    public List<Job> sortByExperience(String ascOrDesc) {
        List <Job> jobs = new ArrayList<>();
        String query ="";
        switch (ascOrDesc){
            case "asc" -> query = "select * from job order by experience";
            case "desc" -> query ="select * from job order by experience desc";
        }
        try {
            Connection connection=Util.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet =statement.executeQuery(query);

            while (resultSet.next()){
                jobs.add(new Job(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5)));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return jobs;
    }

    public Job getJobByEmployeeId(Long employeeId) {
        Job job;
        String sql = "select * from job join employee e on job.id = e.jod_id where e.id = ?;";
        try {
            Connection connection=Util.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,employeeId);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                job = new Job(
                        resultSet.getLong("id"),
                        resultSet.getString("position"),
                        resultSet.getString("profession"),
                        resultSet.getString("description"),
                        resultSet.getInt("experience")
                );
                return job;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void deleteDescriptionColumn() {
        String sql = "ALTER TABLE job DROP COLUMN description CASCADE;";
        try {
            Connection connection =Util.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("yes");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
