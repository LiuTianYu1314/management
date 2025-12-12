package magic.management.service.impl;

import magic.management.entity.Student;
import magic.management.repository.StudentRepository;
import magic.management.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class studentServiceimpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override //根据id查询
    public Student findById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    @Override //根据学号查询
    public Student findByStudentNo(String studentNo) {
        return studentRepository.findByUserStudentNo(studentNo);
    }

    @Override //查询全部
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}
