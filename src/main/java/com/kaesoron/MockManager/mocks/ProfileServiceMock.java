package com.kaesoron.MockManager.mocks;

import com.kaesoron.MockManager.model.Profile;
import com.kaesoron.MockManager.exception.ProfileNotFoundException;
import org.springframework.stereotype.Service;
import com.kaesoron.MockManager.logic.ProfileService;

@Service
public class ProfileServiceMock implements ProfileService {

    @Override
    public Profile getProfile(int personId) {
//        Mocking database response
        if (personId == 123) {
            return new Profile(
                    personId,
                    "Иван",
                    "Иванов"
            );
        } else {
            throw new ProfileNotFoundException(personId);
        }
    }
}
