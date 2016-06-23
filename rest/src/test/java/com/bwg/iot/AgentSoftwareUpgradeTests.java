package com.bwg.iot;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by holow on 6/16/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IotServicesApplication.class)
@WebAppConfiguration
public class AgentSoftwareUpgradeTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void softwareVersionNotFound() throws Exception {
        final File swUpgradeFolder = new File("./sw_upgrade");
        swUpgradeFolder.mkdirs();
        final File swUpgradePackage = new File(swUpgradeFolder, "gateway_agent.11.tar.gz");
        IOUtils.copy(new ByteArrayInputStream(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1}), new FileOutputStream(swUpgradePackage));

        this.mockMvc
                .perform(get("/sw_upgrade").param("currentBuildNumber", "9"))
                .andDo(print()).andExpect(status().is(200)).andExpect(content().bytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1}));

        swUpgradePackage.delete();
        swUpgradeFolder.delete();
    }

    @Test
    public void softwareVersionFound() throws Exception {
        final File swUpgradeFolder = new File("./sw_upgrade");
        swUpgradeFolder.mkdirs();
        final File swUpgradePackage = new File(swUpgradeFolder, "gateway_agent.10.tar.gz");
        IOUtils.copy(new ByteArrayInputStream(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0}), new FileOutputStream(swUpgradePackage));

        this.mockMvc
                .perform(get("/sw_upgrade").param("currentBuildNumber", "10"))
                .andDo(print()).andExpect(status().is(204));

        swUpgradePackage.delete();
        swUpgradeFolder.delete();
    }
}
