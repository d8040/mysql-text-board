package com.sbs.example.mysqlTextBoard.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Util {

	public static void writeFileContents(String filePath, String contents) {
		BufferedOutputStream bs = null;

		try {
			bs = new BufferedOutputStream(new FileOutputStream(filePath));
			bs.write(contents.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		} finally {
			try {
				bs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void writeFile(String path, String body) {
		File file = new File(path);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(body);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void mkdirs(String path) {
		File HtmlFolder = new File(path);

		if (HtmlFolder.exists() == false) {
			HtmlFolder.mkdir();
		}
	}

	public static String getFileContents(String filePath) {
		String rs = null;
		try {
			// 바이트 단위로 파일읽기
			FileInputStream fileStream = null; // 파일 스트림

			fileStream = new FileInputStream(filePath);// 파일 스트림 생성
			// 버퍼 선언
			byte[] readBuffer = new byte[fileStream.available()];
			while (fileStream.read(readBuffer) != -1) {
			}

			rs = new String(readBuffer);

			fileStream.close(); // 스트림 닫기
		} catch (Exception e) {
			e.getStackTrace();
		}

		return rs;
	}

	public static boolean rmdir(String path) {
		return rmdir(new File(path));
	}

	public static boolean rmdir(File dirToBeDeleted) {
		File[] allContents = dirToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				rmdir(file);
			}
		}

		return dirToBeDeleted.delete();
	}

	public static boolean copy(String sourcePath, String destPath) {
		Path source = Paths.get(sourcePath);
		Path target = Paths.get(destPath);

		if (!Files.exists(target.getParent())) {
			try {
				Files.createDirectories(target.getParent());
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		try {
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			return true;
		}
		return true;
	}

	public static void forderCopy(String selected, String copy) {
		File original_dir = new File("C:\\work\\sts-4.8.1.RELEASE-workspace\\mysql-text-board\\"+ selected); // 절대경로
		File move_dir = new File("C:\\work\\sts-4.8.1.RELEASE-workspace\\mysql-text-board\\" + copy);
		if (original_dir.exists()) { // 폴더의 내용물 확인 -> 폴더 & 파일..
			File[] fileNames = original_dir.listFiles(); // 내용 목록 반환
			// System.out.println("--------------폴더 읽기-----------------");
			// for(int i=0; i< fileNames.length; i++) {
			// if(fileNames[i].isDirectory()) {
			// System.out.println(fileNames[i].getName()); //폴더 존재 유무
			// }
			// }
			System.out.println("--------------파일 읽기-----------------");
			for (int i = 0; i < fileNames.length; i++) {
				if (fileNames[i].isFile()) {
					if (fileNames[i].exists()) {
						if (original_dir.exists()) {
						}
						File MoveFile = new File(move_dir, fileNames[i].getName()); // 이동될 파일 경로 및 파일 이름
						fileNames[i].renameTo(MoveFile); // 변경(이동)
						System.out.println(fileNames[i].getName()); // 폴더내에 있는 파일 리스트

					}
				}
			}
		}
	}

}
