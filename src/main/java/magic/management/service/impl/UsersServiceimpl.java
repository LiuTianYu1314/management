package magic.management.service.impl;

import magic.management.entity.Users;
import magic.management.repository.UsersRepository;
import magic.management.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceimpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Users saveUser(Users user) {
        return usersRepository.save(user);
    }

    @Override
    public Users getUserById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users updateUser(Long id, Users user) {
        Users existingUser = getUserById(id);
        if (existingUser != null) {
            // 更新字段
            existingUser.setName(user.getName());
            existingUser.setStudentNo(user.getStudentNo());
            // 可根据需要添加其他字段
            return usersRepository.save(existingUser);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }

    @Override
    public Users findByName(String name) {
        List<Users> users = usersRepository.findByName(name);
        return users.isEmpty() ? null : users.getFirst();
    }
}