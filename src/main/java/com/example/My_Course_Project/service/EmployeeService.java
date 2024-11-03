package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Brigade;
import com.example.My_Course_Project.model.Employee;
import com.example.My_Course_Project.model.Schedule;
import com.example.My_Course_Project.repository.EmployeeRepository;
import com.example.My_Course_Project.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee); // Зберегти працівника у базі даних
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public byte[] getEmployeeImage(int id) {
        // Отримуємо працівника за ID
        Employee employee = employeeRepository.findById(id).orElse(null);

        // Повертаємо зображення
        return (employee != null) ? employee.getImage() : null; // Якщо працівника не знайдено, повертаємо null
    }

    public void saveEmployeeImage(int id, byte[] image) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found")); // Пошук працівника за ID
        employee.setImage(image); // Зберігаємо зображення
        employeeRepository.save(employee); // Оновлюємо запис у базі даних
    }

    public Employee getEmployeeById(int employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Працівника не знайдено з ID: " + employeeId));
    }

    public List<Object[]> findBrigadeCompositionForProject(int projectId) {
        List<Object[]> resultList = new ArrayList<>();
        Set<Integer> addedEmployees = new HashSet<>(); // Унікальні ID працівників

        List<Schedule> schedules = scheduleRepository.findByProjectId(projectId); // Отримуємо розклади лише для обраного проекту

        for (Schedule schedule : schedules) {
            Brigade brigade = schedule.getBrigade();
            List<Employee> employees = employeeRepository.findByBrigadeId(brigade.getId());

            for (Employee employee : employees) {
                if (!addedEmployees.contains(employee.getId())) { // Перевіряємо, чи працівника ще не додано
                    resultList.add(new Object[]{
                            brigade.getName(),           // Назва бригади
                            employee.getName(),          // Ім'я робітника
                            employee.getPosition(),      // Посада робітника
                            schedule.getSite().getName() // Назва ділянки
                    });
                    addedEmployees.add(employee.getId()); // Додаємо ID працівника до множини
                }
            }
        }
        return resultList;
    }




}
