package com.tech.center;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository repository; //final?
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ProfileRepository repositoryProfile;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByUsername(username);
		UserDetailsPrincipal userDetailsPrincipal = new UserDetailsPrincipal(user);
		return userDetailsPrincipal;
	}
	
		// default output
	public List<User> findAllUsers(){
		return repository.findAll();
	}
	public Optional<User> findUserById(long userId){
		return repository.findById(userId);
	}
		// default input
	public void saveUser(User user) {
		repository.save(user);
	}
	
	public void saveUserAddProfile(User user) {
		Role role = roleRepository.getDefaultRole("CLIENT");
		user.setRole(role);
		repository.save(user);
		
		//user = repository.authorizeUser(user.getUsername(), user.getPassword());
		
		Profile profile = new Profile();
		profile.setUserId(user.getUserId());
		repositoryProfile.save(profile);
	}
	
	
	
	// sorting
	public List<User> findUsersWithSorting(String field, String type){
		switch(type.toString()) {
			case "ASC":
				return repository.findAll(Sort.by(Sort.Direction.ASC, field));
			case "DESC":
				return repository.findAll(Sort.by(Sort.Direction.DESC, field));
		}
		return repository.findAll();
	}
	
	public User addUser(User user) {
		return repository.save(user);
	}
	
		// authorization
	public User authorize(String username, String password){
		
		return repository.authorizeUser(username, password);
	}

}
