package com.bwg.iot;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * Holds info about latest gateway agent software build and is able to return software package if requesting party
 * has different version.
 */
@RestController
@RequestMapping("/sw_upgrade")
public class AgentSoftwareUpgradeController {


    @Value("${agentSoftwareUpgradePackageFolder:./sw_upgrade}")
    private String agentSoftwareUpgradePackageFolder;

    @Value("${agentSoftwareUpgradePackageFileNameTemplate:gateway_agent.{0}.tar.gz}")
    private String agentSoftwareUpgradePackageFileNameTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public void getSoftwareUpgradePackage(
            @RequestParam("currentBuildNumber") final String buildNumber, final HttpServletResponse response) {

        final File softwareUpgradePackageFile = getSoftwareUpgradePackageFile(buildNumber);
        if (!softwareUpgradePackageFile.exists()) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            try (final InputStream is = new FileInputStream(softwareUpgradePackageFile)) {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();

            } catch (final Exception e) {
                throw new RuntimeException("Error while returning software upgrade package");
            }
        }
    }

    private File getSoftwareUpgradePackageFile(final String version) {
        final StringBuilder sb = new StringBuilder();
        sb.append(agentSoftwareUpgradePackageFolder);
        if (!agentSoftwareUpgradePackageFolder.endsWith(File.separator)) {
            sb.append(File.separator);
        }
        final String fileName = MessageFormat.format(agentSoftwareUpgradePackageFileNameTemplate, version);
        sb.append(fileName);
        return new File(sb.toString());
    }
}