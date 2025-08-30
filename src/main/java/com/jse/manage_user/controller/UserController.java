package com.jse.manage_user.controller;

import com.jse.manage_user.model.request.UserRequest;
import com.jse.manage_user.model.response.MessageResponse;
import com.jse.manage_user.model.response.UserResponse;
import com.jse.manage_user.service.UserService;
import com.jse.manage_user.validator.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Users", description = "User management APIs")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    UserController(UserService userService, UserValidator userValidator){
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @Operation(
            summary = "Retrieve all users",
            description = "Get all the users. The response is a list of users, description and published status.",
            tags = { "Users" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserRequest.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("")
    ResponseEntity<?> getAllUsers(){
        Set<UserResponse> users =  userService.getAllUsers();
        if(users.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @Operation(
            summary = "Retrieve an user by email",
            description = "Get an user by email. The response is an existing user, description and published status.",
            tags = { "Users" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserRequest.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{email}")
    ResponseEntity<?> findUserByEmail(@PathVariable String email){
        UserResponse user = userService.getUserByEmail(email);
        if(user == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }


    @Operation(
            summary = "Persist an user",
            description = "Persist a new user. The response is a new user, description and published status.",
            tags = { "Users" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = UserRequest.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/signup")
    ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest request, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toSet()));
        }
        if(userService.existsEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Email already exists!"));
        }
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }
}
