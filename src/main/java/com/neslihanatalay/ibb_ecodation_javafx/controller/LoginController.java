package com.neslihanatalay.ibb_ecodation_javafx.controller;

import com.neslihanatalay.ibb_ecodation_javafx.dao.ResourceBundleBinding;
import com.neslihanatalay.ibb_ecodation_javafx.dao.UserDAO;
import com.neslihanatalay.ibb_ecodation_javafx.dto.UserDTO;
import com.neslihanatalay.ibb_ecodation_javafx.utils.ERole;
import com.neslihanatalay.ibb_ecodation_javafx.utils.FXMLPath;
import com.neslihanatalay.ibb_ecodation_javafx.utils.SceneHelper;
import com.neslihanatalay.ibb_ecodation_javafx.utils.SpecialColor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginController {
    private final UserDAO userDAO;
	private final ResourceBundleBinding resourceBundleBinding;

    public LoginController() {
        userDAO = new UserDAO();
		resourceBundleBinding = new ResourceBundleBinding();
    }

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
	@FXML private ComboBox<Locale> languageSelectComboBox;
	
	private static final String RESOURCE_NAME = Resources.class.getTypeName();
	
	private static final ObservableResourceFactory RESOURCE_FACTORY = new ObservableResourceFactory();
	
	static {
		RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(RESOURCE_NAME));
	}
	
	@FXML
    public void initialize() {		
		languageSelectComboBox = new ComboBox<>();
		languageSelectComboBox.getItems().add(null);
        languageSelectComboBox.getItems().addAll(Locale.ENGLISH, Locale.TURKISH);
        languageSelectComboBox.setValue(Locale.TURKISH);
		languageSelectComboBox.setCellFactory(lv -> new LocaleCell());
		languageSelectComboBox.setButtonCell(new LocaleCell());
		
		//languageSelectComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
		languageSelectComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(RESOURCE_NAME, newValue));
			}
		});
	}
	
	public static class LocaleCell extends ListCell<Locale> {
		@Override
		public void updateItem(Locale locale, boolean empty) {
			super.updateItem(locale, empty);
			if (empty) {
				setText(null);
			} else {
				setText(locale.getDisplayLanguage(locale));
			}
		}
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
            login();
        }
    }

    @FXML
    public void login(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        Optional<UserDTO> optionalLoginUserDTO = userDAO.loginUser(username, password);
        if (optionalLoginUserDTO.isPresent()) {
            UserDTO userDTO = optionalLoginUserDTO.get();
            //showAlert("Bilgi", "Sisteme Giriş Başarılı " + userDTO.getUsername(), Alert.AlertType.INFORMATION);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("bilgi"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("girişbaşarılı") + userDTO.getUsername(), Alert.AlertType.INFORMATION);
            if (userDTO.getRole() == ERole.ADMIN) {
                openAdminPane(userDTO.getId());
            } else {
                openUserHomePane(userDTO.getId());
            }
        } else {
            //showAlert("Hata", "Giriş bilgileri hatalı", Alert.AlertType.ERROR);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("girişbaşarısız"), Alert.AlertType.ERROR);
        }
    }

    private void openUserHomePane(Integer userId) {
        try {
			//if (languageSelectComboBox.getValue() == Locale.TURKISH)
			if (languageSelectComboBox.getValue().equals(Locale.TURKISH)) {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.USER_HOME_TR + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + userId.toString()));
			} else if (languageSelectComboBox.getValue().equals(Locale.ENGLISH)) {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.USER_HOME_EN + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + userId.toString()));
			}
            //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.USER_HOME + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + userId.toString()));
            Parent parent = fxmlLoader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(parent));
            //stage.setTitle("Kullanıcı Paneli");
			stage.setTitle(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıpanel"));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            //showAlert("Hata", "Kullanıcı ekranı yüklenemedi", Alert.AlertType.ERROR);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hatakullanıcıanasayfa"), Alert.AlertType.ERROR);
        }
    }

    private void openAdminPane(Integer userId) {
        try {
			//if (languageSelectComboBox.getValue() == Locale.TURKISH)
			if (languageSelectComboBox.getValue().equals(Locale.TURKISH)) {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.ADMIN_TR + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + userId.toString()));
			} else if (languageSelectComboBox.getValue().equals(Locale.ENGLISH)) {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.ADMIN_EN + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + userId.toString()));
			}
            //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.ADMIN + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + userId.toString()));
            Parent parent = fxmlLoader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(parent));
            //stage.setTitle("Yönetici Panel");
			stage.setTitle(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("yöneticipanel"));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            //showAlert("Hata", "Yönetici sayfası ekranı yüklenemedi", Alert.AlertType.ERROR);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hatayöneticisayfası"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void switchToRegister(ActionEvent actionEvent) {
        try {
            // 1.YOL
            /*
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.REGISTER));
            Parent parent = fxmlLoader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Kayıt Ol");
            stage.show();
             */
            // 2.YOL
            //SceneHelper.switchScene(FXMLPath.REGISTER, usernameField, "Kayıt Ol");
			//////////////////////////////////////////////////////////
			//if (languageSelectComboBox.getValue() == Locale.TURKISH)
			if (languageSelectComboBox.getValue().equals(Locale.TURKISH)) {
				SceneHelper.switchScene(FXMLPath.REGISTER_TR + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + getLoginUserId().toString(), usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıkayıt"));
			} else if (languageSelectComboBox.getValue().equals(Locale.ENGLISH)) {
				SceneHelper.switchScene(FXMLPath.REGISTER_EN + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + getLoginUserId().toString(), usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıkayıt"));
			}
			//SceneHelper.switchScene(FXMLPath.REGISTER, usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıkayıt"));
        } catch (Exception e) {
            e.printStackTrace();
            //showAlert("Hata", "Kayıt ekranı yüklenemedi", Alert.AlertType.ERROR);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hatakayıtsayfası"), Alert.AlertType.ERROR);
        }
    }
}
