package com.neslihanatalay.ibb_ecodation_javafx.iofiles;

import com.neslihanatalay.ibb_ecodation_javafx.utils.SpecialColor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpecialFileHandler {

    private static final Logger logger = Logger.getLogger(SpecialFileHandler.class.getName());
    private String filePath;

    public SpecialFileHandler() {
        this.filePath = "default.txt";
    }

    public void createFileIfNotExists() {
        File file = new File(filePath);
        try {
            if (file.exists()) {
                System.out.println(SpecialColor.YELLOW+"âœ… Dosya zaten mevcut: " + filePath+SpecialColor.RESET);
            } else {
                if (file.createNewFile()) {
                    System.out.println(SpecialColor.BLUE+"ğŸ“„ Yeni dosya oluÅŸturuldu: " + filePath+SpecialColor.RESET);
                } else {
                    System.out.println(SpecialColor.RED+"âš ï¸ Dosya oluÅŸturulamadÄ±: " + filePath+SpecialColor.RESET);
                }
            }
        } catch (IOException e) {
            System.out.println(SpecialColor.RED+"âš ï¸ Dosya oluÅŸturma hatasÄ±: " + filePath+SpecialColor.RESET);
        }
    }

    public void writeFile(String data) {
        if (data == null || data.trim().isEmpty()) {
            logger.warning("âš ï¸ BoÅŸ veri yazÄ±lamaz!");
            System.out.println(SpecialColor.RED+"âš ï¸ BoÅŸ veri yazÄ±lamaz! " + filePath+SpecialColor.RESET);
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
            System.out.println(SpecialColor.BLUE+"âœ… Veri baÅŸarÄ±yla dosyaya yazÄ±ldÄ±: " + filePath+SpecialColor.RESET);
        } catch (IOException e) {
            System.out.println(SpecialColor.RED+"âŒDosyaya yazma hatasÄ±: " + filePath+SpecialColor.RESET);
        }
    }

    public List<String> readFile() {
        File file = new File(filePath);
        List<String> fileLines = new ArrayList<>();

        if (!file.exists()) {
            System.out.println(SpecialColor.RED+"âš ï¸ Okunacak dosya bulunamadÄ±: " + filePath+SpecialColor.RESET);
            return fileLines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println(SpecialColor.BLUE+"ğŸ“– Dosya iÃ§eriÄŸi okunuyor... " +SpecialColor.RESET);
            while ((line = reader.readLine()) != null) {
                fileLines.add(line);
            }
        } catch (IOException e) {
            System.out.println(SpecialColor.RED+"âŒ Dosya okuma hatasÄ± " + filePath+SpecialColor.RESET);
        }

        if (fileLines.isEmpty()) {
            System.out.println(SpecialColor.RED+"âš ï¸ Dosya okunmasÄ±na raÄŸmen iÃ§erik boÅŸ." +SpecialColor.RESET);
        } else {
            System.out.println(SpecialColor.BLUE+"âœ… Dosyadan " + fileLines.size() + " satÄ±r baÅŸarÄ±yla okundu."+SpecialColor.RESET);
        }

        return fileLines;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            System.out.println(SpecialColor.RED+"ï¸ GeÃ§ersiz dosya yolu! VarsayÄ±lan dosya adÄ± atanÄ±yor: default.txt"+SpecialColor.RESET);
            this.filePath = "default.txt";
        } else {
            this.filePath = filePath;
        }
    }
}
