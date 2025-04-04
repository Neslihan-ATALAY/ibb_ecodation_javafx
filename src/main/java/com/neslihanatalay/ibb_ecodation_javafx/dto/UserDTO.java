package com.neslihanatalay.ibb_ecodation_javafx.dto;

import com.neslihanatalay.ibb_ecodation_javafx.utils.ERole;
import lombok.*;

@Getter
@Setter
//@AllArgsConstructor 
@NoArgsConstructor
@ToString
@Builder

public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private ERole role;

    public UserDTO(Integer id, String username, String password, String email, ERole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /*
    public static void main(String[] args) {
        UserDTO userDTO= UserDTO.builder()
                .id(0)
                .username("neslihan")
                .email("atalay.neslihan.2015@gmail.com")
                .password("root")
                .build();

        System.out.println(userDTO);
    }
    */
}
