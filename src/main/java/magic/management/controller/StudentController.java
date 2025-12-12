package magic.management.controller;


import magic.management.entity.Student;
import magic.management.service.StudentService;
import org.hibernate.annotations.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping("/{id}") //根据id查询
    public Student getStudentById(@PathVariable Long id){
        return studentService.findById(id);
    }

    @GetMapping("/no/{studentNo}") //根据学号查询
    public Student getStudentNo(@PathVariable String studentNo){
        return studentService.findByStudentNo(studentNo);
    }

    @GetMapping //查询所有
    public List<Student> getAllStudent(){
        return studentService.findAll();
    }
}
