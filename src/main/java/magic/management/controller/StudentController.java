package magic.management.controller;




import magic.management.entity.Student;
import magic.management.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
