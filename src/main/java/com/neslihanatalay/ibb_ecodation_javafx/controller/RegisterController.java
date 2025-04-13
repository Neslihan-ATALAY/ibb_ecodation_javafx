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
    private final UserDAO userDAO;
	private final ResourceBundleBinding resourceBundleBinding;

    public RegisterController() {
        userDAO = new UserDAO();
		resourceBundleBinding = new ResourceBundleBinding();
    }

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField emailField;
	@FXML private ComboBox<ERole> roleField;
	@FXML private ComboBox<Locale> languageSelectComboBox;
	
    @FXML private Label LoginUserIdLabelField;
	private static Integer loginUserId;
	
	public static Integer getLoginUserId() { return loginUserId; }
	public static void setLoginUserId(Integer loginUserId) { this.loginUserId = loginUserId; }
	
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
		
	    roleField.getItems().addAll(ERole.values());
        roleField.setValue(ERole.USER);
		// GİRİŞ YAPAN KULLANICININ ID NUMARASI LoginUserIdLabelField'e KAYDEDİLİR
		setLoginUserId(Integer.valueOf(request().getQueryString("kullanıcı").toString()));
		LoginUserIdLabelField.setText(getLoginUserId().toString());
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
            register();
        }
    }

	//YENİ KULLANICI KAYDETME
    @FXML
    public void register() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();
		String role = String.valueOf(roleField.getValue());

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role.isEmpty()) {
            //showAlert("Hata", "Lütfen tüm alanları doldurun", Alert.AlertType.ERROR);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("tümalanlar"), Alert.AlertType.ERROR);
            return;
        }

        if (userDAO.isUsernameExists(username)) {
            //showAlert("Hata", "Bu kullanıcı adı zaten kayıtlı!", Alert.AlertType.WARNING);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("uyarı"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kayıtlıkullanıcıadı"), Alert.AlertType.WARNING);
            return;
        }

        if (userDAO.isEmailExists(email)) {
            //showAlert("Hata", "Bu e-posta adresi zaten kayıtlı!", Alert.AlertType.WARNING);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("uyarı"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kayıtlıeposta"), Alert.AlertType.WARNING);
            return;
        }

        UserDTO userDTO = UserDTO.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(ERole.fromString(role))
				.count(0)
                .build();

        Optional<UserDTO> createdUser = userDAO.create(userDTO);
        if (createdUser.isPresent()) {
            //showAlert("Bilgi", "Kayıt başarılı", Alert.AlertType.INFORMATION);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("bilgi"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("Kullanıcı kaydedildi."), Alert.AlertType.INFORMATION);
            switchToLoginPane();
        } else {
            //showAlert("Hata", "Kayıt başarısız oldu", Alert.AlertType.ERROR);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("Kullanıcı kaydedilemedi!"), Alert.AlertType.ERROR);
        }
    }
	
	//PROFIL GÜNCELLEME DIALOG
    private UserDTO showProfileForm(UserDTO existing) {
		Dialog<UserDTO> dialog = new Dialog<>();
        //dialog.setTitle(existing == null ? "Yeni Kullanıcı Ekle" : "Kullanıcı Güncelle");
		dialog.setTitle(existing == null ? (resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("yenikullanıcı")) : (resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıgüncelle")));

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
        //grid.addRow(0, new Label("Kullanıcı Ad"), usernameField);
		grid.addRow(0, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıad")), usernameField);
        //grid.addRow(1, new Label("Kullanıcı Email"), emailField);
		grid.addRow(1, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıemail")), emailField);
        //grid.addRow(2, new Label("Yeni Şifre"), passwordField);
		grid.addRow(2, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("yenişifre")), emailField);
		//grid.addRow(3, new Label("Yeni Şifre Tekrar"), passwordAgainField);
		grid.addRow(3, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("yenişifretekrar")), passwordAgainField);
        //grid.addRow(4, new Label("Kullanıcı Rol"), roleCombo);
		grid.addRow(4, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcırol")), roleCombo);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
					if (passwordField.equals(passwordAgainField)) {
						return UserDTO.builder()
                            .username(String.valueOf(usernameField.getText().trim()))
                            .email(String.valueOf(emailField.getText().trim()))
                            .password(passwordField.getText().trim().isEmpty()
                                    ? existingUser.getPassword()
                                    : passwordField.getText().trim())
							.role(ERole.fromString(String.valueOf(roleCombo.getValue())))
							.count(existing.getCount())
                            .build();
					} else {
						//showAlert("Uyarı", "Yeni Şifre ve Yeni Şifre Tekrar alanlarını lütfen aynı doldurunuz!", Alert.AlertType.WARNING);
						showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("uyarı"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("şifreveşifretekrar"), Alert.AlertType.WARNING);
						return;
					}
                } catch (Exception e) {
                    //showAlert("Hata", "Geçersiz veri!", Alert.AlertType.ERROR);
					showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("geçersizveri"), Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<UserDTO> result = dialog.showAndWait();
        return result.orElse(null);
    }
	
	//PROFIL GÜNCELLEME	
    @FXML
    private void updateProfile() {
		//if(Integer.valueOf(LoginUserIdLabelField.getText()) != 0)
			//Integer userId = Integer.valueOf(LoginUserIdLabelField.getText());
		if (getLoginUserId() != 0) {
			UserDTO selected = userDAO.findById(getLoginUserId());
			if (selected == null) {
				//showAlert("Uyarı", "Lütfen sisteme tekrar giriş yapınız.", Alert.AlertType.WARNING);
				showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("uyarı"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("sistemetekrargiriş"), Alert.AlertType.WARNING);
				return;
			}
			UserDTO updated = showProfileForm(selected);
			if (updated != null && updated.isValid()) {
				userDAO.update(selected.getId(), updated);
				//showAlert("Bilgi", "Kullanıcı profil kaydı güncellendi.", Alert.AlertType.INFORMATION);
				showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("bilgi"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıprofiligüncelleme"), Alert.AlertType.INFORMATION);
			}
		}
    }
	
	// PROFIL GÖRÜNTÜLEME
    @FXML
    private void showProfile() {
		private final LabelField usernameField = new LabelField();
		private final LabelField emailField = new LabelField();
		private final LabelField passwordField = new LabelField();
		private final LabelField roleField = new LabelField();
		private final LabelField countField = new LabelField();
		//if(Integer.valueOf(LoginUserIdLabelField.getText()) != 0)
			//Integer userId = Integer.valueOf(LoginUserIdLabelField.getText());
		if (getLoginUserId() != 0) {
			Dialog<UserDTO> dialog = new Dialog<>();
			//dialog.setTitle("Kullanıcı Profili");
			dialog.setTitle(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıprofili"));
			//dialog.setHeaderText("Kullanıcı Profil Bilgileri");
			dialog.setHeaderText(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıprofilbilgileri"));
			Optional<UserDTO> optionalUser = userDAO.findById(getLoginUserId());
			if (optionalUser.isPresent()) {
				usernameField.setText(String.valueOf(optionalUser.getUsername()));
				emailField.setText(String.valueOf(optionalUser.getEmail()));
				passwordField.setText(String.valueOf(optionalUser.getPassword()));
				roleField.setText(String.valueOf(optionalUser.getRole()));
				countField.setText(String.valueOf(optionalUser.getCount() + 1));
			}
			GridPane grid = new GridPane();
			grid.setHgap(10); grid.setVgap(10);
			//grid.addRow(0, new Label("Kullanıcı Ad:"), usernameField);
			grid.addRow(0, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıad")), usernameField);
			//grid.addRow(1 new Label("Kullanıcı Email:"), emailField);
			grid.addRow(1 new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıemail")), emailField);
			//grid.addRow(2, new Label("Kullanıcı Rol"), roleField);
			grid.addRow(2, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcırol")), roleField);
			//grid.addRow(3, new Label("Kullanıcı Profili Görüntülenme Sayısı"), countField);
			grid.addRow(3, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıprofiligörüntülenmesayısı")), countField);
			dialog.getDialogPane().setContent(grid);
			dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == ButtonType.OK) {
					try {
						Optional<UserDTO> updatedUser = UserDTO.builder()
                            .username(String.valueOf(usernameField.getText()))
                            .email(String.valueOf(emailField.getText()))
                            .password(String.valueOf(passwordField.getText()))
							.role(ERole.fromString(String.valueOf(roleField.getValue())))
							.count(Integer.valueOf(countField.getText()))
                            .build();
						if (updatedUser != null && updatedUser.isValid()) {
							userDAO.update(optionalUser.getId(), updatedUser);
							//showAlert("Bilgi", "Kullanıcı profili görüntülendi.", Alert.AlertType.INFORMATION);
							showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("bilgi"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıprofilgörüntüleme"), Alert.AlertType.INFORMATION);
						}
                    } catch (Exception e) {
                        //showAlert("Hata", "Geçersiz veri!", Alert.AlertType.ERROR);
						showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("geçersizveri"), Alert.AlertType.ERROR);
                    }
				}
			});
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
            //SceneHelper.switchScene(FXMLPath.LOGIN, usernameField, "Giriş Yap");			
			//if (languageSelectComboBox.getValue() == Locale.TURKISH)
			if (languageSelectComboBox.getValue().equals(Locale.TURKISH)) {
				SceneHelper.switchScene(FXMLPath.LOGIN_TR, usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("giriş"));
			} else if (languageSelectComboBox.getValue().equals(Locale.ENGLISH)) {
				SceneHelper.switchScene(FXMLPath.LOGIN_EN, usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("giriş"));
			}
			//SceneHelper.switchScene(FXMLPath.LOGIN, usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("giriş"));
        } catch (Exception e) {
            e.printStackTrace();
            //showAlert("Hata", "Giriş ekranı yüklenemedi!", Alert.AlertType.ERROR);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hatagiriş"), Alert.AlertType.ERROR);
        }
    }
	
	// NOTLAR SAYFASINA GEÇİŞ	
    @FXML
    private void notebook(ActionEvent event) {
        switchToNotebookPane();
    }
	
    @FXML
    private void switchToNotebookPane() {
        try {
            //SceneHelper.switchScene(FXMLPath.NOTEBOOK + "?user=" + getLoginUserId().toString(), usernameField, "NOTLAR");
			//if (languageSelectComboBox.getValue() == Locale.TURKISH)
			if (languageSelectComboBox.getValue().equals(Locale.TURKISH)) {
				SceneHelper.switchScene(FXMLPath.NOTEBOOK_TR + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + getLoginUserId().toString(), usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("notlar"));
			} else if (languageSelectComboBox.getValue().equals(Locale.ENGLISH)) {
				SceneHelper.switchScene(FXMLPath.NOTEBOOK_EN + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + getLoginUserId().toString(), usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("notlar"));
			}
			//SceneHelper.switchScene(FXMLPath.NOTEBOOK + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + getLoginUserId().toString(), usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("notlar"));
        } catch (Exception e) {
            e.printStackTrace();
            //showAlert("Hata", "Not ekranı yüklenemedi", Alert.AlertType.ERROR);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hatanotdefteri"), Alert.AlertType.ERROR);
        }
    }
	
    @FXML
    private void switchToUserHomePane() {
        try {
            //SceneHelper.switchScene(FXMLPath.USER_HOME + "?user=" + getLoginUserId().toString(), usernameField, "KULLANICI ANASAYFA");
			//if (languageSelectComboBox.getValue() == Locale.TURKISH)
			if (languageSelectComboBox.getValue().equals(Locale.TURKISH)) {
				SceneHelper.switchScene(FXMLPath.USER_HOME_TR + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + getLoginUserId().toString(), usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıanasayfa"));
			} else if (languageSelectComboBox.getValue().equals(Locale.ENGLISH)) {
				SceneHelper.switchScene(FXMLPath.USER_HOME_EN + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + getLoginUserId().toString(), usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıanasayfa"));
			}
			//SceneHelper.switchScene(FXMLPath.USER_HOME + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("?kullanıcı=") + getLoginUserId().toString(), usernameField, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıanasayfa"));
        } catch (Exception e) {
            e.printStackTrace();
            //showAlert("Hata", "Kullanıcı anasayfa ekranı yüklenemedi!", Alert.AlertType.ERROR);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hatakullanıcıanasayfa"), Alert.AlertType.ERROR);
        }
    }
}
