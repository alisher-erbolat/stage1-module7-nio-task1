package com.epam.mjc.nio;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileReader {
    public Profile getDataFromFile(File file){

        String fileData = readFileData(file);
        return parseProfileData(fileData);
    }

    private String readFileData(File file){
        StringBuilder stringBuilder = new StringBuilder();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
        FileChannel fileChannel = randomAccessFile.getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);
            Charset charset = StandardCharsets.US_ASCII;
            while (fileChannel.read(byteBuffer) > 0) {
                byteBuffer.rewind();
                stringBuilder.append(charset.decode(byteBuffer)).append(System.lineSeparator());
                byteBuffer.flip();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private Profile parseProfileData(String fileData) {
        String[] lines = fileData.split(System.lineSeparator());
        String name = null;
        int age = 0;
        String email = null;
        Long phone = null;

        for (String line : lines) {
            if (line.startsWith("Name:")) {
                name = line.substring(line.indexOf(":") + 1).trim();
            } else if (line.startsWith("Age:")) {
                age = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
            } else if (line.startsWith("Email:")) {
                email = line.substring(line.indexOf(":") + 1).trim();
            } else if (line.startsWith("Phone:")) {
                phone = Long.parseLong(line.substring(line.indexOf(":") + 1).trim());
            }
        }

        return new Profile(name, age, email, phone);
    }
}

