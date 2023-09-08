package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.RegistrationDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.repository.ProfileRepository;
import com.example.utility.CheckValidationUtility;
import com.example.utility.MD5Util;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

public class AuthServiceTest {

    private AuthService underTest;
    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private SmsSenderService smsSenderService;
    @Mock
    private MailSenderService mailSenderService;
    @Mock
    private CheckValidationUtility checkValidationUtility;
    @Captor
    private ArgumentCaptor<Integer> customProfileEntityArgumentCapture;
    @Mock
    private ResourceBundleService resourceBundleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new AuthService();
        underTest.setProfileRepository(profileRepository);
        underTest.setSmsSenderService(smsSenderService);
        underTest.setMailSenderService(mailSenderService);
        underTest.setCheckValidationUtility(checkValidationUtility);
        underTest.setMessageSource(resourceBundleService);


    }

    @Test
    public void itShouldRegisterNewProfile() {
        //given
        RegistrationDTO dto = new RegistrationDTO();
        dto.setName("Alish");
        dto.setSurname("Aliyev");
        dto.setEmail("alish@mail.ru");
        dto.setPassword("123456");

        // when
        ApiResponseDTO response = underTest.registrationByEmail(dto, Language.En);
        // then
        Assertions.assertThat(response.isStatus()).isTrue();
    }
    @Test
    public void itShouldNotRegisterNewProfile_existingEmail() {
        //given
        RegistrationDTO dto = new RegistrationDTO();
        dto.setName("Alish");
        dto.setSurname("Aliyev");
        dto.setEmail("alish@mail.ru");
        dto.setPassword("123456");
        //
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setStatus(ProfileStatus.REGISTRATION);
        BDDMockito.given(profileRepository.findByEmailAndVisibleTrue(dto.getEmail())).willReturn(Optional.of(profileEntity));
        // when
        ApiResponseDTO response = underTest.registrationByEmail(dto,Language.En);
        // then
        Assertions.assertThat(response.isStatus()).isTrue();
        BDDMockito.then(profileRepository).should().deleteById(customProfileEntityArgumentCapture.capture());
        Assertions.assertThat(customProfileEntityArgumentCapture.getValue()).isEqualTo(profileEntity.getId());
    }

    @Test
    public void itShouldNotRegisterNewProfile_existingAndActiveProfile() {
        //given
        RegistrationDTO dto = new RegistrationDTO();
        dto.setEmail("alish@mail.ru");
        //
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setStatus(ProfileStatus.ACTIVE);
        BDDMockito.given(profileRepository.findByEmailAndVisibleTrue(dto.getEmail())).willReturn(Optional.of(profileEntity));
        // when
        ApiResponseDTO response = underTest.registrationByEmail(dto,Language.En);
        // then
        Assertions.assertThat(response.isStatus()).isFalse();
//        Assertions.assertThat(response.getMessage()).isEqualTo("Email already exists.");
        BDDMockito.then(mailSenderService).shouldHaveNoInteractions();
        BDDMockito.then(profileRepository).should(BDDMockito.times(1)).findByEmailAndVisibleTrue(BDDMockito.any(String.class));
        BDDMockito.then(profileRepository).should(BDDMockito.times(0)).save(BDDMockito.any(ProfileEntity.class));
    }

    @Test
    public void itShouldAuthorize() {
        //given
        AuthDTO dto = new AuthDTO();
        dto.setPhone("99891");
        dto.setPassword("12345");
        //
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName("Ali");
        profileEntity.setSurname("Aliyev");
        profileEntity.setPhone(dto.getPhone());
        profileEntity.setStatus(ProfileStatus.ACTIVE);
        profileEntity.setVisible(true);
        profileEntity.setPassword(MD5Util.encode(dto.getPassword()));
        profileEntity.setRole(ProfileRole.ROLE_ADMIN);
        BDDMockito.given(profileRepository.findAllByPhoneAndPasswordAndVisibleTrue(dto.getPhone(),MD5Util.encode(dto.getPassword()))).willReturn(Optional.of(profileEntity));
        // when
        ApiResponseDTO response = underTest.login(dto);
        // then
        Assertions.assertThat(response.isStatus()).isTrue();
        ProfileDTO profileDTO = (ProfileDTO) response.getData();

//        Assertions.assertThat(profileDTO.getName()).isEqualTo(profileEntity.getName());
        Assertions.assertThat(profileDTO.getJwt()).isNotBlank();

        Assertions.assertThat(profileDTO)
                .usingRecursiveComparison()
                .comparingOnlyFields("role", "name", "surname", "phone")
                .isEqualTo(profileEntity);
    }

    @Test
    public void itShouldNotAuthorize_wrongPassword() {
        //given
        AuthDTO dto = new AuthDTO();
        dto.setPhone("99891");
        dto.setPassword("12345");
        //
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setPassword("dasdas");
        BDDMockito.given(profileRepository.findByPhoneAndVisibleTrue(dto.getPhone())).willReturn(Optional.of(profileEntity));
        // when
        ApiResponseDTO response = underTest.login(dto);
        // then
        Assertions.assertThat(response.isStatus()).isFalse();
    }
}

