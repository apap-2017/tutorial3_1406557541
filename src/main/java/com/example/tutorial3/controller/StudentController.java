package com.example.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tutorial3.model.StudentModel;
import com.example.tutorial3.service.InMemoryStudentService;
import com.example.tutorial3.service.StudentService;

@Controller
public class StudentController {
	private final StudentService studentService;

	public StudentController() {
		studentService = new InMemoryStudentService();
	}

	@RequestMapping("/student/add")
	public String add(@RequestParam(value = "npm", required = true) String npm,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "gpa", required = true) double gpa) {
		StudentModel student = new StudentModel(npm, name, gpa);
		studentService.addStudent(student);
		return "add";
	}

	@RequestMapping("/student/view")
	public String view(Model model, @RequestParam(value = "npm", required = true) String npm) {
		StudentModel student = studentService.selectStudent(npm);
		model.addAttribute("student", student);
		return "view";
	}

	@RequestMapping("/student/viewall")
	public String viewAll(Model model) {
		List<StudentModel> students = studentService.selectAllStudents();
		model.addAttribute("students", students);
		return "viewall";
	}

	/*
	 * Method viewStudent merupakan method yang berfungsi untuk melihat data
	 * student berdasarkan npm. Pada method ini, akan dicek apakah npm sudah
	 * diberikan dan student dengan npm tersedia. Apabila npm tersedia, data student
	 * akan ditampilkan, namun apabila npm tidak diberikan atau tidak tersedia,
	 * akan ditampilan error page.
	 */
	@RequestMapping(value = { "/student/view", "student/view/{npm}" })
	public String viewStudent(@PathVariable Optional<String> npm, Model model) {
		if (npm.isPresent()) {
			if (studentService.selectStudent(npm.get()) == null) {
				model.addAttribute("errormessage", "NPM tidak ditemukan, mohon cek kembali NPM.");
				return "error";
			} else {
				StudentModel student = studentService.selectStudent(npm.get());
				model.addAttribute("student", student);
				return "view";
			}
		} else {
			model.addAttribute("errormessage", "NPM kosong, mohon berikan NPM.");
			return "error";
		}
	}

	/*
	 * Method delete merupakan method yang berfungsi untuk menghapus data
	 * student berdasarkan npm. Pada method ini, akan dicek apakah npm sudah
	 * diberikan dan student dengan npm tersedia. Apabila npm tersedia, data student
	 * akan dihapus, namun apabila npm tidak diberikan atau tidak tersedia,
	 * akan ditampilan error page.
	 */
	@RequestMapping(value = { "/student/delete", "student/delete/{npm}" })
	public String delete(@PathVariable Optional<String> npm, Model model) {
		if (npm.isPresent()) {
			if (studentService.selectStudent(npm.get()) == null) {
				model.addAttribute("errormessage", "NPM tidak ditemukan, mohon cek kembali NPM.");
				return "error";
			} else {
				studentService.deleteStudent(npm.get());
				model.addAttribute("npm", npm.get());
				return "delete";
			}
		} else {
			model.addAttribute("errormessage", "NPM kosong, mohon berikan NPM.");
			return "error";
		}
	}
}
