package in.ashokit.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import in.ashokit.bindings.User;
@Service
public interface RegistrationService {
	
	public boolean uniqueEmail(String email);
	public Map<Integer, String> getCountry();
	public Map<Integer, String> getState(Integer countryId);
	public Map<Integer, String> getCity(Integer stateId);
	public boolean registerUser(User user);
	

}
