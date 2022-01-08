package in.ashokit.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.bindings.User;
import in.ashokit.service.RegistrationService;

@RestController
public class RegistrationControl {
	@Autowired
	private RegistrationService service;

	@GetMapping("/emailcheck/{email}")
	public String emailCheck(@PathVariable String email) {
		boolean uniqueEmail = service.uniqueEmail(email);
		if (uniqueEmail) {
			return "unique";
		} else
			return "Duplicate";
	}

	@GetMapping("/selectCountry")
	public Map<Integer, String> getCountry() {
		return service.getCountry();
	}

	@GetMapping("/selectState/{countryId}")
	public Map<Integer, String> getState(@PathVariable Integer countryId) {
		Map<Integer, String> state = service.getState(countryId);
		return state;
	}
	@GetMapping("/selectCity/{stateId}")
	public Map<Integer , String> getCity(@PathVariable Integer stateId){
		Map<Integer, String> city = service.getCity(stateId);
		return city;
	}
	@PostMapping("/Register")
	public String registerUser(@RequestBody User user) {
		boolean registerUser = service.registerUser(user);
		if(registerUser) {
			return "Success";
		}else
			return "Duplicate Entry";
	}
	

}
