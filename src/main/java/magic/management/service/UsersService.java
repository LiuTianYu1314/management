package magic.management.service;

import magic.management.entity.Users;
import java.util.List;

public interface UsersService {
    Users saveUser(Users user);
    Users getUserById(Long id);
    List<Users> getAllUsers();
    Users updateUser(Long id, Users user);
    void deleteUser(Long id);
    Users findByName(String name);
}