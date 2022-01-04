package festivalmanager.ticketShop.qr_code;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class QRCodeController {



	@GetMapping(value = "/genrateAndDownloadQRCode/{uuidText}")
	public void download(
			@PathVariable("uuidText") String uuidText)
			throws Exception {

		QRCodeGenerator.generateQRCodeImage(uuidText);

	}


	@GetMapping(value = "/genrateQRCode/{uuidText}")
	public ResponseEntity<byte[]> generateQRCode( @PathVariable("uuidText") String uuidText)
			throws Exception {

		return ResponseEntity.status(HttpStatus.OK).body(QRCodeGenerator.getQRCodeImage(uuidText));

	}
}