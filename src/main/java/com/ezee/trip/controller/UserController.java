package com.ezee.trip.controller;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ezee.trip.controller.io.UserIO;
import com.ezee.trip.controller.io.ResponseIO;
import com.ezee.trip.dto.UserDTO;
import com.ezee.trip.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseIO<List<UserIO>> getAllUser(@RequestHeader("authCode") String authCode) {
        List<UserDTO> allUsers = userService.getAllUser(authCode);
        List<UserIO> userList = new ArrayList<>();
        for (UserDTO userDTO : allUsers) {
            userList.add(convertToIO(userDTO));
        }
        return ResponseIO.success(userList);
    }

    @GetMapping("/{code}")
    public ResponseIO<UserIO> getUserByCode(@PathVariable("code") String code,
                                            @RequestHeader("authCode") String authCode) {
        UserDTO userDTO = userService.getUserByCode(code, authCode);
        UserIO userIO = convertToIO(userDTO);
        return ResponseIO.success(userIO);
    }

    @PostMapping("/add")
    public ResponseIO<String> addUser(@RequestBody UserIO userIO,
                                      @RequestHeader("authCode") String authCode) {
        UserDTO dto = convertToDTO(userIO);
        userService.addUser(dto, authCode);
        return ResponseIO.success("Inserted Successfully");
    }

//    @PostMapping("/{code}/update")
//    public ResponseIO<String> updateUser(@PathVariable("code") String code,
//                                         @RequestBody Map<String, Object> updates,
//                                         @RequestHeader("authCode") String authCode) {
//        UserDTO dto = new UserDTO();
//        dto.setCode(code);
//        userService.updateUser(updates, dto, authCode);
//        return ResponseIO.success("Updated Successfully");
//    }

    private UserIO convertToIO(UserDTO userDTO) {
        UserIO userIO = new UserIO();
        userIO.setCode(userDTO.getCode());
        userIO.setName(userDTO.getName());
        userIO.setActiveFlag(userDTO.getActiveFlag());
        userIO.setPhoneNo(userDTO.getPhoneNo());
        userIO.setEmail(userDTO.getEmail());
        userIO.setGender(userDTO.getGender());
        userIO.setDateOfBirth(userDTO.getDateOfBirth());
        userIO.setAddress(userDTO.getAddress());
        return userIO;
    }

    private UserDTO convertToDTO(UserIO userIO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setCode(userIO.getCode());
        userDTO.setName(userIO.getName());
        userDTO.setActiveFlag(userIO.getActiveFlag());
        userDTO.setPhoneNo(userIO.getPhoneNo());
        userDTO.setEmail(userIO.getEmail());
        userDTO.setGender(userIO.getGender());
        userDTO.setDateOfBirth(userIO.getDateOfBirth());
        userDTO.setAddress(userIO.getAddress());
        return userDTO;
    }
}

