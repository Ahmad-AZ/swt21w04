package festivalmanager.ticketShop.sender;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Objects;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;


@RestController()
public class TicketSender {


	@Autowired
	private JavaMailSender mailSender;

	String email= "festivalmanager2@gmail.com";
	String subject = "FVIV Ticket sender";
	String body = "sent by FVIV GmbH , ticket conformation ";
	String attachment = ""; // path of the pdf


	@GetMapping("/sendEmail")
	public void sendEmail() throws MessagingException {




		// for sending emails with attachment
		MimeMessage message = mailSender.createMimeMessage();
		// to set the properties
		MimeMessageHelper messageHelper= new MimeMessageHelper(message, true);

		messageHelper.setFrom(email);
		messageHelper.setTo(""); // example email
		messageHelper.setSubject(subject);
		messageHelper.setText(body);



		FileSystemResource fileSystemResource= new FileSystemResource(new File(attachment));

		if (Objects.nonNull(fileSystemResource.getFilename())) {
			messageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
		}


		mailSender.send(message);

	}

	@PostMapping(value = "/export")
	public String generatePdf(@RequestParam("base64") String base64){


		System.out.println("baseeeeeeeeeeeeeeeeeee" + base64);





		File file = new File("./ticket.pdf");

		try (FileOutputStream fileOutput= new FileOutputStream(file)){

		String b64 = "";
		byte[] decoder= Base64.getDecoder().decode(b64);
		fileOutput.write(decoder);
		}
		catch (Exception e){
			e.printStackTrace();
		}

		return ""; //return the file
	}

}
