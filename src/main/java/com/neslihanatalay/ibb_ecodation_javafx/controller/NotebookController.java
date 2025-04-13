package com.neslihanatalay.ibb_ecodation_javafx.controller;

import com.neslihanatalay.ibb_ecodation_javafx.dao.NotebookDAO;
import com.neslihanatalay.ibb_ecodation_javafx.dao.ResourceBundleBinding;
import com.neslihanatalay.ibb_ecodation_javafx.dao.UserDAO;
import com.neslihanatalay.ibb_ecodation_javafx.dto.NotebookDTO;
import com.neslihanatalay.ibb_ecodation_javafx.dto.UserDTO;
import com.neslihanatalay.ibb_ecodation_javafx.utils.ECategory;
import com.neslihanatalay.ibb_ecodation_javafx.utils.FXMLPath;
import com.neslihanatalay.ibb_ecodation_javafx.utils.SceneHelper;
import com.neslihanatalay.ibb_ecodation_javafx.utils.SpecialColor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.*
import org.apache.commons.httpclient.*;
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

public class NotebookController {

    private final NotebookDAO notebookDAO;
    private final UserDAO userDAO;
    private final ResourceBundleBinding resourceBundleBinding;
	
    public NotebookController() {
	notebookDAO = new NotebookDAO();
	userDAO = new UserDAO();
	resourceBundleBinding = new ResourceBundleBinding();
    }

    @FXML private TableView<NotebookDTO> notebookTable;
    @FXML private TableColumn<NotebookDTO, Integer> idColumn;
    @FXML private TableColumn<NotebookDTO, String> titleColumn;
    @FXML private TableColumn<NotebookDTO, String> contentColumn;
    @FXML private TableColumn<NotebookDTO, LocalDate> createdDateColumn;
    @FXML private TableColumn<NotebookDTO, LocalDate> updatedDateColumn;
    @FXML private TableColumn<NotebookDTO, String> categoryColumn;
    @FXML private TableColumn<NotebookDTO, Boolean> pinnedColumn;
    @FXML private TableColumn<NotebookDTO, String> usernameColumn;
    @FXML private TextField searchField;
    @FXML private ComboBox<Locale> languageSelectComboBox;
	
