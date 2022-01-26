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

/**
 * the form used to create the {@link Location}
 *
 * @author Adrian Scholze
 */
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
	
	/**
	 * Creates a new {@link NewLocationForm} with the given name, adress, price per day, 
	 * visitor capacity, stage capacity, image and groundview
	 *
	 * @param name must not be {@literal null}.
	 * @param adress must not be {@literal null}.
	 * @param pricePerDay must not be {@literal null}.
	 * @param visitorCapacity must not be {@literal null}
	 * @param stageCapacity must not be {@literal null}.
	 * @param image 
	 * @param groundView 
	 */
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
	
	/**
	 * Returns forms name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns forms adress.
	 * 
	 * @return adress
	 */
	public String getAdress() {
		return adress;
	}

	/**
	 * Returns forms price per day.
	 * 
	 * @return pricePerDay
	 */
	public String getPricePerDay() {
		return pricePerDay;
	}

	/**
	 * Returns forms visitor capacity.
	 * 
	 * @return visitorCapacity
	 */
	public Long getVisitorCapacity() {
		return visitorCapacity;
	}

	/**
	 * Returns forms stage capacity.
	 * 
	 * @return stageCapacity
	 */
	public Long getStageCapacity() {
		return stageCapacity;
	}
	
	/**
	 * Returns forms multipart image file.
	 * 
	 * @return image as multipart file
	 */
	public MultipartFile getImageFile() {
		return image;
	}
	
	/**
	 * Returns forms multipart groundview file.
	 * 
	 * @return groundView as multipart file
	 */
	public MultipartFile getGroundViewFile() {
		return groundView;
	}

	/**
	 * Returns forms image as filename string.
	 * 
	 * @return image 
	 */
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
	}

	/**
	 * Returns forms groundview as filename string.
	 * 
	 * @return groundView 
	 */
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
	}
}
