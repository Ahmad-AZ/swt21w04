package festivalmanager.ticketShop.qr_code;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
public class QRCodeController {

	private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/qrCodeImg/QRCode.png";


	@GetMapping(value = "/genrateAndDownloadQRCode/{uuidText}")
	public void download(
			@PathVariable("uuidText") String uuidText)
			throws Exception {

		QRCodeGenerator.generateQRCodeImage(uuidText, QR_CODE_IMAGE_PATH);


	}

	@GetMapping(value = "/genrateQRCode/{uuidText}")
	public ResponseEntity<byte[]> generateQRCode( @PathVariable("uuidText") String uuidText)
			throws Exception {

		return ResponseEntity.status(HttpStatus.OK).body(QRCodeGenerator.getQRCodeImage(uuidText));

	}
}