package jr.acens.api.service.impl;

import jakarta.transaction.Transactional;
import jr.acens.api.domain.user.DTO.*;
import jr.acens.api.domain.user.UseCase.*;
import jr.acens.api.domain.user.UserProfile;
import jr.acens.api.domain.user.admin.useCase.CreateAdminUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jr.acens.api.infra.security.TokenJwtDto;
import jr.acens.api.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    //PROCESSO DE LOGIN
    @Autowired
    private ForgotPasswordUseCase forgotPasswordUseCase;
    @Autowired
    private PerformLoginUseCase performLoginUseCase;
    @Autowired
    private ResetPasswordUseCase resetPasswordUseCase;

    //CRUD USER
    @Autowired
    private CreateUserUseCase createUserUseCase;
    @Autowired
    private DeleteUserUseCase deleteUserUseCase;
    @Autowired
    private ListUserByIdUseCase listUserByIdUseCase;
    @Autowired
    private ListAllUserUseCase listAllUserUseCase;
    @Autowired
    private ListUsersByProfile listUsersByProfile;
    @Autowired
    private UpdateUserUseCase updateUserUseCase;


    //CRUD ADMIN
    @Autowired
    private CreateAdminUseCase createAdminUseCase;

    @Override
    public TokenJwtDto performLogin(UserLoginDTO data) {
        var tokenJwt = performLoginUseCase.performLogin(data);
        return tokenJwt;
    }

    @Override
    public UserReturnLoginDTO createUser(UserDTO data) {
        var user = createUserUseCase.createUser(data);
        return user;
    }

    @Override
    public String forgotPassword(UserLoginOnlyDTO data) {
        forgotPasswordUseCase.forgotPassword(data);
        return "Email successfully sent!";
    }

    @Override
    public String resetPassword(UserResetPassDTO data) {
        resetPasswordUseCase.resetPassword(data);
        return "Password successfully updated!";
    }

    @Override
    public UserReturnListDTO updateUser(UserUpdateDTO data, Long id) {
        var ret = updateUserUseCase.updateUser(data, id);
        return ret;
    }

    @Override
    public UserReturnListDTO listUserById(Long id) {
        var user = listUserByIdUseCase.listUserById(id);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        deleteUserUseCase.deleteUser(id);
    }

    @Override
    public Page<UserReturnListDTO> listAllUser(Pageable pageable) {
        var page = listAllUserUseCase.listAllUsers(pageable);
        return page;
    }

    @Override
    public Page<UserReturnListDTO> listUsersByProfile(Pageable pageable, String profile) {
        var page = listUsersByProfile.listUsersByProfile(pageable, profile);
        return page;
    }

    @Override
    public UserReturnLoginDTO createAdmin(UserDTO data) {
        var admin = createAdminUseCase.createAdmin(data);
        return admin;
    }
}
