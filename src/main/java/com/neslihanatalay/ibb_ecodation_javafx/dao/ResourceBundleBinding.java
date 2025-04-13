package com.neslihanatalay.ibb_ecodation_javafx.database;

import org.h2.tools.Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ResourceBundleBinding extends Application {

	private static final String RESOURCE_NAME = Resources.class.getTypeName();
	
	private static final ObservableResourceFactory RESOURCE_FACTORY = new ObservableResourceFactory();
	
	static {
		RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(RESOURCE_NAME));
	}
	
	@Override
	public void start(Stage primaryStage) {
		ComboBox<Locale> languageSelect = new ComboBox<>();
		languageSelect.getItems().addAll(Locale.ENGLISH, Locale.TURKISH);
		languageSelect.setValue(Locale.TURKISH);
		languageSelect.setCellFactory(lv -> new LocaleCell());
		languageSelect.setButtonCell(new LocaleCell());
		
		//languageSelectComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
		languageSelectComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(RESOURCE_NAME, newValue));
			}
		});
		languageSelect.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(RESOURCE_NAME, newValue));
			}
		});
		/*
		Label label = new Label();
		label.textProperty().bind(RESOURCE_FACTORY.getStringBinding("selamlama"));
		BorderPane root = new BorderPane(null, languageSelect, null, label, null);
		root.setPadding(new Insets(10));
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
		*/
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

	public class ObversableResourceFactory {

		private ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();
		public ObjectProperty<ResourceBundle> resourcesProperty() {
			return resources;
		}
		public final ResourceBundle getResources() {
			return resourcesProperty().get();
		}
		public final void setResources(ResourceBundle resources) {
			return resourcesProperty().set(resources);
		}
		public StringBinding getStringBinding(String key) {
			return new StringBinding() {
				{ 
					bind(resourcesProperty()); 
				}
				@Override
				public String computeValue() {
					return getResources().getString(key);
				}
			};
		}
	}
	
	public static class Resources extends ListResourceBundle {
		@Override
		protected Object[][] getContents() {
			return new Object[][] {
				{ "merhaba", " Merhaba " }
				{ "hoşgeldiniz", " Hoşgeldiniz " }
				{ "başarılı", "Başarılı" }
				{ "başarısız", "Başarısız" }
				{ "noteklendi", "Not başarıyla eklendi." }
				{ "notsilindi", "Not başarıyla silindi." }
				{ "notgüncellendi", "Not başarıyla güncellendi." }
				{ "yeninotekle", "Yeni Not Ekle" }
				{ "notgüncelle", "Not Güncelle" }
				{ "uyarı", "UYARI" }
				{ "hata", "HATA" }
				{ "bilgi", "BİLGİ" }
				{ "seçgüncellenecek", "Lütfen güncellenecek bir kayıt seçin." }
				{ "seçsilinecek", "Lütfen silinecek bir kayıt seçin." }
				{ "onaylasilme", "Silmeyi onaylıyor musunuz?" }
				{ "başlık", "Başlık" }
				{ "içerik", "İçerik" }
				{ "nottarihi", "Not Tarihi" }
				{ "notgüncellemetarihi", "Not Güncelleme Tarihi" }
				{ "kategori", "Kategori" }
				{ "notsabitle", "Not Sabitle" }
				{ "kullanıcıad", "Kullanıcı Adı" }
				{ "geçersizveri", "Geçersiz Veri!" }
				{ "hatakullanıcıanasayfa", "Kullanıcı anasayfa ekranı yüklenemedi!" }
				{ "kullanıcıanasayfa", "KULLANICI ANASAYFA" }
				{ "?kullanıcı=", "?kullanıcı=" }
				{ "notlar", "NOT DEFTERİ" }
				{ "hatanotdefteri", "Not defteri ekranı yüklenemedi!" }
				{ "hatagiriş", "Giriş ekranı yüklenemedi!" }
				{ "giriş", "GİRİŞ" }
				{ "tümalanlar", "Lütfen tüm alanları doldurunuz!" }
				{ "kayıtlıkullanıcıadı", "Bu kullanıcı adı zaten kayıtlı!" }
				{ "kayıtlıeposta", "Bu e-posta adresi zaten kayıtlı!" }
				{ "kullanıcıkaydetme", "Kullanıcı başarıyla kaydedildi." }
				{ "kullanıcıkaydedilmedi", "Kullanıcı başarıyla kaydedilemedi!" }
				{ "yenikullanıcı", "Yeni Kullanıcı" }
				{ "kullanıcıgüncelle", "Kullanıcı Güncelle" }
				{ "şifreveşifretekrar", "Yeni Şifre ve Yeni Şifre Tekrar alanlarını lütfen aynı doldurunuz!"}
				{ "sistemetekrargiriş", "Lütfen sisteme tekrar giriş yapınız." }
				{ "kullanıcıprofiligüncelleme", "Kullanıcı profili başarıyla güncellendi." }
				{ "kullanıcıprofili", "Kullanıcı Profili" }
				{ "kullanıcıprofilbilgileri", "Kullanıcı Profil Bilgileri" }				
				{ "kullanıcıad", "Kullanıcı Ad" }
				{ "kullanıcıemail", "Kullanıcı Email" }
				{ "kullanıcırol", "Kullanıcı Rol" }
				{ "yenişifre", "Yeni Şifre" }
				{ "yenişifretekrar", "Yeni Şifre Tekrar" }
				{ "kullanıcıprofiligörüntülenmesayısı", "Kullanıcı Profili Görüntülenme Sayısı" }
				{ "kullanıcıprofilgörüntüleme", "Kullanıcı profili başarıyla görüntülendi." }
				{ "girişbaşarılı", "Sisteme Giriş Başarılı " }
				{ "girişbaşarısız", "Sisteme Giriş Başarısız, Giriş Bilgileri Hatalı" }
				{ "kullanıcıpanel", "Kullanıcı Paneli" }
				{ "yöneticipanel", "Yönetici Paneli" }
				{ "hatayöneticisayfası", "Yönetici sayfası ekranı yüklenemedi!"}
				{ "hatakayıtsayfası", "Kayıt sayfası ekranı yüklenemedi!" }
				{ "kullanıcıkayıt", "Kullanıcı Kaydet" }
				{ "yenikdv", "KDV kaydı başarıyla eklendi." }
				{ "kdvgüncelleme", "KDV kaydı başarıyla güncellendi."}
				{ "kdvsilme", "KDV kaydı başarıyla silindi."}
				{ "kayıt", "Kayıt: " }
				{ "silme", "Silme" }
				{ "yenikdvekle", "Yeni KDV Ekle" }
				{ "kdvgüncelle", "KDV Güncelle" }
				{ "tutar", "Tutar" }
				{ "kdvoran", "KDV Oranı (%)" }
				{ "özeloran", "Özel Oran" }
				{ "fişno", "Fiş No" }
				{ "tarih", "Tarih" }
				{ "açıklama", "Açıklama" }
				{ "format", "Format" }
				{ "kdvpanel", "KDV Paneli" }
				{ "hatakdvsayfası", "KDV sayfası ekranı yüklenemedi!" }
				{ "tabloyenileme", "Tablo başarıyla yenilendi." }
				{ "çıkış", "Çıkış" }
				{ "çıkma", "Oturumdan çıkmak istiyor musunuz?" }
				{ "eminmisiniz?", "Emin misiniz?" }
				{ "hatagirişsayfası", "Giriş sayfası ekranı yüklenemedi!" }
				{ "hatayazıcı", "Yazıcı sistemde tanımlı değil, yazıcı bulunamadı!" }
				{ "yazdırmabaşarılı", "Tablo başarıyla yazdırıldı." }
				{ "yazdırmabaşarısız", "Tablo başarıyla yazdırılamadı." }
				{ "hataişletimsistemi", "Bu işletim sistemi desteklenmiyor!" }
				{ "hatahesapmakinesi", "Hesap makinesi açılamadı!" }
				{ "kdvhesapla", "KDV Hesapla" }
				{ "kdvhesaplayıcı", "KDV Hesaplayıcı" }
				{ "Özel", "özel" }
				{ "hesapla", "Hesapla" }
				{ "kdv", "KDV" }
				{ "aratoplam", "Ara Toplam" }
				{ "geneltoplam", "Genel Toplam" }
				{ "geçersizgiriş", "Geçersiz Giriş" }
				{ "dışaaktarma", "Dışa Aktarma" }
				{ "dışaaktarmaformatı", "KDV sonucu nasıl dışa aktarılsın?" }
				{ "format", "Format" }
				{ "epostagönderme", "E-Posta Gönderme" }
				{ "epostagönderme", "Lütfen KDV sonucunu göndereceğiniz e-posta adresini giriniz" }
				{ "eposta", "E-posta" }
				{ "kdvsonuç", "KDV Hesaplama Sonucu" }
				{ "mailbaşarılı", "Mail başarıyla gönderildi." }
				{ "mailbaşarısız", "Mail başarıyla gönderilemedi!" }
				{ "txtdosyasıbaşarılı", "TXT dosyası masaüstüne başarıyla kaydedildi." }
				{ "txtdosyasıbaşarısız", "TXT dosyası masaüstüne başarıyla kaydedilemedi!" }
				{ "pdfdosyasıbaşarılı", "PDF dosyası masaüstüne başarıyla kaydedildi." }
				{ "pdfdosyasıbaşarısız", "PDF dosyası masaüstüne başarıyla kaydedilemedi!" }
				{ "kdvtutar", "KDV Tutarı" }
				{ "exceldosyasıbaşarılı", "EXCEL dosyası masaüstüne başarıyla kaydedildi." }
				{ "exceldosyasıbaşarısız", "EXCEL dosyası masaüstüne başarıyla kaydedilemedi!" }
				{ "kdvtablosuyazdırmabaşarılı", "KDV tablosu yazdırma başarılı." }
				{ "kdvtablosuyazdırmabaşarısız", "KDV tablosu yazdırma başarısız!" }
				{ "toplam", "Toplam" }
				{ "yeni", "Yeni oluşturuluyor..." }
				{ "dosyaaçma", "Dosya açılıyor..." }
				{ "hakkında", "Hakkında" }
				{ "uygulamabilgisi", "Uygulama Bilgisi" }
				{ "uygulamabilgisiaçıklama", "Bu uygulama JavaFX ile geliştirilmiştir." }
				{ "yenikullanıcıbilgisi", "Yeni kullanıcı bilgilerini giriniz." }
				{ "şifre", "Şifre" }
				{ "şifretekrar", "Şifre Tekrar" }
				{ "rol", "Rol" }
				{ "şifrevetekrar", "Şifre ve Şifre Tekrar alanlarını lütfen aynı doldurunuz!"}
				{ "kullanıcıbilgigüncelle", "Kullanıcı bilgilerini düzenleyin." }
				{ "güncelle", "Güncelle" }
				{ "kullanıcıgüncelleme", "Kullanıcı başarıyla güncellendi." }
				{ "kullanıcıgüncellenmedi", "Kullanıcı başarıyla güncellenemedi!" }
				{ "silmeonay", "Silme Onayı" }
				{ "silinecekkullanıcı", "Silinecek kullanıcı: " }
				{ "kullanıcısilme", "Kullanıcı başarıyla silindi." }
				{ "kullanıcısilinemedi", "Kullanıcı başarıyla silinemedi!" }
				{ "profilgüncelle", "Profil Güncelleme" }
				{ "profilbilgileri", "Profil bilgilerini düzenleyin" }
				{ "profilgüncelleme", "Profil başarıyla güncellendi." }
				{ "profilgüncellenmedi", "Profil başarıyla güncellenemedi!" }
				{ "hatanotsayfası", "Not sayfası ekranı yüklenemedi!" }
				{ "yönetici", "YÖNETİCİ" }
				{ "uygulamayahoşgeldiniz", "JavaFX Uygulamasına Hoşgeldiniz" }
				{ "kullanıcıyönetimigiriş", "Kullanıcı Yönetimi Giriş Sayfası" }
				{ "fxmlyüklenemedi", "FXML Yüklenemedi!" }
				{ "dosya", "Dosya yolu: " }
				{ "kullanıcırol", "Kullanıcı" }
				{ "moderatörrol", "Moderatör" }
				{ "yöneticirol", "Yönetici" }
				{ "geçersizrol", "❌ Geçersiz rol: " }
				{ "kişisel", "Kişisel" }
				{ "iş", "İş" }
				{ "okul", "Okul" }
				{ "geçersizkategori", "❌ Geçersiz kategori: " }
				{ "dosyamevcut", "Dosya zaten mevcut: " }
				{ "yenidosya", "Yeni dosya oluşturuldu: " }
				{ "dosyaoluşturulamadı", "Dosya oluşturulamadı: " }
				{ "dosyahata", "Dosya oluşturma hatası: " }
				{ "boşveri", "Boş veri yazılamaz!" }
				{ "veridosyayayazıldı", "Veri başarıyla dosyaya yazıldı: " }
				{ "dosyayayazmahatası", "Dosyaya yazma hatası: " }
				{ "dosyabulunamadı", "Okunacak dosya bulunamadı: " }
				{ "dosyaokuma", "Dosya içeriği okunuyor... " }
				{ "dosyaokumahatası", "Dosya okuma hatası: " }
				{ "boşdosya", "Dosya okunmasına rağmen içerik boş." }
				{ "dosyadan", "Dosyadan " }
				{ "satır", " satır başarıyla okundu." }
				{ "varsayılandosyaadı", "Geçersiz dosya yolu! Varsayılan dosya adı atanıyor: default.txt." }
			};
		}
	}
	
	public static class Resources_EN extends ListResourceBundle {
		@Override
		protected Object[][] getContents() {
			return new Object[][] {
				{ "selamlama", " Hello " }
				{ "hoşgeldiniz", " Welcome " }
				{ "başarılı", "Successful" }
				{ "başarısız", "Unsuccessful" }
				{ "noteklendi", "Note is added successfully." }
				{ "notsilindi", "Note is removed successfully." }
				{ "notgüncellendi", "Note is updated successfully." }
				{ "yeninotekle", "Add New Note" }
				{ "notgüncelle", "Update Note" }
				{ "uyarı", "WARNING" }
				{ "hata", "ERROR" }
				{ "bilgi", "INFORMATION" }
				{ "seçgüncellenecek", "Select a record that will update, please:" }
				{ "seçsilinecek", "Select a record that will remove, please:" }
				{ "onaylasilme", "Are you confirming to remove?" }
				{ "başlık", "Title" }
				{ "içerik", "Content" }
				{ "nottarihi", "Note Date" }
				{ "notgüncellemetarihi", "Note Update Date" }
				{ "kategori", "Category" }
				{ "notsabitle", "Pin Note" }
				{ "kullanıcıadı", "User Name" }
				{ "geçersizveri", "Invalid Data!" }
				{ "hatakullanıcıanasayfa", "User home screen has not been loaded!" }
				{ "kullanıcıanasayfa", "USER HOME" }
				{ "?kullanıcı=", "?user=" }
				{ "notlar", "NOTEBOOK" }
				{ "hatanotdefteri", "Notebook screen has not been loaded!" }
				{ "hatagiriş", "Login screen has not been loaded!" }
				{ "giriş", "LOGIN" }
				{ "tümalanlar", "Fill all fields, please!" }
				{ "kayıtlıkullanıcıadı", "This user name has already been registered!" }
				{ "kayıtlıeposta", "This email address has already been registered!" }
				{ "kullanıcıkaydetme", "User has been registered successfully." }
				{ "kullanıcıkaydedilmedi", "User has NOT been registered successfully!" }
				{ "yenikullanıcı", "New User" }
				{ "kullanıcıgüncelle", "Update User" }
				{ "şifreveşifretekrar", "Fill New Password and New Password Again fields with same value, please!"}
				{ "sistemetekrargiriş", "Login again to system, please!" }
				{ "kullanıcıprofiligüncelleme", "User profile has been updated successfully." }
				{ "kullanıcıprofili", "User Profile" }
				{ "kullanıcıprofilbilgileri", "User Profile Informations" }				
				{ "kullanıcıad", "User Name" }
				{ "kullanıcıemail", "User Email" }
				{ "kullanıcırol", "User Role" }
				{ "yenişifre", "New Password" }
				{ "yenişifretekrar", "New Password Again" }
				{ "kullanıcıprofiligörüntülenmesayısı", "User Profile Viewing Count" }
				{ "kullanıcıprofilgörüntüleme", "User profile has been viewed successfully." }
				{ "girişbaşarılı", "Login to System is Successful " }
				{ "girişbaşarısız", "Login to System is Unsuccessful, Login informations is Wrong!" }
				{ "kullanıcıpanel", "User Panel" }
				{ "yöneticipanel", "Admin Panel" }
				{ "hatayöneticisayfası", "Admin page screen has not been loaded!"}
				{ "hatakayıtsayfası", "Register page screen has not been loaded!" }
				{ "kullanıcıkayıt", "User Submit" }				
				{ "yenikdv", "KDV record has been submitted successfully." }
				{ "kdvgüncelleme", "KDV record has been updated successfully."}
				{ "kdvsilme", "KDV record has been removed successfully."}
				{ "kayıt", "Record: " }
				{ "silme", "Removing" }
				{ "yenikdvekle", "New KDV" }
				{ "kdvgüncelle", "Update KDV " }
				{ "tutar", "Total" }
				{ "kdvoran", "KDV Percent (%)" }
				{ "özeloran", "Private Percent" }
				{ "fişno", "Receipt Number" }
				{ "tarih", "Date" }
				{ "açıklama", "Description" }
				{ "format", "Format" }
				{ "kdvpanel", "KDV Panel" }
				{ "hatakdvsayfası", "KDV page screen has NOT been loaded!" }
				{ "tabloyenileme", "Table has been refreshed successfully." }
				{ "çıkış", "Logout" }
				{ "çıkma", "Are you exit from session?" }
				{ "eminmisiniz?", "Emin misiniz?" }
				{ "hatagirişsayfası", "Login page screen has NOT been loaded!" }
				{ "hatayazıcı", "Printer is NOT defined in system, printer has NOT been found!" }
				{ "yazdırmabaşarılı", "Table has been written successfully." }
				{ "yazdırmabaşarısız", "Table has NOT been written successfully." }
				{ "hataişletimsistemi", "This operating system is NOT supported!" }
				{ "hatahesapmakinesi", "Calculator has NOT been opened!" }
				{ "kdvhesapla", "KDV Calculate" }
				{ "kdvhesaplayıcı", "KDV Calculator" }
				{ "Özel", "private" }
				{ "hesapla", "Calculate" }
				{ "kdv", "KDV" }
				{ "aratoplam", "ARA Total" }
				{ "geneltoplam", "General Total" }
				{ "geçersizgiriş", "Invalid Login" }
				{ "dışaaktarma", "Export" }
				{ "dışaaktarmaformatı", "How has been KDV Result exported?" }
				{ "format", "Format" }
				{ "epostagönderme", "Email Sending" }
				{ "epostagönderme", "Enter email address that KDV result will be send, please." }
				{ "eposta", "E-mail" }
				{ "kdvsonuç", "KDV Calculate Result" }
				{ "mailbaşarılı", "Mail has been sended successfully." }
				{ "mailbaşarısız", "Mail has NOT been sended successfully!" }
				{ "txtdosyasıbaşarılı", "TXT file has been saved to desktop successfully." }
				{ "txtdosyasıbaşarısız", "TXT file has NOT been saved to desktop successfully.!" }
				{ "pdfdosyasıbaşarılı", "PDF file has been saved to desktop successfully.." }
				{ "pdfdosyasıbaşarısız", "PDF file has NOT been saved to desktop successfully.!" }
				{ "kdvtutar", "KDV Total" }
				{ "exceldosyasıbaşarılı", "EXCEL file has been saved to desktop successfully.." }
				{ "exceldosyasıbaşarısız", "TXT file has NOT been saved to desktop successfully.!" }
				{ "kdvtablosuyazdırmabaşarılı", "KDV table writing successfully." }
				{ "kdvtablosuyazdırmabaşarısız", "KDV table writing unsuccessfully!" }
				{ "toplam", "Sum" }
				{ "yeni", "New is creating..." }
				{ "dosyaaçma", "File is opening..." }
				{ "hakkında", "About" }
				{ "uygulamabilgisi", "Application Information" }
				{ "uygulamabilgisiaçıklama", "This application had been developed by JavaFX." }
				{ "yenikullanıcıbilgisi", "Enter new user informations." }
				{ "şifre", "Password" }
				{ "şifretekrar", "Password Again" }
				{ "rol", "Role" }
				{ "şifrevetekrar", "Fill Password and Password Again fields with same value, please!"}
				{ "kullanıcıbilgigüncelle", "Edit user informations." }
				{ "güncelle", "Update" }
				{ "kullanıcıgüncelleme", "User has been updated successfully." }
				{ "kullanıcıgüncellenmedi", "User has NOT been updated successfully!" }
				{ "silmeonay", "Remove Confirm" }
				{ "silinecekkullanıcı", "User that will remove: " }
				{ "kullanıcısilme", "User has been remove successfully." }
				{ "kullanıcısilinemedi", "User has NOT been remove successfully!" }
				{ "profilgüncelle", "Profile Update" }
				{ "profilbilgileri", "Edit profile informations" }
				{ "profilgüncelleme", "Profile has been updated successfully." }
				{ "profilgüncellenmedi", "Profile has NOT been updated successfully!" }
				{ "hatanotsayfası", "Notebook page screen has NOT been loaded!" }
				{ "yönetici", "ADMIN" }
				{ "uygulamayahoşgeldiniz", "Welcome to JavaFX Application" }
				{ "kullanıcıyönetimigiriş", "User Login Page" }
				{ "fxmlyüklenemedi", "FXML has NOT been loaded!" }
				{ "dosya", "File Path: " }
				{ "kullanıcırol", "User" }
				{ "moderatörrol", "Moderator" }
				{ "yöneticirol", "Admin" }
				{ "geçersizrol", "❌ Invalid role: " }
				{ "kişisel", "Personal" }
				{ "iş", "Work" }
				{ "okul", "School" }
				{ "geçersizkategori", "❌ Geçersiz kategori: " }
				{ "dosyamevcut", "File is already exist: " }
				{ "yenidosya", "New file has been created: " }
				{ "dosyaoluşturulamadı", "File has NOT been created: " }
				{ "dosyahata", "File creation error: " }
				{ "boşveri", "Null data can NOT write!" }
				{ "veridosyayayazıldı", "Data has been written to file successfully: " }
				{ "dosyayayazmahatası", "Writing error to file: " }
				{ "dosyabulunamadı", "Read file has NOT been found: " }
				{ "dosyaokuma", "File content is reading... " }
				{ "dosyaokumahatası", "File reading error:" }
				{ "boşdosya", "However file has been read, file content is null." }
				{ "dosyadan", "From file " }
				{ "satır", " row(s) has been read successfully." }
				{ "varsayılandosyaadı", "Invalid file path! Default file name is initializing: default.txt." }
			};
		}
	}
	
	public static void main(String args) {
		launch(args);
	}
}
