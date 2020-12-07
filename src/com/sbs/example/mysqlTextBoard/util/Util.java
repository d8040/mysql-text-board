package com.sbs.example.mysqlTextBoard.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {

	public static void writeFileContents(String filePath, String contents) {
		BufferedOutputStream bs = null;
		
		try {
			bs = new BufferedOutputStream(new FileOutputStream(filePath));
			bs.write(contents.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		} finally{
			try {
				bs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
