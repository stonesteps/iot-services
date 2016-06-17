package com.bwg.iot;

import com.bwg.iot.model.Attachment;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by holow on 5/18/2016.
 */
@RepositoryRestController
@ExposesResourceFor(Attachment.class)
@RequestMapping("/attachments")
public class AttachmentController implements ResourceProcessor<RepositoryLinksResource> {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private EntityLinks entityLinks;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> listAll(final Pageable pageable) {

        final PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), getSort(pageable.getSort()));
        final Page<Attachment> attachments = attachmentRepository.findAll(pageRequest);

        final Resources<Attachment> resources = new Resources<>(attachments);
        resources.add(linkTo(methodOn(AttachmentController.class).listAll(pageable)).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    private Sort getSort(final Sort sort) {
        return (sort != null) ? sort : new Sort(Sort.Direction.DESC, "created");
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource<Attachment>> uploadMultipart(@RequestParam("name") final String name,
                                                                @RequestParam("attachmentFile") final MultipartFile attachmentFile) {

        final Attachment attachment = new Attachment();
        attachment.setName(name);
        attachmentRepository.save(attachment);

        try (final InputStream in = attachmentFile.getInputStream()) {
            final String fileId = fileStorageService.storeFile(attachment.get_id(), in, null);
            if (fileId == null) {
                cleanup(attachment);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                attachment.setFileId(fileId);
                attachmentRepository.save(attachment);

                final Resource<Attachment> resource = new Resource<>(attachment);
                resource.add(entityLinks.linkToSingleResource(Attachment.class, attachment.get_id()).withSelfRel());

                return ResponseEntity.ok(resource);
            }
        } catch (final IOException e) {
            cleanup(attachment);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private void cleanup(final Attachment attachment) {
        if (attachment != null && attachment.get_id() != null) {
            attachmentRepository.delete(attachment);
        }
    }

    @RequestMapping(value = "/{attachmentId}", method = RequestMethod.GET)
    public ResponseEntity<Void> get(@PathVariable("attachmentId") final String attachmentId, final HttpServletResponse response) {

        final Attachment attachment = attachmentRepository.findOne(attachmentId);
        if (attachment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        response.setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
        response.setHeader("Content-Disposition", "attachment; filename=" + attachment.getName());

        try (final InputStream is = fileStorageService.getFileById(attachment.getFileId())) {
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (final IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(value = "/{attachmentId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("attachmentId") final String attachmentId) {

        final Attachment attachment = attachmentRepository.findOne(attachmentId);
        if (attachment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        fileStorageService.delete(attachment.getFileId());
        attachmentRepository.delete(attachment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        resource.add(ControllerLinkBuilder.linkTo(AttachmentController.class).withRel("attachments"));
        return resource;
    }
}
