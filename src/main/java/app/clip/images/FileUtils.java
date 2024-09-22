package app.clip.images;

import app.clip.commons.exceptions.ApplicationException;
import app.clip.images.models.FileDetails;

public interface FileUtils {
    FileDetails uploadFile(byte[] file, String fileName) throws ApplicationException;
}
