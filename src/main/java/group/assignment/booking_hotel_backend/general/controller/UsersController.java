package group.assignment.booking_hotel_backend.general.controller;

import group.assignment.booking_hotel_backend.general.dto.UsersDto;
import group.assignment.booking_hotel_backend.general.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public String hello() {
        return "Hello, World!";
    }

}