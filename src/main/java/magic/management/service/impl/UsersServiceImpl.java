package magic.management.service.impl;

import magic.management.entity.Users;
import magic.management.repository.UsersRepository;
import magic.management.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "users")  // 统一前缀，默认缓存名 "users"
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    // 新增/保存：返回的对象会放入缓存（如果有 id）
    @Override
    @Caching(
            put = @CachePut(key = "#user.id"),
            cacheable = @Cacheable(key = "#result.studentNo")
    )
    public Users saveUser(Users user) {
        return usersRepository.save(user);
    }

    // 根据 ID 查询
    @Override
    @Cacheable(key = "#id")
    public Users getUserById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    // 查询全部
    @Override
    @Cacheable(cacheNames = "usersAll")
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    // 更新：同时更新缓存
    @Override
    @Caching(
            put = {@CachePut(key = "#id"), @CachePut(key = "#user.studentNo")},
            evict = @CacheEvict(cacheNames = "usersAll", allEntries = true)  // 更新后列表缓存失效
    )
    public Users updateUser(Long id, Users user) {
        Users existingUser = getUserById(id);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setStudentNo(user.getStudentNo());
            // 其他字段同理...
            return usersRepository.save(existingUser);
        }
        return null;
    }

    // 删除：清除相关缓存
    @Override
    @Caching(evict = {
            @CacheEvict(key = "#id"),                                   // 清除 id 缓存
            @CacheEvict(cacheNames = "usersAll", allEntries = true)     // 清除列表缓存
    })
    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }

    // 根据名字查询（假设名字可能重复，取第一个）
    @Override
    @Cacheable(cacheNames = "usersByName", key = "#name")
    public Users findByName(String name) {
        List<Users> users = usersRepository.findByName(name);
        return users.isEmpty() ? null : users.getFirst();
    }
}