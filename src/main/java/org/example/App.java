package org.example;

import org.example.dao.EmployeeDao;
import org.example.dao.EmployeeDaoImpl;
import org.example.model.Employee;
import org.example.model.Job;
import org.example.service.EmployeeService;
import org.example.service.EmployeeServiceImpl;
import org.example.service.JobService;
import org.example.service.JobServiceImpl;

import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeServiceImpl();
        JobService jobService = new JobServiceImpl();
        while (true) {
            System.out.println("""
                    1.Job
                    2.Employee
                    """);
            int num = new Scanner(System.in).nextInt();
            switch (num) {
                case 1 -> {
                    while (true) {
                        System.out.println("""
                                ----------JOB------------
                                1.createJobTable
                                2.addJob
                                3.get job by id
                                4.sort by experience
                                5.get job by employeeId
                                6.delete description column
                                """);
                        int sum =new Scanner(System.in).nextInt();
                        switch (sum) {
                            case 1 -> jobService.createJobTable();
                            case 2 -> {
                                System.out.println("position");
                                String position = new Scanner(System.in).nextLine();
                                System.out.println("profession");
                                String profession = new Scanner(System.in).nextLine();
                                System.out.println("description");
                                String description = new Scanner(System.in).nextLine();
                                System.out.println("experience");
                                int experience = new Scanner(System.in).nextInt();
                                jobService.addJob(new Job(position, profession, description, experience));
                            }
                            case 3 -> {
                                System.out.println("id");
                                long id = new Scanner(System.in).nextLong();
                                System.out.println(jobService.getJobById(id));
                            }
                            case 4 -> {
                                System.out.println("Sort asc and desc");
                                String sort =new Scanner(System.in).nextLine();
                                System.out.println(jobService.sortByExperience(sort));
                            }
                            case 5 -> {
                                System.out.println("get job by employeeId");
                                Long id = new Scanner(System.in).nextLong();
                                System.out.println(jobService.getJobByEmployeeId(id));
                            }
                            case 6 -> {
                                System.out.println("delete description column (yes/no)");
                                String a = new Scanner(System.in).nextLine();
                                if (a.equals("yes") || a.equals("no")) {
                                    jobService.deleteDescriptionColumn();
                                }
                            }
                        }
                    }
                }
                case 2->{
                    while (true){
                        System.out.println("""
                1.createEmployee 
                2.addEmployee
                3.dropTable
                4.cleanTable
                5.updateEmployee
                6.getAllEmployees
                7.findByEmail
                8.getEmployeeById
                9.getEmployeeByPosition
                """);
                        int sum = new Scanner(System.in).nextInt();
                        switch (sum) {
                            case 1 -> employeeService.createEmployee();
                            case 2 -> {
                                System.out.println("first name");
                                String name = new Scanner(System.in).nextLine();
                                System.out.println("last_name");
                                String last = new Scanner(System.in).nextLine();
                                System.out.println("Age");
                                int age = new Scanner(System.in).nextInt();
                                System.out.println("email");
                                String email = new Scanner(System.in).nextLine();
                                System.out.println("job_id");
                                int job = new Scanner(System.in).nextInt();
                                employeeService.addEmployee(new Employee(name, last, age, email, job));
                            }
                            case 3 -> employeeService.dropTable();
                            case 4 -> employeeService.cleanTable();

                            case 5 -> {
                                System.out.println("id");
                                Long id = new Scanner(System.in).nextLong();
                                System.out.println("first name");
                                String name = new Scanner(System.in).nextLine();
                                System.out.println("last_name");
                                String last = new Scanner(System.in).nextLine();
                                System.out.println("Age");
                                int age = new Scanner(System.in).nextInt();
                                System.out.println("email");
                                String email = new Scanner(System.in).nextLine();
                                System.out.println("job_id");
                                int job = new Scanner(System.in).nextInt();
                                employeeService.updateEmployee(id, new Employee(name, last, age, email, job));
                            }
                            case 6 -> System.out.print(employeeService.getAllEmployees());
                            case 7 -> {
                                System.out.println("email");
                                String email = new Scanner(System.in).nextLine();
                                System.out.println(employeeService.findByEmail(email));
                            }
                            case 8 -> {
                                System.out.println("get employee by id");
                                Long id = new Scanner(System.in).nextLong();
                                System.out.println(employeeService.getEmployeeById(id));
                            }
                            case 9 -> employeeService.getEmployeeByPosition("sds").forEach(System.out::println);

                        }
                    }
                }
            }
        }
    }
}
