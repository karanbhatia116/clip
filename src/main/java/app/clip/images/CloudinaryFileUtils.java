package app.clip.images;

import app.clip.commons.exceptions.ApplicationException;
import app.clip.images.models.FileDetails;
import com.cloudinary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;

@Service
public class CloudinaryFileUtils implements FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudinaryFileUtils.class);

    @Override
    public FileDetails uploadFile(byte[] file, String fileName) throws ApplicationException {
        Properties properties = System.getProperties();
        Cloudinary cloudinary = new Cloudinary((String) properties.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
        try {
            Map result = cloudinary.uploader().uploadLarge(file, Map.of(
                    "use_filename", true,
                    "overwrite", true,
                    "unique_filename", false,
                    "public_id", fileName
            ));
            return new FileDetails((String) result.get("secure_url"));
        }
        catch (Exception e) {
            LOGGER.error("Failed to upload file {} to cloudinary", fileName, e);
            throw new ApplicationException("Failed to upload file " + fileName + " to cloudinary", e);
        }
    }
}
