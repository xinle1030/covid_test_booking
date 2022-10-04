package fit3077.lab02team14.assignment2.services;

import java.io.*;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpStatus;

import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.imageio.ImageIO;

public class QRCode {

	static String path = "./src/main/resources/static/image/qrCodes/";

	public QRCode() {
	}

	public QRCode(String path) {
		QRCode.path = path;
	}

	public static String generateByteQRCode(String text, int width, int height) {
		ByteArrayOutputStream outputStream = null;
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		try {
			outputStream = new ByteArrayOutputStream();
			BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
			MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF);
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream, config);
		} catch (WriterException | IOException e) {
			e.printStackTrace();
		}
		byte[] retByte = outputStream.toByteArray();
		String encodedString = byteArrToStr(retByte);
		return encodedString;
	}

	public static String generateByteQRCode(String text) {
		return generateByteQRCode(text, 200, 200);
	}

	public static String prepareErrorJSON(HttpStatus status, Exception ex) {
		JSONObject errorJSON = new JSONObject();
		try {
			errorJSON.put("status", status.value());
			errorJSON.put("error", status.getReasonPhrase());
			errorJSON.put("message", ex.getMessage().split(":")[0]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return errorJSON.toString();
	}

	public static String byteArrToStr(byte[] byteArr) {
		return java.util.Base64.getEncoder().encodeToString(byteArr);
	}

	public static byte[] strToByteArr(String encodedString) {
		return java.util.Base64.getDecoder().decode(encodedString);
	}

	public static String byteArrToImgPath(String encodedString, String id) {
		byte[] byteArray = strToByteArr(encodedString);

		// create the object of ByteArrayInputStream class
		// and initialized it with the byte array.
		ByteArrayInputStream inStreambj = new ByteArrayInputStream(byteArray);

		// read image from byte array
		BufferedImage newImage = null;
		try {
			newImage = ImageIO.read(inStreambj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String retImgPath = "";

		// write output image
		try {
			String imgPath = path + "QRCode-" + id + ".jpg";
			ImageIO.write(newImage, "jpg", new File(imgPath));
			retImgPath = "./" + imgPath.substring(28);
			System.out.println("Image generated from the byte array.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Image cannot be generated from the byte array.");
		}
		
		return retImgPath;
	}

	public static String readQRCode(String path, String charset, Map map) throws IOException, NotFoundException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
		Result result = new MultiFormatReader().decode(binaryBitmap);
		return result.getText();
	}

}
