package in.ashokit.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.bindings.User;
import in.ashokit.entities.CityEntity;
import in.ashokit.entities.CountryEntity;
import in.ashokit.entities.StateEntity;
import in.ashokit.entities.UserEntity;
import in.ashokit.repositories.CityRepo;
import in.ashokit.repositories.CountryRepo;
import in.ashokit.repositories.StateRepo;
import in.ashokit.repositories.UserRepo;
import in.ashokit.utils.Emailutils;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CountryRepo countryRepo;

	@Autowired
	private StateRepo stateRepo;

	@Autowired
	private CityRepo cityRepo;
	@Autowired
	private Emailutils emailutils;

	@Override
	public boolean uniqueEmail(String email) {
		String findByEmail = userRepo.findByUserEmail(email);
		if (findByEmail != null) {
			return true;

		} else
			return false;
	}

	@Override
	public Map<Integer, String> getCountry() {
		List<CountryEntity> findAll = countryRepo.findAll();
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (CountryEntity country : findAll) {
			map.put(country.getCountryId(), country.getCountryName());

		}
		return map;
	}

	@Override
	public Map<Integer, String> getState(Integer countryId) {
		List<StateEntity> list = stateRepo.findByCountryId(countryId);
		Map<Integer, String> state = new HashMap<Integer, String>();
		for (StateEntity st : list) {
			state.put(st.getStateId(), st.getStateName());
		}
		return state;
	}

	@Override
	public Map<Integer, String> getCity(Integer stateId) {
		List<CityEntity> findById = cityRepo.findByStateId(stateId);
		Map<Integer, String> city = new HashMap<Integer, String>();
		for (CityEntity ce : findById) {
			city.put(ce.getCityId(), ce.getCityName());
		}
		return city;
	}

	@Override
	public boolean registerUser(User user) {
		user.setUserPwd(generateTempPwd());
		user.setUserAccStatus("LOCKED");
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		UserEntity save = userRepo.save(userEntity);
		if (save.getUserId() != null) {
			return sendRegEmail(user);
		}

		return false;
	}

	private String generateTempPwd() {
		String tempPwd = null;
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 6;
		Random random = new Random();

		tempPwd = random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return tempPwd;
	}

	private boolean sendRegEmail(User user) {

		return emailutils.sendEmail("Unlock Account", mailBody(user), user.getUserEmail());

	}

	private String mailBody(User user) {
		String mailbody = "";
		try {
			File file = new File("USER_MAIL_BODY.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			while (br.ready()) {

				String readLine = br.readLine();
				readLine = readLine.replace("{FNAME}", user.getUserFirstName());
				readLine = readLine.replace("{TEMP-PWD}", user.getUserPwd());
				readLine = readLine.replace("{EMAIL}", user.getUserEmail());

				mailbody += readLine;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailbody;

	}

}
