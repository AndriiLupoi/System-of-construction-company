package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Employee;
import com.example.My_Course_Project.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

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

}
