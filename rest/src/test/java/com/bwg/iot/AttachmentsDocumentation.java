package com.bwg.iot;

import com.bwg.iot.model.Attachment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by holow on 5/19/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IotServicesApplication.class)
@WebAppConfiguration
public class AttachmentsDocumentation extends ModelTestBase {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @Test
    public void uploadAttachmentExample() throws Exception {
        this.clearAllData();

        MockMultipartFile file = new MockMultipartFile("attachmentFile", "filename.txt", "text/plain", "some xml".getBytes());

        this.mockMvc
                .perform(MockMvcRequestBuilders.fileUpload("/attachments").file(file).param("name", "random name"))
                .andExpect(status().isOk())
                .andDo(document("attachment-upload-example"));

    }

    @Test
    public void getAttachmentExample() throws Exception {
        this.clearAllData();

        MockMultipartFile file = new MockMultipartFile("attachmentFile", "sample.pdf", ContentType.APPLICATION_OCTET_STREAM.getMimeType(), AttachmentsDocumentation.class.getResourceAsStream("/sample.pdf"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.fileUpload("/attachments").file(file).param("name", "sample.pdf"))
                .andExpect(status().isOk())
                .andReturn();

        Attachment attachment = attachmentRepository.findAll().get(0);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/attachments/{0}", attachment.get_id()))
                .andExpect(status().isOk())
                .andDo(document("attachment-get-example"));
    }

    @Test
    public void deleteAttachmentExample() throws Exception {
        this.clearAllData();

        MockMultipartFile file = new MockMultipartFile("attachmentFile", "filename.txt", "text/plain", "some xml".getBytes());
        this.mockMvc
                .perform(MockMvcRequestBuilders.fileUpload("/attachments").file(file).param("name", "random name"))
                .andExpect(status().isOk())
                .andReturn();

        Attachment attachment = attachmentRepository.findAll().get(0);

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/attachments/{0}", attachment.get_id()))
                .andExpect(status().isOk())
                .andDo(document("attachment-delete-example"));
    }
}
