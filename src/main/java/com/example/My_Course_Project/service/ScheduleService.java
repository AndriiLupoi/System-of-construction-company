package com.example.My_Course_Project.service;

import com.example.My_Course_Project.model.*;
import com.example.My_Course_Project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private WorkTypeRepository workTypeRepository;
    @Autowired
    private BrigadeRepository brigadeRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ReportRepository reportRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // Логіка пошуку розкладів
    public List<Schedule> searchSchedules(String query) {
        try {
            Integer id = Integer.parseInt(query);
            return scheduleRepository.findByProjectIdOrWorkTypeIdOrStartDateOrEndDate(id, id, null, null);
        } catch (NumberFormatException e) {
            // Якщо не число, спробуйте розпізнати як дату
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Задайте формат дати
                Date date = dateFormat.parse(query);

                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return scheduleRepository.findByProjectIdOrWorkTypeIdOrStartDateOrEndDate(null, null, localDate, localDate);
            } catch (ParseException ex) {
                // Тут ви можете обробити інші значення, наприклад, якщо ви хочете шукати за іншим критерієм
                throw new RuntimeException("Invalid search query: " + query, ex);
            }
        }
    }

    public void saveSchedule(int projectId, int workTypeId, LocalDate startDate, LocalDate endDate) {
        Schedule schedule = new Schedule();

        // Отримуємо об'єкти Project та WorkType з репозиторіїв
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Проект не знайдено"));
        WorkType workType = workTypeRepository.findById(workTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Тип роботи не знайдено"));

        // Встановлюємо об'єкти Project та WorkType
        schedule.setProject(project);
        schedule.setWorkType(workType);
        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);

        // Зберігаємо об'єкт у базі даних
        scheduleRepository.save(schedule);
    }

    public void deleteScheduleById(int id) {
        scheduleRepository.deleteById(id);
    }

    public List<Schedule> getSchedulesByProjectName(String projectName) {
        // Припускаємо, що у вас є метод у репозиторії, який знаходить ID проекту за його назвою
        Integer projectId = scheduleRepository.findProjectIdByName(projectName);
        return scheduleRepository.findByProjectId(projectId);
    }

    public List<Object[]> findBrigadesByWorkTypeAndPeriod(int workTypeId, LocalDate startDate, LocalDate endDate) {
        Set<Object[]> resultSet = new HashSet<>(); // Використовуємо Set для унікальності

        // Отримуємо всі розклади
        List<Schedule> schedules = scheduleRepository.findAll();

        for (Schedule schedule : schedules) {
            // Перевіряємо, чи відповідає тип роботи та період
            if (schedule.getWorkType().getId() == workTypeId &&
                    !schedule.getStartDate().isBefore(startDate) &&
                    !schedule.getEndDate().isAfter(endDate)) {

                // Додаємо до результату
                resultSet.add(new Object[]{
                        schedule.getBrigade().getName(),
                        schedule.getProject().getName(),
                        schedule.getWorkType().getName()
                });
            }
        }

        return new ArrayList<>(resultSet); // Повертаємо список результатів
    }

    public List<Object[]> findWorkDetailsByBrigadeAndPeriod(int brigadeId, LocalDate startDate, LocalDate endDate) {
        Set<Object[]> resultSet = new HashSet<>(); // Використовуємо Set для унікальності результатів

        // Отримуємо всі розклади
        List<Schedule> schedules = scheduleRepository.findAll();

        for (Schedule schedule : schedules) {
            // Перевіряємо, чи відповідає бригада та період
            if (schedule.getBrigade().getId() == brigadeId &&
                    !schedule.getStartDate().isBefore(startDate) &&
                    !schedule.getEndDate().isAfter(endDate)) {

                // Додаємо до результату
                resultSet.add(new Object[]{
                        schedule.getBrigade().getName(),       // Назва бригади
                        schedule.getWorkType().getName(),      // Тип робіт
                        schedule.getProject().getName(),       // Назва проекту
                        schedule.getStartDate(),               // Дата початку
                        schedule.getEndDate()                  // Дата закінчення
                });
            }
        }

        return new ArrayList<>(resultSet); // Повертаємо список результатів
    }

    public List<Object[]> findConstructionProjectsByManagementOrSite(Integer managementId, Integer siteId) {
        List<Object[]> resultList = new ArrayList<>();

        // Отримуємо всі розклади для обробки
        List<Schedule> schedules = scheduleRepository.findAll();

        for (Schedule schedule : schedules) {
            // Перевіряємо умови для будівельного управління або ділянки
            boolean matchesManagement = managementId != null && managementId.equals(schedule.getSite().getBuildingManagement().getId());
            boolean matchesSite = siteId != null && siteId.equals(schedule.getSite().getId());

            // Додаємо до результату, якщо хоча б один з параметрів відповідає
            if (matchesManagement || matchesSite) {
                resultList.add(new Object[]{
                        schedule.getProject().getName(),               // Назва проекту
                        schedule.getSite().getName(),                  // Назва ділянки
                        schedule.getSite().getBuildingManagement().getName(),  // Назва управління
                        schedule.getStartDate(),                       // Дата початку
                        schedule.getEndDate()                          // Дата закінчення
                });
            }
        }
        return resultList;
    }

    public List<Object[]> findAllSchedulesWithDelayedCompletion(Integer siteId, Integer managementId, Boolean organizationFlag) {
        Set<Object[]> resultSet = new HashSet<>();

        List<Schedule> schedules = scheduleRepository.findAll();

        for (Schedule schedule : schedules) {
            List<Report> reports = reportRepository.findByProjectAndWorkType(schedule.getProject(), schedule.getWorkType());

            for (Report report : reports) {
                LocalDate reportCompletionDate = report.getCompletionDate();
                LocalDate scheduleEndDate = schedule.getEndDate();

                if (reportCompletionDate != null && scheduleEndDate != null && reportCompletionDate.isAfter(scheduleEndDate)) {
                    boolean matchesSite = true;
                    boolean matchesManagement = true;

                    if (siteId != null) {
                        matchesSite = (schedule.getSite().getId() == siteId);
                    }

                    if (managementId != null) {
                        matchesManagement = (schedule.getSite().getBuildingManagement().getId() == managementId);
                    }

                    if (organizationFlag != null && organizationFlag) {
                        resultSet.add(new Object[]{
                                schedule.getWorkType().getName(),
                                schedule.getSite().getId(),
                                schedule.getSite().getBuildingManagement().getName(),
                                schedule.getProject().getName(),
                                scheduleEndDate,
                                reportCompletionDate
                        });
                        break;
                    }

                    if (matchesSite || matchesManagement) {
                        resultSet.add(new Object[]{
                                schedule.getWorkType().getName(),
                                schedule.getSite().getId(),
                                schedule.getSite().getBuildingManagement().getName(),
                                schedule.getProject().getName(),
                                scheduleEndDate,
                                reportCompletionDate
                        });
                        break;
                    }
                }
            }
        }

        return new ArrayList<>(resultSet);
    }

    public Schedule findScheduleById(int id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public void updateScheduleById(int id, Schedule schedule) {
        Schedule existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Розклад з ID " + id + " не знайдено."));

        // Оновлюємо поля об'єкта existingSchedule значеннями з параметра schedule
        existingSchedule.setStartDate(schedule.getStartDate());
        existingSchedule.setEndDate(schedule.getEndDate());
        existingSchedule.setProject(schedule.getProject());
        existingSchedule.setWorkType(schedule.getWorkType());
        existingSchedule.setBrigade(schedule.getBrigade());
        existingSchedule.setSite(schedule.getSite());

        // Зберігаємо оновлений об'єкт
        scheduleRepository.save(existingSchedule);
    }

}