    @FXML private Label welcomeLabel;
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
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void specialOnEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            //notebook();
        }
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
		
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        updatedDateColumn.setCellValueFactory(new PropertyValueFactory<>("updatedDate"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        pinnedColumn.setCellValueFactory(new PropertyValueFactory<>("pinned"));
	usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        refreshTable();
		
	// GİRİŞ YAPAN KULLANICININ ID NUMARASI LoginUserIdLabelField'e KAYDEDİLİR
	setLoginUserId(Integer.valueOf(request().getQueryString("kullanıcı").toString()));
	LoginUserIdLabelField.setText(request().getQueryString("kullanıcı").toString());
	UserDTO userDTO = userDAO.findById(getLoginUserId());
	if (userDTO.isPresent()) {
	    welcomeLabel.setText(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("selamlama") 
		+ " " + userDTO.getUsername() + ", " + resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hoşgeldiniz"));
	    //welcomeLabel.setText("Merhaba " + userDTO.getUsername() + ", Hoşgeldiniz");
	}
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
	
    @FXML
    private void applyFilter() {
        String keyword = searchField.getText().trim().toLowerCase();
        Optional<List<NotebookDTO>> all = notebookDAO.list();
        List<NotebookDTO> filtered = all.orElse(List.of()).stream()
                .filter(notebook -> notebook.getTitle().toLowerCase().contains(keyword))
                .toList();
        notebookTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    public void clearFilters() {
        searchField.clear();
        refreshTable();
    }

    //NOTLAR LİSTESİ
    @FXML
    public void refreshTable() {
	if (getLoginUserId() != 0) {
	    Integer UserId = getLoginUserId();
	    Optional<List<NotebookDTO>> list = NotebookDAO.listByUserId(UserId);
	    list.ifPresent(data -> notebookTable.setItems(FXCollections.observableArrayList(data)));
	}
    }

    // YENİ NOT
    @FXML
    private void addNotebook(ActionEvent event) {
        NotebookDTO newNotebook = showNotebookForm(null);
        if (newNotebook != null && newNotebook.isValid()) {
            notebookDAO.create(newNotebook);
            refreshTable();
            showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("bilgi"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("noteklendi"), Alert.AlertType.INFORMATION);
            //showAlert("Bilgi", "Not eklendi.", Alert.AlertType.INFORMATION);
	}
    }
	
    //NOT GÜNCELLEME
    @FXML
    private void updateNotebook(ActionEvent event) {
        NotebookDTO selected = notebookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            //showAlert("Uyarı", "Güncellenecek bir kayıt seçin.", Alert.AlertType.WARNING);
	    showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("uyarı"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("seçgüncellenecek"), Alert.AlertType.WARNING);
            return;
        }
        NotebookDTO updated = showNotebookForm(selected);
        if (updated != null && updated.isValid()) {
            notebookDAO.update(selected.getId(), updated);
            refreshTable();
	    showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("bilgi"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("notgüncellendi"), Alert.AlertType.INFORMATION);
            //showAlert("Bilgi", "Not kaydı güncellendi.", Alert.AlertType.INFORMATION);
        }
    }

    //NOT SİLME
    @FXML
    private void deleteNotebook(ActionEvent event) {
        NotebookDTO selected = notebookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            //showAlert("Uyarı", "Silinecek bir kayıt seçin.", Alert.AlertType.WARNING);
	    showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("uyarı"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("seçsilinecek"), Alert.AlertType.WARNING);
            return;
        }
        //Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Silmek istiyor musunuz?", ButtonType.OK, ButtonType.CANCEL);
	Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("onaylasilme"), ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Kayıt: " + selected.getTitle());
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            notebookDAO.delete(selected.getId());
            refreshTable();
	    showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("silme"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("notsilindi"), Alert.AlertType.INFORMATION);
            //showAlert("Silindi", "Not kaydı silindi.", Alert.AlertType.INFORMATION);
        }
    }

    //NOT DİALOG SAYFASI
    //@FXML
    private NotebookDTO showNotebookForm(NotebookDTO existing) {
        Dialog<NotebookDTO> dialog = new Dialog<>();
        //dialog.setTitle(existing == null ? "Yeni Not Ekle" : "Not Güncelle");
	dialog.setTitle(existing == null ? resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("yeninotekle") : resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("notgüncelle");
		
        TextField titleField = new TextField();
        TextField contentField = new TextField();
        DatePicker createdDateField = new DatePicker(LocalDate.now());
        DatePicker updatedDateField = new DatePicker(LocalDate.now());
        ComboBox<String> categoryField = new ComboBox<>();
        categoryField.getItems().addAll(ECategory.values());
        categoryField.setValue(ECategory.PERSONAL);
        CheckBox pinnedField = new CheckBox();
	LabelField usernameField = new LabelField();

        if (existing != null) {
            titleField.setText(String.valueOf(existing.getTitle()));
            contentField.setText(String.valueOf(existing.getContent()));
            createdDateField.setValue(existing.getCreatedDate());
            updatedDateField.setValue(existing.getUpdatedDate());
            categoryField.setValue(existing.getCategory());
            pinnedField.setValue(Boolean.parseBoolean(existing.getPinned()));
	    UserDTO userDTO = userDAO.findById(existing.getUserId());
	    if (userDTO.isPresent()) {
		usernameField.setValue(userDTO.getUsername());
	    } else {
		usernameField.setValue("");
	    }
        }

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        //grid.addRow(0, new Label("Başlık"), titleField);
	grid.addRow(0, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("başlık")), titleField);
        //grid.addRow(1, new Label("İçerik:"), contentField);
	grid.addRow(1, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("içerik")), contentField);
        //grid.addRow(2, new Label("Not Tarihi"), createdDateField);
	grid.addRow(2, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("nottarihi")), createdDateField);
        //grid.addRow(3, new Label("Not Güncelleme Tarihi"), updatedDateField);
	grid.addRow(3, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("notgüncellemetarihi")), updatedDateField);
        //grid.addRow(4, new Label("Kategori"), categoryField);
	grid.addRow(4, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kategori")), categoryField);
        //grid.addRow(5, new Label("Not Sabitle"), pinnedField);
	grid.addRow(5, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("notsabitle")), pinnedField);
	//grid.addRow(6, new Label("Kullanıcı Adı"), usernameField);
	grid.addRow(6, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kullanıcıadı")), usernameField);
		
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    return NotebookDTO.builder()
                            .title(titleField.getText())
                            .content(contentField.getText())
                            .createdDate(createdDateField.getValue().toLocalDate())
                            .updatedDate(updatedDateField.getValue().toLocalDate())
                            .category(ECategory.valueOf(categoryField.getValue().name()))
                            .pinned(Boolean.parseBoolean(pinnedField.getValue()))
			    .userId((existing != null) ? (existing.getUserId()) : (getLoginUserId())
                            .build();
                } catch (Exception e) {
                    //showAlert("Hata", "Geçersiz veri!", Alert.AlertType.ERROR);
		    showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("geçersizveri"), Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<NotebookDTO> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
