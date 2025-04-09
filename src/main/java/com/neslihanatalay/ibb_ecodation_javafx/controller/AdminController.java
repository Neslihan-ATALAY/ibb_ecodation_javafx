
package com.neslihanatalay.ibb_ecodation_javafx.controller;

import com.neslihanatalay.ibb_ecodation_javafx.dao.KdvDAO;
import com.neslihanatalay.ibb_ecodation_javafx.dao.UserDAO;
import com.neslihanatalay.ibb_ecodation_javafx.dto.KdvDTO;
import com.neslihanatalay.ibb_ecodation_javafx.dto.UserDTO;
import com.neslihanatalay.ibb_ecodation_javafx.utils.ERole;
import com.neslihanatalay.ibb_ecodation_javafx.utils.FXMLPath;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.*
import org.apache.commons.httpclient.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class AdminController {

    private UserDAO userDAO;
    private KdvDAO kdvDAO;

    public AdminController() {
        userDAO = new UserDAO();
        kdvDAO = new KdvDAO();
    }

    @FXML private TableView<UserDTO> userTable;
    @FXML private TableColumn<UserDTO, Integer> idColumn;
    @FXML private TableColumn<UserDTO, String> usernameColumn;
    @FXML private TableColumn<UserDTO, String> emailColumn;
    @FXML private TableColumn<UserDTO, String> passwordColumn;
    @FXML private TableColumn<UserDTO, String> roleColumn;
    @FXML private TextField searchField;
    @FXML private ComboBox<ERole> filterRoleComboBox;

    @FXML private TableView<KdvDTO> kdvTable;
    @FXML private TableColumn<KdvDTO, Integer> idColumnKdv;
    @FXML private TableColumn<KdvDTO, Double> amountColumn;
    @FXML private TableColumn<KdvDTO, Double> kdvRateColumn;
    @FXML private TableColumn<KdvDTO, Double> kdvAmountColumn;
    @FXML private TableColumn<KdvDTO, Double> totalAmountColumn;
    @FXML private TableColumn<KdvDTO, String> receiptColumn;
    @FXML private TableColumn<KdvDTO, LocalDate> dateColumn;
    @FXML private TableColumn<KdvDTO, String> descColumn;
    @FXML private TextField searchKdvField;
	
    @FXML private Label LoginUserIdLabelField;
    @FXML private Label welcomeLabel;
    @FXML private Label clockLabel;
    private static Integer loginUserId;
	
    public static Integer getLoginUserId() { return loginUserId; }
    public static void setLoginUserId(Integer loginUserId) { this.loginUserId = loginUserId; }
	
    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                    clockLabel.setText(now.format(formatter));
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
		
	// GİRİŞ YAPAN KULLANICININ ID NUMARASI LoginUserIdLabelField'e KAYDEDİLİR
	//LoginUserIdLabelField.setText(Integer.valueOf(request.getParameter("user")));
	setLoginUserId(Integer.valueOf(Request["user"].toString()));
	LoginUserIdLabelField.setText(Request["user"].toString());
	UserDTO userDTO = userDAO.findById(getLoginUserId());
	if (userDTO.isPresent()) {
	    welcomeLabel.setText("Merhaba " + userDTO.getUsername() + ", Hoşgeldiniz");
	}

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        filterRoleComboBox.getItems().add(null);
        filterRoleComboBox.getItems().addAll(ERole.values());
        filterRoleComboBox.setValue(null);

        searchField.textProperty().addListener((observable, oldVal, newVal) -> applyFilters());
        filterRoleComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String password, boolean empty) {
                super.updateItem(password, empty);
                setText((empty || password == null) ? null : "******");
            }
        });

        refreshTable();

        idColumnKdv.setCellValueFactory(new PropertyValueFactory<>("id"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        kdvRateColumn.setCellValueFactory(new PropertyValueFactory<>("kdvRate"));
        kdvAmountColumn.setCellValueFactory(new PropertyValueFactory<>("kdvAmount"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        receiptColumn.setCellValueFactory(new PropertyValueFactory<>("receiptNumber"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        searchKdvField.textProperty().addListener((obs, oldVal, newVal) -> applyKdvFilter());

        refreshKdvTable();
    }

    private void applyFilters() {
        String keyword = searchField.getText().toLowerCase().trim();
        ERole selectedRole = filterRoleComboBox.getValue();

        Optional<List<UserDTO>> optionalUsers = userDAO.list();
        List<UserDTO> fullList = optionalUsers.orElseGet(List::of);

        List<UserDTO> filteredList = fullList.stream()
            .filter(user -> {
                boolean matchesKeyword = keyword.isEmpty() ||
                    user.getUsername().toLowerCase().contains(keyword) ||
                    user.getEmail().toLowerCase().contains(keyword) ||
                    user.getRole().getDescription().toLowerCase().contains(keyword);

                    boolean matchesRole = (selectedRole == null) || user.getRole() == selectedRole;
                    return matchesKeyword && matchesRole;
            })
            .toList();
        userTable.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    public void clearFilters() {
        searchField.clear();
        filterRoleComboBox.setValue(null);
    }

    @FXML
    public void openKdvPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/neslihanatalay/ibb_ecodation_javafx/view/kdv.fxml"));
            Parent kdvRoot = loader.load();
            Stage stage = new Stage();
            stage.setTitle("KDV Paneli");
            stage.setScene(new Scene(kdvRoot));
            stage.show();
        } catch (IOException e) {
            showAlert("Hata", "KDV ekranı açılamadı!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshTable() {
        applyFilters();
        Optional<List<UserDTO>> optionalUsers = userDAO.list();
        List<UserDTO> userDTOList = optionalUsers.orElseGet(List::of);
        ObservableList<UserDTO> observableList = FXCollections.observableArrayList(userDTOList);
        userTable.setItems(observableList);
        showAlert("Bilgi", "Tablo başarıyla yenilendi!", Alert.AlertType.INFORMATION);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Çıkış Yap");
        alert.setHeaderText("Oturumdan çıkmak istiyor musunuz?");
        alert.setContentText("Emin misiniz?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLPath.LOGIN));
                Parent root = loader.load();
                Stage stage = (Stage) userTable.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                showAlert("Hata", "Giriş sayfasına yönlendirme başarısız!", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void printTable() {
        Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            showAlert("Yazıcı Bulunamadı", "Yazıcı sistemde tanımlı değil.", Alert.AlertType.ERROR);
            return;
        }

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(userTable.getScene().getWindow())) {
            boolean success = job.printPage(userTable);
            if (success) {
                job.endJob();
                showAlert("Yazdırma", "Tablo başarıyla yazdırıldı.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Yazdırma Hatası", "Yazdırma işlemi başarısız oldu.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void openCalculator() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                Runtime.getRuntime().exec("calc");
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open -a Calculator");
            } else if (os.contains("nux")) {
                Runtime.getRuntime().exec("gnome-calculator"); // Linux için
            } else {
                showAlert("Hata", "Bu işletim sistemi desteklenmiyor!", Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            showAlert("Hata", "Hesap makinesi açılamadı.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void openKdvCalculator() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("KDV Hesapla");
        dialog.setHeaderText("KDV Hesaplayıcı");

        TextField amountField = new TextField();
        ComboBox<String> kdvBox = new ComboBox<>();
        kdvBox.getItems().addAll("1%", "8%", "18%", "Özel");
        kdvBox.setValue("18%");
        TextField customKdv = new TextField(); customKdv.setDisable(true);
        TextField receiptField = new TextField();
        DatePicker datePicker = new DatePicker();
        Label resultLabel = new Label();

        kdvBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            customKdv.setDisable(!"Özel".equals(newVal));
            if (!"Özel".equals(newVal)) customKdv.clear();
        });

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, new Label("Tutar:"), amountField);
        grid.addRow(1, new Label("KDV Oranı:"), kdvBox);
        grid.addRow(2, new Label("Özel Oran:"), customKdv);
        grid.addRow(3, new Label("Fiş No:"), receiptField);
        grid.addRow(4, new Label("Tarih:"), datePicker);
        grid.add(resultLabel, 0, 5, 2, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("Hesapla", ButtonBar.ButtonData.OK_DONE), ButtonType.CLOSE);

        dialog.setResultConverter(button -> {
            if (button.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    double rate = switch (kdvBox.getValue()) {
                        case "1%" -> 1;
                        case "8%" -> 8;
                        case "18%" -> 18;
                        default -> Double.parseDouble(customKdv.getText());
                    };
                    double kdv = amount * rate / 100;
                    double total = amount + kdv;

                    String result = String.format("""
                            Fiş No: %s
                            Tarih: %s
                            Ara Toplam: %.2f ₺
                            KDV (%%%.1f): %.2f ₺
                            Genel Toplam: %.2f ₺
                            """,
                            receiptField.getText(), datePicker.getValue(),
                            amount, rate, kdv, total);

                    resultLabel.setText(result);
                    showExportOptions(result);
                } catch (Exception e) {
                    showAlert("Hata", "Geçersiz giriş.", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showExportOptions(String content) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("TXT", "TXT", "PDF", "EXCEL", "MAIL");
        dialog.setTitle("Dışa Aktar");
        dialog.setHeaderText("KDV sonucu nasıl dışa aktarılsın?");
        dialog.setContentText("Format:");
        dialog.showAndWait().ifPresent(choice -> {
            switch (choice) {
                case "TXT" -> exportAsTxt(content);
                case "PDF" -> exportAsPdf(content);
                case "EXCEL" -> exportAsExcel(content);
                case "MAIL" -> sendMail(content);
            }
        });
    }

    private void sendMail(String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("E-Posta Gönder");
        dialog.setHeaderText("KDV sonucunu göndereceğiniz e-posta adresini girin:");
        dialog.setContentText("E-posta:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(receiver -> {
            String senderEmail = "seninmailin@gmail.com";
            String senderPassword = "uygulama-sifresi";
            String host = "smtp.gmail.com";
            int port = 587;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
                message.setSubject("KDV Hesaplama Sonucu");
                message.setText(content);

                Transport.send(message);

                showAlert("Başarılı", "Mail başarıyla gönderildi!", Alert.AlertType.INFORMATION);
            } catch (MessagingException e) {
                e.printStackTrace();
                showAlert("Hata", "Mail gönderilemedi.", Alert.AlertType.ERROR);
            }
        });
    }

    private void exportAsTxt(String content) {
        try {
            Path path = Paths.get(System.getProperty("user.home"), "Desktop",
                    "kdv_" + System.currentTimeMillis() + ".txt");
            Files.writeString(path, content);
            showAlert("Başarılı", "TXT masaüstüne kaydedildi", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Hata", "TXT kaydedilemedi.", Alert.AlertType.ERROR);
        }
    }

    private void exportAsPdf(String content) {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            PDPageContentStream stream = new PDPageContentStream(doc, page);
            stream.beginText();
            stream.setFont(PDType1Font.HELVETICA, 12);
            stream.setLeading(14.5f);
            stream.newLineAtOffset(50, 750);

            for (String line : content.split("\n")) {
                String safeLine = line.replace("\t", "    ");
                stream.showText(safeLine);
                stream.newLine();
            }

            stream.endText();
            stream.close();

            File file = new File(System.getProperty("user.home") + "/Desktop/kdv_" + System.currentTimeMillis() + ".pdf");
            doc.save(file);
            showAlert("Başarılı", "PDF masaüstüne kaydedildi", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Hata", "PDF kaydedilemedi.", Alert.AlertType.ERROR);
        }
    }

    private void exportAsExcel(String content) {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("KDV");

            var headerStyle = wb.createCellStyle();
            var font = wb.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            Row header = sheet.createRow(0);
            String[] headers = {"ID", "Tutar", "KDV Oranı", "KDV Tutarı", "Toplam", "Fiş No", "Tarih", "Açıklama"};
            for (int i = 0; i < headers.length; i++) {
                var cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (KdvDTO kdv : kdvTable.getItems()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(kdv.getId());
                row.createCell(1).setCellValue(kdv.getAmount());
                row.createCell(2).setCellValue(kdv.getKdvRate());
                row.createCell(3).setCellValue(kdv.getKdvAmount());
                row.createCell(4).setCellValue(kdv.getTotalAmount());
                row.createCell(5).setCellValue(kdv.getReceiptNumber());
                row.createCell(6).setCellValue(String.valueOf(kdv.getTransactionDate()));
                row.createCell(7).setCellValue(kdv.getDescription());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            File file = new File(System.getProperty("user.home") + "/Desktop/kdv_" + System.currentTimeMillis() + ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }

            showAlert("Başarılı", "Excel masaüstüne kaydedildi", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Hata", "Excel kaydedilemedi.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void exportKdvAsTxt() {
        exportAsTxt(generateKdvSummary());
    }

    @FXML
    public void exportKdvAsPdf() {
        exportAsPdf(generateKdvSummary());
    }

    @FXML
    public void exportKdvAsExcel() {
        exportAsExcel(generateKdvSummary());
    }

    @FXML
    public void printKdvTable() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(kdvTable.getScene().getWindow())) {
            boolean success = job.printPage(kdvTable);
            if (success) {
                job.endJob();
                showAlert("Yazdırma", "KDV tablosu yazdırıldı.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Hata", "Yazdırma başarısız.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void sendKdvByMail() {
        sendMail(generateKdvSummary());
    }

    private String generateKdvSummary() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID\tTutar\tKDV Oranı\tKDV Tutarı\tToplam\tFiş No\tTarih\tAçıklama\n");
        for (KdvDTO kdv : kdvTable.getItems()) {
            builder.append(String.format("%d\t%.2f\t%.2f%%\t%.2f\t%.2f\t%s\t%s\t%s\n",
                    kdv.getId(),
                    kdv.getAmount(),
                    kdv.getKdvRate(),
                    kdv.getKdvAmount(),
                    kdv.getTotalAmount(),
                    kdv.getReceiptNumber(),
                    kdv.getTransactionDate(),
                    kdv.getDescription()));
        }
        return builder.toString();
    }

    @FXML
    private void handleNew() {
        System.out.println("Yeni oluşturuluyor...");
    }

    @FXML
    private void handleOpen() {
        System.out.println("Dosya açılıyor...");
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    @FXML
    private void goToUsers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/path/to/user.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void goToSettings(ActionEvent event) throws IOException {
       /* Parent root = FXMLLoader.load(getClass().getResource("/path/to/settings.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hakkında");
        alert.setHeaderText("Uygulama Bilgisi");
        alert.setContentText("Bu uygulama JavaFX ile geliştirilmiştir.");
        alert.showAndWait();
    }

    private static class AddUserDialog extends Dialog<UserDTO> {
        private final TextField usernameField = new TextField();
        private final PasswordField passwordField = new PasswordField();
	private final PasswordField passwordAgainField = new PasswordField();
        private final TextField emailField = new TextField();
        private final ComboBox<String> roleComboBox = new ComboBox<>();

        public AddUserDialog() {
            setTitle("Yeni Kullanıcı Ekle");
            setHeaderText("Yeni kullanıcı bilgilerini girin");

            //roleComboBox.getItems().addAll("USER", "ADMIN", "MODERATOR");
            //roleComboBox.setValue("USER");

            ComboBox<ERole> roleComboBox = new ComboBox<>();
            roleComboBox.getItems().addAll(ERole.values());
            roleComboBox.setValue(ERole.USER); // Varsayılan seçim

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Kullanıcı Adı:"), 0, 0);
            grid.add(usernameField, 1, 0);
            grid.add(new Label("Şifre:"), 0, 1);
            grid.add(passwordField, 1, 1);
	    grid.add(new Label("Yeni Şifre:"), 0, 2);
            grid.add(passwordAgainField, 1, 2);
            grid.add(new Label("E-posta:"), 0, 3);
            grid.add(emailField, 1, 3);
            grid.add(new Label("Rol:"), 0, 4);
            grid.add(roleComboBox, 1, 4);

            getDialogPane().setContent(grid);

            ButtonType addButtonType = new ButtonType("Ekle", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
		    if (passwordField.equals(passwordAgainField)) {
			return UserDTO.builder()
				.username(usernameField.getText().trim())
				.password(passwordField.getText().trim())
				.email(emailField.getText().trim())
				.role(ERole.valueOf(roleComboBox.getValue().name()))
				.count(0)
				.build();
		    } else {
			showAlert("Uyarı", "Yeni Şifre ve Yeni Şifre Tekrar alanlarını lütfen aynı doldurunuz!", Alert.AlertType.WARNING);
			return;
		    }
                }
                return null;
            });
        }
    }

    @FXML
    public void addUser(ActionEvent actionEvent) {
        AddUserDialog dialog = new AddUserDialog();
        Optional<UserDTO> result = dialog.showAndWait();

        result.ifPresent(newUser -> {
            if (newUser.getUsername().isEmpty() || newUser.getPassword().isEmpty() || newUser.getEmail().isEmpty()) {
                showAlert("Hata", "Tüm alanlar doldurulmalı!", Alert.AlertType.ERROR);
                return;
            }

            if (userDAO.isUsernameExists(newUser.getUsername())) {
                showAlert("Uyarı", "Bu kullanıcı adı zaten kayıtlı!", Alert.AlertType.WARNING);
                return;
            }

            if (userDAO.isEmailExists(newUser.getEmail())) {
                showAlert("Uyarı", "Bu e-posta zaten kayıtlı!", Alert.AlertType.WARNING);
                return;
            }

            Optional<UserDTO> createdUser = userDAO.create(newUser);
            if (createdUser.isPresent()) {
                showAlert("Başarılı", "Kullanıcı başarıyla eklendi!", Alert.AlertType.INFORMATION);
                refreshTable();
            } else {
                showAlert("Hata", "Kullanıcı eklenemedi!", Alert.AlertType.ERROR);
            }
        });
    }

    private static class UpdateUserDialog extends Dialog<UserDTO> {
        private final TextField usernameField = new TextField();
        private final PasswordField passwordField = new PasswordField();
	private final PasswordField passwordAgainField = new PasswordField();
        private final TextField emailField = new TextField();
        private final ComboBox<ERole> roleComboBox = new ComboBox<>();

        public UpdateUserDialog(UserDTO existingUser) {
            setTitle("Kullanıcı Güncelle");
            setHeaderText("Kullanıcı bilgilerini düzenleyin");

            usernameField.setText(existingUser.getUsername());
            emailField.setText(existingUser.getEmail());

            roleComboBox.getItems().addAll(ERole.values());

            try {
                roleComboBox.setValue(ERole.fromString(String.valueOf(existingUser.getRole())));
            } catch (RuntimeException e) {
                roleComboBox.setValue(ERole.USER);
            }

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Kullanıcı Adı:"), 0, 0);
            grid.add(usernameField, 1, 0);
            grid.add(new Label("Yeni Şifre:"), 0, 1);
            grid.add(passwordField, 1, 1);
            grid.add(new Label("Yeni Şifre Tekrar:"), 0, 2);
            grid.add(passwordAgainField, 1, 2);
            grid.add(new Label("E-posta:"), 0, 3);
            grid.add(emailField, 1, 3);
            grid.add(new Label("Rol:"), 0, 4);
            grid.add(roleComboBox, 1, 4);

            getDialogPane().setContent(grid);

            ButtonType updateButtonType = new ButtonType("Güncelle", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            setResultConverter(dialogButton -> {
		if (dialogButton == updateButtonType) {
                    try {
			if (passwordField.equals(passwordAgainField)) {
			    return UserDTO.builder()
				.username(usernameField.getText().trim())
				.password(passwordField.getText().trim().isEmpty()
                                    ? existingUser.getPassword()
                                    : passwordField.getText().trim())
				.email(emailField.getText().trim())
				.role(ERole.valueOf(roleComboBox.getValue().name()))
				.count(existingUser.getCount())
				.build();
			} else {
			    showAlert("Uyarı", "Yeni Şifre ve Yeni Şifre Tekrar alanlarını lütfen aynı doldurunuz!", Alert.AlertType.WARNING);
			    return;
			}
		    } catch(Exception e) {
			showAlert("Hata", "Geçersiz veri!", Alert.AlertType.ERROR);
		    }
                }
                return null;
            });
        }
    }

    @FXML
    public void updateUser(ActionEvent actionEvent) {
        UserDTO selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert("Uyarı", "Lütfen güncellenecek bir kullanıcı seçin!", Alert.AlertType.WARNING);
            return;
        }

        UpdateUserDialog dialog = new UpdateUserDialog(selectedUser);
        Optional<UserDTO> result = dialog.showAndWait();

        result.ifPresent(updatedUser -> {
            if (updatedUser.getUsername().isEmpty() || updatedUser.getPassword().isEmpty() || updatedUser.getEmail().isEmpty()) {
                showAlert("Hata", "Tüm alanlar doldurulmalı!", Alert.AlertType.ERROR);
                return;
            }

            Optional<UserDTO> updated = userDAO.update(selectedUser.getId(), updatedUser);
            if (updated.isPresent()) {
                showAlert("Başarılı", "Kullanıcı güncellendi!", Alert.AlertType.INFORMATION);
                refreshTable();
            } else {
                showAlert("Hata", "Güncelleme işlemi başarısız!", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    public void deleteUser(ActionEvent actionEvent) {
        Optional<UserDTO> selectedUser = Optional.ofNullable(userTable.getSelectionModel().getSelectedItem());
        selectedUser.ifPresent(user -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Silme Onayı");
            confirmationAlert.setHeaderText("Kullanıcıyı silmek istiyor musunuz?");
            confirmationAlert.setContentText("Silinecek kullanıcı: " + user.getUsername());
            Optional<ButtonType> isDelete = confirmationAlert.showAndWait();
            if (isDelete.isPresent() && isDelete.get() == ButtonType.OK) {
                Optional<UserDTO> deleteUser = userDAO.delete(user.getId());
                if (deleteUser.isPresent()) {
                    showAlert("Başarılı", "Kullanıcı başarıyla silindi", Alert.AlertType.INFORMATION);
                    refreshTable();
                } else {
                    showAlert("Başarısız", "Silme işlemi başarısız oldu", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void refreshKdvTable() {
        Optional<List<KdvDTO>> list = kdvDAO.list();
        list.ifPresent(data -> kdvTable.setItems(FXCollections.observableArrayList(data)));
    }

    private void applyKdvFilter() {
        String keyword = searchKdvField.getText().trim().toLowerCase();
        Optional<List<KdvDTO>> all = kdvDAO.list();
        List<KdvDTO> filtered = all.orElse(List.of()).stream()
                .filter(kdv -> kdv.getReceiptNumber().toLowerCase().contains(keyword))
                .toList();
        kdvTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    public void addKdv() {
        KdvDTO newKdv = showKdvForm(null);
        if (newKdv != null && newKdv.isValid()) {
            kdvDAO.create(newKdv);
            refreshKdvTable();
            showAlert("Başarılı", "KDV kaydı eklendi.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void updateKdv() {
        KdvDTO selected = kdvTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Uyarı", "Güncellenecek bir kayıt seçin.", Alert.AlertType.WARNING);
            return;
        }

        KdvDTO updated = showKdvForm(selected);
        if (updated != null && updated.isValid()) {
            kdvDAO.update(selected.getId(), updated);
            refreshKdvTable();
            showAlert("Başarılı", "KDV kaydı güncellendi.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void deleteKdv() {
        KdvDTO selected = kdvTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Uyarı", "Silinecek bir kayıt seçin.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Silmek istiyor musunuz?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Fiş: " + selected.getReceiptNumber());
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            kdvDAO.delete(selected.getId());
            refreshKdvTable();
            showAlert("Silindi", "KDV kaydı silindi.", Alert.AlertType.INFORMATION);
        }
    }

    private KdvDTO showKdvForm(KdvDTO existing) {
        Dialog<KdvDTO> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Yeni KDV Ekle" : "KDV Güncelle");

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
        grid.addRow(0, new Label("Tutar:"), amountField);
        grid.addRow(1, new Label("KDV Oranı (%):"), rateField);
        grid.addRow(2, new Label("Fiş No:"), receiptField);
        grid.addRow(3, new Label("Tarih:"), datePicker);
        grid.addRow(4, new Label("Açıklama:"), descField);
        grid.addRow(5, new Label("Format:"), exportCombo);

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
                    showAlert("Hata", "Geçersiz veri!", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<KdvDTO> result = dialog.showAndWait();
        return result.orElse(null);
    }

    // BİTİRME PROJESİ
    @FXML
    private void toggleTheme(ActionEvent event) {
        // Tema değiştirme işlemleri burada yapılacak
    }

    @FXML
    private void languageTheme(ActionEvent event) {
        // Uygulamanın dili değiştirilecek (TR/EN vs.)
    }

    @FXML
    private void showNotifications(ActionEvent event) {
        // Bildirimleri gösteren popup veya panel açılacak
    }
	
    //PROFIL GÜNCELLEME DIALOG
    private static class UpdateProfileDialog extends Dialog<UserDTO> {
        private final TextField usernameField = new TextField();
        private final PasswordField passwordField = new PasswordField();
	private final PasswordField passwordAgainField = new PasswordField();
        private final TextField emailField = new TextField();
        private final ComboBox<ERole> roleComboBox = new ComboBox<>();

        public UpdateProfileDialog(UserDTO existingUser) {
            setTitle("Profil Güncelle");
            setHeaderText("Profil bilgilerini düzenleyin");

            usernameField.setText(existingUser.getUsername());
            emailField.setText(existingUser.getEmail());
	    passwordField.setText(existing.getPassword());
	    passwordAgainField.setText(existing.getPassword());
            roleComboBox.getItems().addAll(ERole.values());

            try {
                roleComboBox.setValue(ERole.fromString(String.valueOf(existingUser.getRole())));
            } catch (RuntimeException e) {
                roleComboBox.setValue(ERole.USER);
            }

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Kullanıcı Adı:"), 0, 0);
            grid.add(usernameField, 1, 0);
            grid.add(new Label("Yeni Şifre:"), 0, 1);
            grid.add(passwordField, 1, 1);
	    grid.add(new Label("Yeni Şifre Tekrar:"), 0, 2);
            grid.add(passwordAgainField, 1, 2);
            grid.add(new Label("E-posta:"), 0, 3);
            grid.add(emailField, 1, 3);
            grid.add(new Label("Rol:"), 0, 4);
            grid.add(roleComboBox, 1, 4);

            getDialogPane().setContent(grid);

            ButtonType updateButtonType = new ButtonType("Güncelle", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    try {
			if (passwordField.equals(passwordAgainField)) {
			    return UserDTO.builder()
				.username(usernameField.getText().trim())
				.password(passwordField.getText().trim().isEmpty()
                                    ? existingUser.getPassword()
                                    : passwordField.getText().trim())
				.email(emailField.getText().trim())
				.role(ERole.valueOf(roleComboBox.getValue().name()))
				.count(existingUser.getCount())
				.build();
			} else {
			    showAlert("Uyarı", "Yeni Şifre ve Yeni Şifre Tekrar alanlarını lütfen aynı doldurunuz!", Alert.AlertType.WARNING);
			    return;
			}
		    } catch(Exception e) {
			showAlert("Hata", "Geçersiz veri!", Alert.AlertType.ERROR);
		    }
                }
                return null;
            });
        }
    }

    //PROFIL GÜNCELLEME
    @FXML
    public void updateProfile() {		
	//if(Integer.valueOf(LoginUserIdLabelField.getText()) != 0) {
	    //Integer userId = Integer.valueOf(LoginUserIdLabelField.getText());
	if (getLoginUserId() != 0) {
	    UserDTO selectedUser = userDAO.findById(getLoginUserId());
	    if (selectedUser.isPresent()) {
		UpdateProfileDialog dialog = new UpdateProfileDialog(selectedUser);
		Optional<UserDTO> result = dialog.showAndWait();
		result.ifPresent(updatedUser -> {
		    if (updatedUser.getUsername().isEmpty() || updatedUser.getPassword().isEmpty() || updatedUser.getEmail().isEmpty()) {
			showAlert("Hata", "Tüm alanlar doldurulmalı!", Alert.AlertType.ERROR);
			return;
		    }
		    Optional<UserDTO> updated = userDAO.update(selectedUser.getId(), updatedUser);
		    if (updated.isPresent()) {
			showAlert("Başarılı", "Profil güncellendi.", Alert.AlertType.INFORMATION);
			//showProfile();
		    } else {
		        showAlert("Hata", "Güncelleme işlemi başarısız!", Alert.AlertType.ERROR);
		    }
	    	});
	    }
	}
    }
	
    // PROFIL GÖRÜNTÜLEME
    @FXML
    private void showProfile() {
	private final LabelField usernameField = new LabelField();
	private final LabelField emailField = new LabelField();
	private final LabelField roleField = new LabelField();
	private final LabelField countField = new LabelField();
	//if(Integer.valueOf(LoginUserIdLabelField.getText()) != 0) {
	    //Integer userId = Integer.valueOf(LoginUserIdLabelField.getText());
	if (getLoginUserId() != 0) {
	    Dialog<UserDTO> dialog = new Dialog<>();
	    dialog.setTitle("Kullanıcı Profili");
	    dialog.setHeaderText("Kullanıcı Profil Bilgileri");			
	    Optional<UserDTO> optionalUser = userDAO.findById(getLoginUserId());
	    if (optionalUser.isPresent()) {
		usernameField.setText(String.valueOf(optionalUser.getUsername()));
		emailField.setText(String.valueOf(optionalUser.getEmail()));
		roleField.setText(String.valueOf(optionalUser.getRole()));
		countField.setText(String.valueOf(optionalUser.getCount() + 1));
	    }
	    GridPane grid = new GridPane();
	    grid.setHgap(10); grid.setVgap(10);
	    grid.addRow(0, new Label("Kullanıcı Ad:"), usernameField);
	    grid.addRow(1 new Label("Kullanıcı Email:"), emailField);
	    grid.addRow(2, new Label("Kullanıcı Rol:"), roleField);
	    grid.addRow(3, new Label("Kullanıcı Profili Görüntülenme Sayısı:"), countField);
	    dialog.getDialogPane().setContent(grid);
	    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
	    dialog.setResultConverter(dialogButton -> {
		if (dialogButton == ButtonType.OK) {
		    try {
			Optional<UserDTO> updatedUser = UserDTO.builder()
                            	.username(String.valueOf(usernameField.getText()))
                            	.email(String.valueOf(emailField.getText()))
                            	.password(String.valueOf(passwordField.getText()))
			    	.role(ERole.fromString(String.valueOf(roleCombo.getValue())))
			    	.count(Integer.valueOf(countField.getText()))
                            	.build();
			if (updatedUser != null && updatedUser.isValid()) {
			    userDAO.update(optionalUser.getId(), updatedUser);
			    showAlert("Bilgi", "Kullanıcı profili görüntülendi.", Alert.AlertType.INFORMATION);
			    //switchToUserHomePane();
			}
                    } catch (Exception e) {
                        showAlert("Hata", "Geçersiz veri!", Alert.AlertType.ERROR);
                    }
		}
	    });
	}
    }

    @FXML
    private void backupData(ActionEvent event) {
        // Veritabanı yedekleme işlemleri burada yapılacak
    }

    @FXML
    private void restoreData(ActionEvent event) {
        // Daha önce alınmış bir yedek dosyadan veri geri yüklenecek
    }

    // NOTLAR SAYFASINA GEÇİŞ
    @FXML
    private void notebook(ActionEvent event) {
        switchToNotebookPane();
    }
	
    @FXML
    private void switchToNotebookPane() {
        try {
            SceneHelper.switchScene(FXMLPath.NOTEBOOK + "?user=" + getLoginUserId().toString(), searchField, "NOTLAR");
        } catch (Exception e) {
            System.out.println(SpecialColor.RED + "Not sayfasına yönlendirme başarısız" + SpecialColor.RESET);
            e.printStackTrace();
            showAlert("Hata", "Not ekranı yüklenemedi", Alert.AlertType.ERROR);
        }
    }
	
    @FXML
    private void switchToAdminPane() {
        try {
            SceneHelper.switchScene(FXMLPath.ADMIN + "?user" + getLoginUserId().toString(), searchField, "YÖNETİCİ");
        } catch (Exception e) {
            System.out.println(SpecialColor.RED + "Yönetici sayfasına yönlendirme başarısız" + SpecialColor.RESET);
            e.printStackTrace();
            showAlert("Hata", "Yönetici ekranı yüklenemedi", Alert.AlertType.ERROR);
        }
    }
}
