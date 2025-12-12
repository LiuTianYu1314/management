package magic.management.service.impl;

import magic.management.entity.Student;
import magic.management.repository.StudentRepository;
import magic.management.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "student")
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // 根据 ID 查询：缓存 key 为 id
    @Override
    @Cacheable(key = "#id")
    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // 根据学号查询：缓存 key 为 studentNo
    @Override
    @Cacheable(cacheNames = "studentByNo", key = "#studentNo")
    public Student findByStudentNo(String studentNo) {
        return studentRepository.findByUserStudentNo(studentNo);
    }

    // 查询全部：单独一个缓存名（因为是列表，更新时容易清理）
    @Override
    @Cacheable(cacheNames = "studentAll")
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    // 如果你后续有新增/更新/删除 Student 的方法，可以这样加：
    // 示例（如果你以后实现 save/update/delete）：
    // @CachePut(key = "#student.id")  // 更新单个缓存
    // @CacheEvict(cacheNames = "studentAll", allEntries = true)  // 清除列表缓存
    // public Student save(Student student) { ... }

    // @CacheEvict(allEntries = true)  // 删除后清除所有相关 student 缓存
    // public void deleteById(Long id) { ... }
}
