package com.tech.center;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
	
	@Autowired
	private ProfileRepository repository;
	
	public List<Profile> findAllProfiles(){
		return repository.findAll();
	}

	public Profile findProfileByUserId(long userId){
		return repository.findProfileByUserId(userId);
	}

	public void saveProfile(Profile profile) {
		repository.save(profile);
	}

}
