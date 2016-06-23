package com.bwg.iot;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Holds info about latest gateway agent software build and is able to return software package if requesting party
 * has different version.
 */
@RestController
@RequestMapping("/sw_upgrade")
public class AgentSoftwareUpgradeController {

    private static final FileFilter TAR_GZ_ONLY = new FileFilter() {
        @Override
        public boolean accept(final File pathname) {
            return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".tar.gz");
        }
    };

    private static final Comparator<File> LAST_MODIFIED_COMPARATOR = new Comparator<File>() {
        @Override
        public int compare(final File f1, final File f2) {
            return Long.compare(f1.lastModified(), f2.lastModified());
        }
    };

    @Value("${agentSoftwareUpgradePackageFolder:./sw_upgrade}")
    private String agentSoftwareUpgradePackageFolder;

    @Value("${agentSoftwareUpgradePackageFileNameTemplate:gateway_agent.{0}.tar.gz}")
    private String agentSoftwareUpgradePackageFileNameTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public void getSoftwareUpgradePackage(
            @RequestParam("currentBuildNumber") final String buildNumber, final HttpServletResponse response) {

        final File softwareUpgradePackageFile = getSoftwareUpgradePackageFile(buildNumber);
        final File latestSoftwareUpgradeFile = getLatestSoftwareUpgradePackageFile();
        if (softwareUpgradePackageFile.exists() || latestSoftwareUpgradeFile == null) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            try (final InputStream is = new FileInputStream(latestSoftwareUpgradeFile)) {
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

    private File getLatestSoftwareUpgradePackageFile() {
        final File softwareUpgradeFolder = new File(agentSoftwareUpgradePackageFolder);
        if (softwareUpgradeFolder.exists() && softwareUpgradeFolder.isDirectory()) {
            final File[] contents = softwareUpgradeFolder.listFiles(TAR_GZ_ONLY);
            return latest(contents);
        }
        return null;
    }

    private File latest(File[] contents) {
        final File latest = null;

        if (contents != null && contents.length > 0) {
            final List<File> files = Arrays.asList(contents);
            Collections.sort(files, LAST_MODIFIED_COMPARATOR);
            return files.get(files.size() - 1);
        }

        return latest;
    }
}