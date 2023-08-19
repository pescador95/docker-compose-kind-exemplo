package app.core.controller.profile;

import app.core.model.DTO.Responses;
import app.core.model.profile.MultiPartFormData;
import app.core.model.profile.Profile;
import app.core.utils.BasicFunctions;
import app.quarkus.model.person.PersonalActivity;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
@Transactional
public class ProfileController {
    @ConfigProperty(name = "quarkus.http.body.uploads-directory")
    @Inject
    String directory;

    private Responses responses;

    public Profile findOne(Long id) {

        Profile profile = Profile.findById(id);

        if (BasicFunctions.isEmpty(profile)) {
            throw new RuntimeException("File not Found");
        }

        return profile;
    }

    public Response sendUpload(@NotNull MultiPartFormData file, String fileRefence, Long personalactivityId)
            throws IOException {

        responses = new Responses();
        responses.messages = new ArrayList<>();

        Profile profileCheck = Profile.find("personalactivityId = ?1", personalactivityId).firstResult();

        PersonalActivity personalActivity = PersonalActivity.findById(personalactivityId);

        if (BasicFunctions.isNotEmpty(personalActivity)) {
            if (BasicFunctions.isEmpty(profileCheck) || !profileCheck.originalName.equals(file.getFile().fileName())) {
                Profile profile = new Profile();
                List<String> mimetype = Arrays.asList("image/jpg", "image/jpeg", "application/msword",
                        "application/vnd.ms-excel", "application/xml",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "image/gif",
                        "image/png", "text/plain", "application/vnd.ms-powerpoint", "application/pdf", "text/csv",
                        "document/doc", "document/docx",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/zip",
                        "application/vnd.sealed.xls");

                if (!mimetype.contains(file.getFile().contentType())) {
                    throw new IOException(
                            "Unsupported file type. Only accept files in the formats: ppt, pptx, csv, doc, docx, txt, pdf, xlsx, xml, xls, jpg, jpeg, png e zip.");
                }

                if (file.getFile().size() > 1024 * 1024 * 4) {
                    throw new IOException("File too big.");
                }

                String fileName = personalactivityId + "-" + file.getFile().fileName();

                profile.originalName = file.getFile().fileName();

                profile.keyName = fileName;

                profile.mimetype = file.getFile().contentType();

                profile.fileSize = file.getFile().size();

                profile.createAt = LocalDateTime.now();

                profile.personalActivity = personalActivity;

                profile.personName = profile.personalActivity.person.name;

                profile.fileReference = fileRefence;

                profile.persist();

                Files.copy(file.getFile().filePath(), Paths.get(directory + fileName));
                responses.status = 200;
                responses.messages.add("File register with sucessful!");
                return Response.ok(responses).status(Response.Status.ACCEPTED).build();
            } else {
                responses.status = 400;
                responses.messages.add("Já existe um arquivo com o mesmo name. Verifique!");
                return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            responses.status = 400;
            responses.messages.add("Por favor, verifique o Histórico da Person do anexo.");
        }
        return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
    }

    public Response removeUpload(List<Long> pListIdProfile) {

        List<Profile> profiles;
        responses = new Responses();
        responses.messages = new ArrayList<>();
        profiles = Profile.list("id in ?1 and active = true", pListIdProfile);
        int count = profiles.size();

        try {
            profiles.forEach((profile) -> {

                try {
                    Profile.delete("id = ?1", profile.id);
                    Files.deleteIfExists(Paths.get(directory + profile.keyName));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            responses.status = 200;
            if (count <= 1) {
                responses.messages.add("File successfully deleted!");
            } else {
                responses.messages.add(count + " Files successfully deleted!");
            }
            return Response.ok(responses).status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            responses.status = 400;
            if (count <= 1) {
                responses.messages.add("File not found or already deleted.");
            } else {
                responses.messages.add("Files not found or already deleted.");
            }
            return Response.ok(responses).status(Response.Status.BAD_REQUEST).build();
        }
    }
}