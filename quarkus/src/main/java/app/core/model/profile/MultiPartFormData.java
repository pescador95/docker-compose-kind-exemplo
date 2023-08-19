package app.core.model.profile;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import javax.ws.rs.FormParam;

public class MultiPartFormData {
    @FormParam("file")
    @PartType("application/octet-stream")
    public FileUpload file;

    @FormParam("text")
    @PartType("text/plain")
    public String text;

    public FileUpload getFile() {
        return file;
    }
}
