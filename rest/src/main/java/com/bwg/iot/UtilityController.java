package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@RepositoryRestController
@RequestMapping("/util")
public class UtilityController {

    private final Environment environment;

    @Autowired
    public UtilityController(Environment env) {
        this.environment = env;
    };

    @RequestMapping(method = RequestMethod.GET, value = "/systemSettings")
    public @ResponseBody ResponseEntity<?> getSystemSettings() {
        SystemSettingsPayload payload = new SystemSettingsPayload(environment);
        return new ResponseEntity<SystemSettingsPayload>(payload, HttpStatus.OK);
    }

    public class SystemSettingsPayload extends ResourceSupport {
        private String profiles_active;
        private String max_file_size;
        private String log_file;
        private String idm_domain;
        private String uma_metadata_url;
        private String uma_aat_client_id;
        private String uma_openid_keys_filename;
        
        public SystemSettingsPayload(Environment env) {
            profiles_active = env.getProperty(PropertyNames.ACTIVE_PROFILES);
            log_file = env.getProperty(PropertyNames.LOG_FILE);
            max_file_size = env.getProperty(PropertyNames.MAX_FILE_SIZE);
            idm_domain = env.getProperty(PropertyNames.IDM_DOMAIN);
            uma_metadata_url = env.getProperty(PropertyNames.UMA_METADATA_URL);
            uma_aat_client_id = env.getProperty(PropertyNames.UMA_AAT_CLIENT_ID);
            uma_openid_keys_filename = env.getProperty(PropertyNames.UMA_OPENID_KEYS_FILENAME);

            this.add(new Link(uma_metadata_url).withRel("umaMetadata"));
            this.add(linkTo(UtilityController.class).slash("/systemSettings/").withSelfRel());
        }

        public String getProfiles_active() {
            return profiles_active;
        }

        public String getMax_file_size() {
            return max_file_size;
        }

        public String getLog_file() {
            return log_file;
        }

        public String getIdm_domain() {
            return idm_domain;
        }

        public String getUma_metadata_url() {
            return uma_metadata_url;
        }

        public String getUma_aat_client_id() {
            return uma_aat_client_id;
        }

        public String getUma_openid_keys_filename() {
            return uma_openid_keys_filename;
        }

        public void setProfiles_active(String profiles_active) {
            this.profiles_active = profiles_active;
        }

        public void setMax_file_size(String max_file_size) {
            this.max_file_size = max_file_size;
        }

        public void setLog_file(String log_file) {
            this.log_file = log_file;
        }

        public void setIdm_domain(String idm_domain) {
            this.idm_domain = idm_domain;
        }

        public void setUma_metadata_url(String uma_metadata_url) {
            this.uma_metadata_url = uma_metadata_url;
        }

        public void setUma_aat_client_id(String uma_aat_client_id) {
            this.uma_aat_client_id = uma_aat_client_id;
        }

        public void setUma_openid_keys_filename(String uma_openid_keys_filename) {
            this.uma_openid_keys_filename = uma_openid_keys_filename;
        }
    }
}
