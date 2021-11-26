package festivalmanager.location;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

class NewLocationForm {
	
	private final String UPLOAD_DIR = Paths.get("src\\main\\resources\\static\\resources\\img\\location").toAbsolutePath().toString()+ "\\";
	
	@NotEmpty   
	private final String name; 
	
	@NotEmpty  
	private final String adress;
	
 
	private final MultipartFile image;
	

	private final MultipartFile groundView;
	
	@Min(value = 0) 
	private final Double pricePerDay;

	@Min(value = 0)  
	private final Long visitorCapacity;
	
	@Min(value = 0) 
	private final Long stageCapacity;
		
	public NewLocationForm(String name, String adress, Double pricePerDay, Long visitorCapacity, Long stageCapacity, MultipartFile image, MultipartFile groundView) {
		this.name = name;
		this.adress = adress;
		this.pricePerDay = pricePerDay;
		this.visitorCapacity = visitorCapacity;
		this.stageCapacity = stageCapacity;
		this.image = image;
		this.groundView = groundView;
	}     
	
	public String getName() {
		return name;
	}

	public String getAdress() {
		return adress;
	}

	public double getPricePerDay() {
		return pricePerDay;
	}

	public Long getVisitorCapacity() {
		return visitorCapacity;
	}

	public Long getStageCapacity() {
		return stageCapacity;
	}

	public String getImage() {
        // normalize the file path
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());

        // save the file on the local file system
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            System.out.println(path);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

		return fileName.substring(0, fileName.length()-4);
		//return image;
	}

	public String getGroundView() {
        // normalize the file path
        String fileName = StringUtils.cleanPath(groundView.getOriginalFilename());
        // save the file on the local file system
        try {
        	Path path = Paths.get(UPLOAD_DIR + fileName);    
            Files.copy(groundView.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

		return fileName.substring(0, fileName.length()-4);
		//return groundView;
	}


}
