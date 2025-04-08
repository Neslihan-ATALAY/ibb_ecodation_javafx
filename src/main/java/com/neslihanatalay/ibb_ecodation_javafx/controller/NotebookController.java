package com.neslihanatalay.ibb_ecodation_javafx.controller;

import com.neslihanatalay.ibb_ecodation_javafx.dao.NotebookDAO;
import com.neslihanatalay.ibb_ecodation_javafx.dto.NotebookDTO;
import com.neslihanatalay.ibb_ecodation_javafx.utils.ECategory;
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

import java.util.Optional;

public class NotebookController {

    private final NotebookDAO notebookDAO = new NotebookDAO();

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
	
    private Integer LoginUserIdLabelField = 0;

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
		
	LoginUserIdLabelField = Integer.valueOf(Request["LoginUserIdLabelField"]);
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

    @FXML
    public void refreshTable() {
        Optional<List<NotebookDTO>> list = NotebookDAO.list();
        list.ifPresent(data -> notebookTable.setItems(FXCollections.observableArrayList(data)));
    }

    @FXML
    private void addNotebook(ActionEvent event) {
        NotebookDTO newNotebook = showNotebookForm(null);
        if (newNotebook != null && newNotebook.isValid()) {
            notebookDAO.create(newNotebook);
            refreshTable();
            showAlert("Başarılı", "Not eklendi.", Alert.AlertType.INFORMATION);
        }
    }
	
