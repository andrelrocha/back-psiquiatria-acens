package jr.acens.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jr.acens.api.domain.user.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import jr.acens.api.infra.security.TokenJwtDto;
import jr.acens.api.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity performLogin(@RequestBody @Valid UserLoginDTO data) {
        TokenJwtDto tokenJwt = userService.performLogin(data);
        return ResponseEntity.ok(tokenJwt);
    }

    @PostMapping("/forgot_password")
    @Transactional
    public ResponseEntity forgotPassword(@RequestBody UserLoginOnlyDTO data) {
        var stringSuccess= userService.forgotPassword(data);
        return ResponseEntity.ok(stringSuccess);
    }

    @PostMapping("/reset_password")
    @Transactional
    public ResponseEntity resetPassword(@RequestBody UserResetPassDTO data) {
        var stringSuccess= userService.resetPassword(data);
        return ResponseEntity.ok(stringSuccess);
    }


    @PostMapping("/create")
    @Transactional
    public ResponseEntity createUser(@RequestBody @Valid UserDTO data, UriComponentsBuilder uriBuilder) {
        var newUser = userService.createUser(data);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(newUser.id()).toUri();
        return ResponseEntity.created(uri).body(newUser);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity updateUser(@RequestBody UserUpdateDTO data, @PathVariable Long id) {
        var ret = userService.updateUser(data, id);
        return ResponseEntity.ok(ret);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity listUserById(@PathVariable Long id) {
        var user = userService.listUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/admin/profiles/{profile}")
    @Transactional
    public ResponseEntity listUserByProfile(@PathVariable String profile,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "name") String sortField,
                                            @RequestParam(defaultValue = "asc") String sortOrder) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var pageReturn = userService.listUsersByProfile(pageable, profile);
        return ResponseEntity.ok(pageReturn);
    }

    @GetMapping("/admin/allusers")
    public ResponseEntity<Page<UserReturnListDTO>> listAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "name") String sortField,
                                                                @RequestParam(defaultValue = "asc") String sortOrder) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var pageReturn = userService.listAllUser(pageable);
        return ResponseEntity.ok(pageReturn);
    }

    @PostMapping("/admin/create")
    @Transactional
    public ResponseEntity createAdmin(@RequestBody @Valid UserDTO data) {
        var newAdmin = userService.createAdmin(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAdmin);
    }
}
