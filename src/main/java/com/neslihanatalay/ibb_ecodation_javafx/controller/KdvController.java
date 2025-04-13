package com.neslihanatalay.ibb_ecodation_javafx.controller;

import com.neslihanatalay.ibb_ecodation_javafx.dao.KdvDAO;
import com.neslihanatalay.ibb_ecodation_javafx.dao.ResourceBundleBinding;
import com.neslihanatalay.ibb_ecodation_javafx.dto.KdvDTO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class KdvController {

    private final KdvDAO kdvDAO;
	private final ResourceBundleBinding resourceBundleBinding;
	
	public KdvController() {
		kdvDAO = new KdvDAO();
		resourceBundleBinding = new ResourceBundleBinding();
	}

    @FXML private TableView<KdvDTO> kdvTable;
    @FXML private TableColumn<KdvDTO, Integer> idColumn;
    @FXML private TableColumn<KdvDTO, Double> amountColumn;
    @FXML private TableColumn<KdvDTO, Double> kdvRateColumn;
    @FXML private TableColumn<KdvDTO, Double> kdvAmountColumn;
    @FXML private TableColumn<KdvDTO, Double> totalAmountColumn;
    @FXML private TableColumn<KdvDTO, String> receiptColumn;
    @FXML private TableColumn<KdvDTO, LocalDate> dateColumn;
    @FXML private TableColumn<KdvDTO, String> descColumn;
    @FXML private TextField searchField;
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
		
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        kdvRateColumn.setCellValueFactory(new PropertyValueFactory<>("kdvRate"));
        kdvAmountColumn.setCellValueFactory(new PropertyValueFactory<>("kdvAmount"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        receiptColumn.setCellValueFactory(new PropertyValueFactory<>("receiptNumber"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        refreshTable();
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

    public void refreshTable() {
        Optional<List<KdvDTO>> list = kdvDAO.list();
        list.ifPresent(data -> kdvTable.setItems(FXCollections.observableArrayList(data)));
    }

    private void applyFilter() {
        String keyword = searchField.getText().trim().toLowerCase();
        Optional<List<KdvDTO>> all = kdvDAO.list();
        List<KdvDTO> filtered = all.orElse(List.of()).stream()
                .filter(kdv -> kdv.getReceiptNumber().toLowerCase().contains(keyword))
                .toList();
        kdvTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    public void clearFilters() {
        searchField.clear();
        refreshTable();
    }

    @FXML
    public void addKdv(ActionEvent event) {
        KdvDTO newKdv = showKdvForm(null);
        if (newKdv != null && newKdv.isValid()) {
            kdvDAO.create(newKdv);
            refreshTable();
            //showAlert("Bilgi", "KDV kaydı eklendi.", Alert.AlertType.INFORMATION);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("bilgi"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("yenikdv"), Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void updateKdv(ActionEvent event) {
        KdvDTO selected = kdvTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            //showAlert("Uyarı", "Güncellenecek bir kayıt seçin.", Alert.AlertType.WARNING);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("uyarı"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("seçgüncellenecek"), Alert.AlertType.WARNING);
            return;
        }
        KdvDTO updated = showKdvForm(selected);
        if (updated != null && updated.isValid()) {
            kdvDAO.update(selected.getId(), updated);
            refreshTable();
            //showAlert("Bilgi", "KDV kaydı güncellendi.", Alert.AlertType.INFORMATION);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("bilgi"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kdvgüncelleme"), Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void deleteKdv(ActionEvent event) {
        KdvDTO selected = kdvTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            //showAlert("Uyarı", "Silinecek bir kayıt seçin.", Alert.AlertType.WARNING);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("uyarı"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("seçsilinecek"), Alert.AlertType.WARNING);
            return;
        }
        //Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Silmek istiyor musunuz?", ButtonType.OK, ButtonType.CANCEL);
		Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("onaylasilme"), ButtonType.OK, ButtonType.CANCEL);
        //confirm.setHeaderText("Kayıt: " + selected.getReceiptNumber());
		confirm.setHeaderText(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kayıt") + selected.getReceiptNumber());
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            kdvDAO.delete(selected.getId());
            refreshTable();
            //showAlert("Silindi", "KDV kaydı silindi.", Alert.AlertType.INFORMATION);
			showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("silme"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kdvsilme"), Alert.AlertType.INFORMATION);			
        }
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private KdvDTO showKdvForm(KdvDTO existing) {
        Dialog<KdvDTO> dialog = new Dialog<>();
        //dialog.setTitle(existing == null ? "Yeni KDV Ekle" : "KDV Güncelle");
		dialog.setTitle(existing == null ? resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("yenikdvekle") : resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kdvgüncelle"));

        TextField amountField = new TextField();
        TextField rateField = new TextField();
        TextField receiptField = new TextField();
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField descField = new TextField();
        ComboBox<String> exportCombo = new ComboBox<>();
        exportCombo.getItems().addAll("TXT", "PDF", "EXCEL");
        exportCombo.setValue("TXT");

        if (existing != null) {
            amountField.setText(String.valueOf(existing.getAmount()));
            rateField.setText(String.valueOf(existing.getKdvRate()));
            receiptField.setText(existing.getReceiptNumber());
            datePicker.setValue(existing.getTransactionDate());
            descField.setText(existing.getDescription());
            exportCombo.setValue(existing.getExportFormat());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        //grid.addRow(0, new Label("Tutar"), amountField);
		grid.addRow(0, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("tutar")), amountField);
        //grid.addRow(1, new Label("KDV Oranı (%)"), rateField);
		grid.addRow(1, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("kdvoran")), rateField);
        //grid.addRow(2, new Label("Fiş No"), receiptField);
		grid.addRow(2, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("fişno")), receiptField);
        //grid.addRow(3, new Label("Tarih"), datePicker);
		grid.addRow(3, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("tarih")), datePicker);
        //grid.addRow(4, new Label("Açıklama"), descField);
		grid.addRow(4, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("açıklama")), descField);
        //grid.addRow(5, new Label("Format"), exportCombo);
		grid.addRow(5, new Label(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("format")), exportCombo);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    return KdvDTO.builder()
                            .amount(Double.parseDouble(amountField.getText()))
                            .kdvRate(Double.parseDouble(rateField.getText()))
                            .receiptNumber(receiptField.getText())
                            .transactionDate(datePicker.getValue())
                            .description(descField.getText())
                            .exportFormat(exportCombo.getValue())
                            .build();
                } catch (Exception e) {
                    //showAlert("Hata", "Geçersiz veri girdiniz!", Alert.AlertType.ERROR);
					showAlert(resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("hata"), resourceBundleBinding.RESOURCE_FACTORY.getStringBinding("geçersizveri"), Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<KdvDTO> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