    @FXML
    private void updateNotebook(ActionEvent event) {
        NotebookDTO selected = notebookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Uyarı", "Güncellenecek bir kayıt seçin.", Alert.AlertType.WARNING);
            return;
        }
        NotebookDTO updated = showNotebookForm(selected);
        if (updated != null && updated.isValid()) {
            notebookDAO.update(selected.getId(), updated);
            refreshTable();
            showAlert("Başarılı", "Not kaydı güncellendi.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void deleteNotebook(ActionEvent event) {
        NotebookDTO selected = notebookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Uyarı", "Silinecek bir kayıt seçin.", Alert.AlertType.WARNING);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Silmek istiyor musunuz?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Kayıt: " + selected.getTitle());
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            notebookDAO.delete(selected.getId());
            refreshTable();
            showAlert("Silindi", "Not kaydı silindi.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private NotebookDTO showNotebookForm(NotebookDTO existing) {
        Dialog<NotebookDTO> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Yeni Not Ekle" : "Not Güncelle");

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
        grid.addRow(0, new Label("Başlık:"), titleField);
        grid.addRow(1, new Label("İçerik:"), contentField);
        grid.addRow(2, new Label("Not Tarihi:"), createdDateField);
        grid.addRow(3, new Label("Not Güncelleme Tarihi:"), updatedDateField);
        grid.addRow(4, new Label("Kategori:"), categoryField);
        grid.addRow(5, new Label("Not Sabitlendi:"), pinnedField);
	grid.addRow(6, new Label("Kullanıcı Adı:"), usernameField);
		
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    return NotebookDTO.builder()
                            .title(titleField.getText())
                            .content(contentField.getText())
                            .createdDate(createdDateField.getValue().toLocalDate())
                            .updatedDate(updatedDateField.getValue().toLocalDAte())
                            .category(ECategory.valueOf(categoryComboBox.getValue().name()))
                            //.category(categoryField.getValue())
                            .pinned(Boolean.parseBoolean(pinnedField.getValue()))
			    .userId(existing.getUserId())
                            .build();
                } catch (Exception e) {
                    showAlert("Hata", "Geçersiz veri!", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<NotebookDTO> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /*
    @FXML
    public void addNotebook2(ActionEvent actionEvent) {
        AddNotebookDialog dialog = new AddNotebookDialog();
        Optional<NotebookDTO> result = dialog.showAndWait();
        result.ifPresent(newNotebook -> {
            if (newNotebook.getTitle().isEmpty() || newNotebook.getContent().isEmpty()) {
                showAlert("Hata", "Tüm alanlar doldurulmalı!", Alert.AlertType.ERROR);
                return;
            }
            Optional<NotebookDTO> createdNotebook = notebookDAO.create(newNotebook);
            if (createdNotebook.isPresent()) {
                showAlert("Başarılı", "Not başarıyla eklendi!", Alert.AlertType.INFORMATION);
                //refreshTableNotebook();
            } else {
                showAlert("Hata", "Not eklenemedi!", Alert.AlertType.ERROR);
            }
        });
    }

    private static class AddNotebookDialog extends Dialog<NotebookDTO> {
        private final private static class AddNotebookDialog extends Dialog<NotebookDTO> {
            private final TextField titleField = new TextField();
            private final TextField contentField = new TextField();
            private final ComboBox<String> categoryComboBox = new ComboBox<>();
            private final CheckBox<Boolean> pinnedCheckBox = new CheckBox<>();
            private final DatePicker createdDateField = new DatePicker(LocalDate.now());
            private final DatePicker updatedDateField = new DatePicker(LocalDate.now());

            public AddNotebookDialog() {
                setTitle("Yeni Not Ekle");
                setHeaderText("Yeni Not Bilgilerini Girin");

                //roleComboBox.getItems().addAll("USER", "ADMIN", "MODERATOR");
                //roleComboBox.setValue("USER");

                ComboBox<ECategory> categoryComboBox = new ComboBox<>();
                categoryComboBox.getItems().addAll(ECategory.values());
                categoryComboBox.setValue(ECategory.PERSONAL);
                pinnedCheckBox.setValue("Notu Sabitle");

                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                String stringNow = now.format(formatter);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                grid.add(new Label("Başlık:"), 0, 0);
                grid.add(titleField, 1, 0);
                grid.add(new Label("İçerik:"), 0, 1);
                grid.add(contentField, 1, 1);
                grid.add(new Label("Not Tarihi:"), 0, 2);
                grid.add(createdDateField, 1, 2);
                grid.add(new Label("Not Güncellenme Tarihi:"), 0, 3);
                grid.add(updatedDateField, 1, 3);
                grid.add(new Label("Kategori:"), 0, 4);
                grid.add(categoryComboBox, 1, 4);
                grid.add(new Label("Not Sabit mi?:"), 0, 5);
                grid.add(pinnedCheckBox, 1, 5);

                getDialogPane().setContent(grid);

                ButtonType addButtonType = new ButtonType("Ekle", ButtonBar.ButtonData.OK_DONE);
                getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

                setResultConverter(dialogButton -> {
                    if (dialogButton == addButtonType) {
                        return NotebookDTO.builder()
                                .title(titleField.getText().trim())
                                .content(contentField.getText().trim())
                                .createdDate(createdDateField.getValue())
                                .updatedDate(updatedDateField.getValue())
                                .category(ECategory.valueOf(categoryComboBox.getValue().name()))
                                //.category(categoryComboBox.getValue())
                                .pinned(pinnedCheckBox.getValue())
                                .build();
                    }
                    return null;
                });
            }
        }
    }

    @FXML
    private void addNotebookOld(ActionEvent event) {
        String title = titleField.getText().trim();
        String content = contentField.getText().trim();
        String category = categoryField.getSelected();
		Boolean pinned = pinnedField.getValue();
		LocalDateTime createdDate, updatedDate;

        if (title.isEmpty() || content.isEmpty()) {
            showAlert("Hata", "Lütfen başlık ve içerik alanlarını doldurun", Alert.AlertType.ERROR);
            return;
        }

        NotebookDTO notebookDTO = NotebookDTO.builder()
                .title(title)
                .content(content)
				.createdDate(createdDate)
				.updatedDate(updatedDate)
                .category(category)
                .pinned(pinned)
				//.userDTO(userDTO)
                .build();

        Optional<NotebookDTO> createdNotebook = notebookDAO.create(notebookDTO);
        if (createdNotebook.isPresent()) {
            showAlert("Başarılı", "Başarılı", Alert.AlertType.INFORMATION);
            switchToNotebookPane();
        } else {
            showAlert("Hata", "Başarısız", Alert.AlertType.ERROR);
        }
    }
     */
}
