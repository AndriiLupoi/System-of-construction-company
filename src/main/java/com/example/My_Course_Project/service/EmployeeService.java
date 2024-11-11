package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.*;
import com.example.My_Course_Project.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    private JobCategoryRepository jobCategoryRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private BrigadeRepository brigadeRepository;

    public void saveEmployee(String name, String position, int jobCategoryId, int siteId, int brigadeId, MultipartFile image) throws IOException {
        Employee employee = new Employee();

        employee.setName(name);
        employee.setPosition(position);

        JobCategory jobCategory = jobCategoryRepository.findById(jobCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("JobCategory not found"));
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new EntityNotFoundException("Site not found"));
        Brigade brigade = brigadeRepository.findById(brigadeId)
                .orElseThrow(() -> new EntityNotFoundException("Brigade not found"));

        employee.setJobCategory(jobCategory);
        employee.setSite(site);
        employee.setBrigade(brigade);

        // Збереження зображення, якщо воно завантажене
        if (image != null && !image.isEmpty()) {
            byte[] imageData = image.getBytes();
            employee.setImage(imageData); // Зберігаємо зображення у полі типу byte[]
        }

        employeeRepository.save(employee);
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

    public void deleteEmployeeById(int id) {
        employeeRepository.deleteById(id);
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

    public List<Object[]> findEngineeringStaffBySiteOrManagement(Integer siteId, Integer managementId) {
        List<Object[]> resultList = new ArrayList<>();

        List<Employee> employees = employeeRepository.findByBrigade_Site_IdOrBrigade_Site_BuildingManagement_Id(
                siteId, managementId);

        for (Employee employee : employees) {
            if (employee.getJobCategory() != null &&
                    "Інженерно-технічний склад".equals(employee.getJobCategory().getName())) {

                resultList.add(new Object[] {
                        employee.getName(),
                        employee.getPosition(),
                        employee.getBrigade().getSite().getName(),
                        employee.getBrigade().getSite().getBuildingManagement().getName(),
                        employee.getJobCategory().getName()
                });
            }
        }

        return resultList;
    }


    public void updateEmployee(Employee updatedEmployee) {
        // Пошук працівника в базі даних за ID
        Employee existingEmployee = findEmployeeById(updatedEmployee.getId());

        // Оновлення полів
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setPosition(updatedEmployee.getPosition());
        existingEmployee.setJobCategory(updatedEmployee.getJobCategory());
        existingEmployee.setSite(updatedEmployee.getSite());
        existingEmployee.setBrigade(updatedEmployee.getBrigade());

        // Оновлення зображення, якщо воно присутнє
        if (updatedEmployee.getImage() != null) {
            existingEmployee.setImage(updatedEmployee.getImage());
        }

        // Збереження оновленого працівника в базу даних
        employeeRepository.save(existingEmployee);
    }

    public Employee findEmployeeById(int id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}
