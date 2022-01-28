package festivalmanager.ticketShop.qr_code;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
 public class QRCodeGenerator {



	private  static final int  WIDTH= 100;
	private static final int  HEIGHT =100;
	private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/static/resources/img/qr_code/QRCode.png";



	public static void generateQRCodeImage(String text)

			throws WriterException, IOException {

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
		Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

	}



}