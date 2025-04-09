package com.neslihanatalay.ibb_ecodation_javafx;

import com.neslihanatalay.ibb_ecodation_javafx.dao.UserDAO;
import com.neslihanatalay.ibb_ecodation_javafx.dto.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.commons.*
import org.apache.commons.httpclient.*;

public class HelloController {
    private UserDAO userDAO;

    public HelloController() {
        userDAO = new UserDAO();
    }

    @FXML private Label welcomeText;
    @FXML private Label LoginUserIdLabelField;
    @FXML private Label welcomeLabel;
    private static Integer loginUserId;
	
    public static Integer getLoginUserId() { return loginUserId; }
    public static void setLoginUserId(Integer loginUserId) { this.loginUserId = loginUserId; }
	
    @FXML
    public void initialize() {
	// GİRİŞ YAPAN KULLANICININ ID NUMARASI LoginUserIdLabelField'e KAYDEDİLİR
	//LoginUserIdLabelField.setText(Integer.valueOf(request.getParameter("user")));
	setLoginUserId(Integer.valueOf(Request["user"].toString()));
	LoginUserIdLabelField.setText(Request["user"].toString());
	UserDTO userDTO = userDAO.findById(getLoginUserId());
	if (userDTO.isPresent()) {
	    welcomeLabel.setText("Merhaba " + userDTO.getUsername() + ", Hoşgeldiniz");
	}
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
