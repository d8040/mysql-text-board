package com.sbs.example.mysqlTextBoard.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbs.example.mysqlTextBoard.apidto.DisqusApiDataListThread;

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

	public static String callApi(String urlStr, String... args) {
		// URL 구성 시작
		StringBuilder queryString = new StringBuilder();

		for (String param : args) {
			if (queryString.length() == 0) {
				queryString.append("?");
			} else {
				queryString.append("&");
			}

			queryString.append(param);
		}

		urlStr += queryString.toString();
		// URL 구성 끝

		// 연결생성 시작
		HttpURLConnection con = null;

		try {
			URL url = new URL(urlStr);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000); // 최대통신시간 제한
			con.setReadTimeout(5000); // 최대데이터읽기시간 제한
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		// 연결생성 끝

		// 연결을 통해서 데이터 가져오기 시작
		StringBuffer content = null;
		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			String inputLine;
			content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 연결을 통해서 데이터 가져오기 끝

		return content.toString();
	}

	public static Map<String, Object> callApiResponseToMap(String urlStr, String... args) {
		String jsonString = callApi(urlStr, args);
		
		ObjectMapper mapper = new ObjectMapper();

		try {
			return (Map<String, Object>) mapper.readValue(jsonString, Map.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static Object callApiResponseTo(Class cls, String urlStr, String... args) {
		String jsonString = callApi(urlStr, args);

		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.readValue(jsonString, cls);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}
}
