package magic.management.repository;

import magic.management.entity.Student; // 引入 Student 类
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUserStudentNo(String studentNo);
}