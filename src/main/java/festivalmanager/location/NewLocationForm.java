package festivalmanager.location;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

class NewLocationForm {
	
	private final String uploadDir = Paths.get("locationImages").toAbsolutePath().toString()+ "\\";
	
	@NotEmpty   
	@NotNull
	@NotBlank
	private final String name; 
	
	@NotEmpty
	@NotNull
	@NotBlank
	private final String adress;
	
	@NotNull
	@NotEmpty
	@NotBlank
	private final String pricePerDay;
	
	@NotNull
	@Min(value = 0)  
	private final Long visitorCapacity;
	
	@NotNull
	@Min(value = 0) 
	private final Long stageCapacity;
	
	private final MultipartFile image;
	
	private final MultipartFile groundView;
		
	public NewLocationForm(String name, String adress, String pricePerDay,
						   Long visitorCapacity, Long stageCapacity, MultipartFile image,
						   MultipartFile groundView) {
		this.name = name;
		this.adress = adress;
		this.pricePerDay = pricePerDay;
		this.visitorCapacity = visitorCapacity;
		this.stageCapacity = stageCapacity;
		this.image = image;
		this.groundView = groundView;
	}     
	
	public String getName() {
		System.out.println(name);
		return name;
	}

	public String getAdress() {
		return adress;
	}

	public String getPricePerDay() {
		return pricePerDay;
	}

	public Long getVisitorCapacity() {
		return visitorCapacity;
	}

	public Long getStageCapacity() {
		return stageCapacity;
	}
	
	public MultipartFile getImageFile() {
		return image;
	}
	
	public MultipartFile getGroundViewFile() {
		return groundView;
	}

	public String getImage() {
        // normalize the file path
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());

        // save the file on the local file system
        try {
            Path path = Paths.get(uploadDir + fileName);
            System.out.println(path);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

		return fileName;
		//return image;
	}

	public String getGroundView() {
        // normalize the file path
        String fileName = StringUtils.cleanPath(groundView.getOriginalFilename());
        // save the file on the local file system
        try {
        	Path path = Paths.get(uploadDir + fileName);
            Files.copy(groundView.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

		return fileName;
		//return groundView;
	}


}
