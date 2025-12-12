package magic.management.service;

import magic.management.entity.Student;
import magic.management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StudentService {
    Student findById(Long id);
    Student findByStudentNo(String studentNo);
    List<Student> findAll();
}
