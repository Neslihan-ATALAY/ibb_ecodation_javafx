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
    private Integer count;

    public UserDTO(Integer id, String username, String password, String email, ERole role, Integer count) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
	this.count = count;
    }
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public ERole getERole() {
		return role;
	}
	
	public void setERole(String role) {
		this.role = role;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
}
