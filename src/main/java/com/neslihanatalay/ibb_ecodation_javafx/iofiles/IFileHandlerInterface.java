package com.hamitmizrak.iofiles;

public interface IFileHandlerInterface {

    void createFileIfNotExists();

    void writeFile(String data);

    void readFile();

    default void logInfo(String message) {
        System.out.println("â„¹ï¸ " + message);
    }
}
