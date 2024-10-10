package com.example.My_Course_Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;


import com.example.My_Course_Project.service.ProjectService;
@SpringBootApplication
public class MyCourseProjectApplication {

	@Autowired
	private ProjectService projectService;

	public static void main(String[] args) {
		SpringApplication.run(MyCourseProjectApplication.class, args);
	}
}
