package com.neslihanatalay.ibb_ecodation_javafx.controller;

import com.neslihanatalay.ibb_ecodation_javafx.dao.UserDAO;
import com.neslihanatalay.ibb_ecodation_javafx.dto.UserDTO;
import com.neslihanatalay.ibb_ecodation_javafx.utils.ERole;
import com.neslihanatalay.ibb_ecodation_javafx.utils.FXMLPath;
import com.neslihanatalay.ibb_ecodation_javafx.utils.SceneHelper;
import com.neslihanatalay.ibb_ecodation_javafx.utils.SpecialColor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.*
import org.apache.commons.httpclient.*;

import java.util.Optional;

public class RegisterController {
    private UserDAO userDAO;

    public RegisterController() {
        userDAO = new UserDAO();
    }

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    
	private Integer LoginUserIdLabelField = 0;
	
	@FXML
    public void initialize() {
		LoginUserIdLabelField = Integer.valueOf(Request["LoginUserIdLabelField"]);
	}

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void specialOnEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            register();
        }
    }

    @FXML
    public void register() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Hata", "Lütfen tüm alanları doldurun", Alert.AlertType.ERROR);
            return;
        }

        if (userDAO.isUsernameExists(username)) {
            showAlert("Hata", "Bu kullanıcı adı zaten kayıtlı!", Alert.AlertType.WARNING);
            return;
        }

        if (userDAO.isEmailExists(email)) {
            showAlert("Hata", "Bu e-posta adresi zaten kayıtlı!", Alert.AlertType.WARNING);
            return;
        }

        UserDTO userDTO = UserDTO.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(ERole.USER)
                .build();

        Optional<UserDTO> createdUser = userDAO.create(userDTO);
        if (createdUser.isPresent()) {
            showAlert("Başarılı", "Kayıt başarılı", Alert.AlertType.INFORMATION);
            switchToLoginPane();
        } else {
            showAlert("Hata", "Kayıt başarısız oldu", Alert.AlertType.ERROR);
        }
    }
	
	private UserDTO showProfileForm(UserDTO existing) {
		Dialog<UserDTO> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Yeni Kullanıcı Ekle" : "Kullanıcı Güncelle");

        TextField usernameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
		PasswordField passwordAgainField = new PasswordField();
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll(ERole.values());
        roleCombo.setValue(ERole.USER);

        if (existing != null) {
            usernameField.setText(String.valueOf(existing.getUsername()));
            emailField.setText(String.valueOf(existing.getEmail()));
            passwordField.setText(existing.getPassword());
			passwordAgainField.setText(existing.getPassword());
            roleCombo.setValue(existing.getERole());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, new Label("Kullanıcı Ad:"), usernameField);
        grid.addRow(1, new Label("Email:"), emailField);
        grid.addRow(2, new Label("Yeni Şifre:"), passwordField);
		grid.addRow(3, new Label("Yeni Şifre Tekrar:"), passwordAgainField);
        grid.addRow(4, new Label("Kullanıcı Rol:"), roleCombo);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
					if (passwordField.equals(passwordAgainField)) {
						return UserDTO.builder()
                            .username(String.valueOf(usernameField.getText()))
                            .email(String.valueOf(emailField.getText()))
							///////////////////
                            .password(passwordField.getText())
							.role(ERole.fromString(String.valueOf(roleCombo.getValue())))
                            .build();
					} else {
						showAlert("Uyarı", "Yeni Şifre ve Yeni Şifre Tekrar alanlarını lütfen aynı doldurunuz!", Alert.AlertType.WARNING);
						return;
					}
                } catch (Exception e) {
                    showAlert("Hata", "Geçersiz veri!", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<UserDTO> result = dialog.showAndWait();
        return result.orElse(null);
	}
	
	@FXML
    private void updateProfile() {
		if(LoginUserIdLabelField != 0)
			Integer userId = LoginUserIdLabelField;
			UserDTO selected = userDAO.findById(userId);
			if (selected == null) {
				showAlert("Uyarı", "Güncellenecek bir kayıt seçin.", Alert.AlertType.WARNING);
				return;
			}
			UserDTO updated = showProfileForm(selected);
			if (updated != null && updated.isValid()) {
				userDAO.update(selected.getId(), updated);
				refreshTable();
				showAlert("Başarılı", "KDV kaydı güncellendi.", Alert.AlertType.INFORMATION);
			}
		}
    }
	
	@FXML
    private void showProfile() {
        //ShowProfileDialog dialog = new ShowProfileDialog();
        //Optional<UserDTO> result = dialog.showAndWait();
		private final LabelField usernameField = new LabelField();
		private final LabelField emailField = new LabelField();
		private final LabelField roleField = new LabelField();
		if(LoginUserIdLabelField != 0) {
			Integer userId = LoginUserIdLabelField;
			Dialog<UserDTO> dialog = new Dialog<>();
			dialog.setTitle("Kullanıcı Profili");
			dialog.setHeaderText("Kullanıcı Profil Bilgileri");			
			Optional<UserDTO> optionalUser = userDAO.findById(userId);
			if (optionalUser.isPresent()) {
				usernameField.setText(String.valueOf(optionalUser.getUsername()));
				emailField.setText(String.valueOf(optionalUser.getEmail()));
				roleField.setText(String.valueOf(optionalUser.getRole()));
			}
			GridPane grid = new GridPane();
			grid.setHgap(10); grid.setVgap(10);
			grid.addRow(0, new Label("Kullanıcı Ad:"), usernameField);
			grid.addRow(1 new Label("Kullanıcı Email:"), emailField);
			grid.addRow(2, new Label("Kullanıcı Rol:"), roleField);
			dialog.getDialogPane().setContent(grid);
			dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == ButtonType.OK) {
					showAlert("Bilgi", "Kullanıcı profili görüntülendi.", Alert.AlertType.INFORMATION);
					//switchToUserHomePane();
				}
			}
		}
    }

    @FXML
    private void switchToLoginPane() {
        try {
            //1.YOL
            /*
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.LOGIN));
            Parent parent = fxmlLoader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Giriş Yap");
            stage.show();
             */
            //2.YOL
            SceneHelper.switchScene(FXMLPath.LOGIN, usernameField, "Giriş Yap");
        } catch (Exception e) {
            System.out.println(SpecialColor.RED + "Login Sayfasına yönlendirme başarısız" + SpecialColor.RESET);
            e.printStackTrace();
            showAlert("Hata", "Login ekranı yüklenemedi", Alert.AlertType.ERROR);
        }
    }
	
	@FXML
    private void notebook(ActionEvent event) {
        switchToNotebookPane();
    }
	
	@FXML
    private void switchToNotebookPane() {
        try {
            SceneHelper.switchScene(FXMLPath.NOTEBOOK, usernameField, "NOTLAR");
        } catch (Exception e) {
            System.out.println(SpecialColor.RED + "Not sayfasına yönlendirme başarısız" + SpecialColor.RESET);
            e.printStackTrace();
            showAlert("Hata", "Not ekranı yüklenemedi", Alert.AlertType.ERROR);
        }
    }
	
	@FXML
    private void switchToUserHomePane() {
        try {
            SceneHelper.switchScene(FXMLPath.USER_HOME, usernameField, "KULLANICI ANASAYFA");
        } catch (Exception e) {
            System.out.println(SpecialColor.RED + "Kuallanıcı anasayfasına yönlendirme başarısız" + SpecialColor.RESET);
            e.printStackTrace();
            showAlert("Hata", "Kullanıcı anasayfa ekranı yüklenemedi", Alert.AlertType.ERROR);
        }
    }
}
