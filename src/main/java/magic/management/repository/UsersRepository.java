package magic.management.repository;

import magic.management.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByStudentNo(String studentNo);
    List<Users> findByName(String name);
}
