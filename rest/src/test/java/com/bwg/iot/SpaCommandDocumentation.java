/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bwg.iot;

import com.bwg.iot.model.SpaCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IotServicesApplication.class)
@WebAppConfiguration
public final class SpaCommandDocumentation {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private SpaCommandRepository spaCommandRepository;

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
    public void setDesiredTempExample() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_DESIRED_TEMP, "101");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "myspacommand000001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setDesiredTemp").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-desired-temp-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_DESIRED_TEMP).description("The desired temperatue in degrees Fahrenheit (int)"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-desired-temp-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The set temperature"))));
    }

    @Test
    public void turnJetOn() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_DEVICE_NUMBER, "0");
        command.put(SpaCommand.REQUEST_DESIRED_STATE, "LOW");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setJetState").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-pump-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_DEVICE_NUMBER).description("The pump number (starting with 0)"),
                                fieldWithPath(SpaCommand.REQUEST_DESIRED_STATE).description("Pump state options: OFF, LOW or HIGH"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-pump-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The target state of the jets"))));
    }

    @Test
    public void turnCircPumpOn() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_DESIRED_STATE, "LOW");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setCircPumpState").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-circpump-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_DESIRED_STATE).description("Circ pump state options: OFF, LOW or HIGH"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-circpump-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The target state of the jets"))));
    }

    @Test
    public void turnLightOn() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_DEVICE_NUMBER, "0");
        command.put(SpaCommand.REQUEST_DESIRED_STATE, "MED");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0002");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setLightState").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-light-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_DEVICE_NUMBER).description("The light number (starting with 0)"),
                                fieldWithPath(SpaCommand.REQUEST_DESIRED_STATE).description("Light state options: OFF, LOW, MED or HIGH"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-light-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The target state of the light"))));
    }

    @Test
    public void turnBlowerOn() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_DEVICE_NUMBER, "0");
        command.put(SpaCommand.REQUEST_DESIRED_STATE, "LOW");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setBlowerState").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-blower-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_DEVICE_NUMBER).description("The blower number (starting with 0)"),
                                fieldWithPath(SpaCommand.REQUEST_DESIRED_STATE).description("Blower state options: OFF. LOW, MED or HIGH"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-blower-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The target state of the device"))));
    }

    @Test
    public void turnMisterOn() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_DEVICE_NUMBER, "0");
        command.put(SpaCommand.REQUEST_DESIRED_STATE, "ON");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setMisterState").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-mister-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_DEVICE_NUMBER).description("The mister number (starting with 0)"),
                                fieldWithPath(SpaCommand.REQUEST_DESIRED_STATE).description("Turn mister: ON or OFF"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-mister-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The target state"))));
    }

    @Test
    public void turnOzoneOn() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_DESIRED_STATE, "ON");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setOzoneState").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-ozone-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_DESIRED_STATE).description("Turn ozone: ON or OFF"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-ozone-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The target ozone setting"))));
    }

    @Test
    public void turnMicrosilkOn() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_DESIRED_STATE, "ON");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setMicrosilkState").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-microsilk-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_DESIRED_STATE).description("Turn microsilk: ON or OFF"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-microsilk-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The target state"))));
    }

    @Test
    public void turnAuxOn() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_DESIRED_STATE, "ON");
        command.put(SpaCommand.REQUEST_DEVICE_NUMBER, "0");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setAuxState").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-aux-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_DESIRED_STATE).description("Turn aux: ON or OFF"),
                                fieldWithPath(SpaCommand.REQUEST_DEVICE_NUMBER).description("Aux number, starting with 0"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-aux-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The target state of the device"))));
    }

    @Test
    public void setAgentSettings() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put("INTERVAL_SECONDS", "120");
        command.put("DURATION_MINUTES", "60");
        command.put("RS485_CONTROLLER_TYPE", "NGSC");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setAgentSettings").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-agentsettings-example",
                        requestFields(fieldWithPath("INTERVAL_SECONDS").description("optional, only required if duration is specified, max frequency that agent will submit spa state messages"),
                                fieldWithPath("DURATION_MINUTES").description("optional, only required if interval is specified, length of time to support INTERVAL_SECONDS, or set to -1 to make permanent"),
                                fieldWithPath("RS485_CONTROLLER_TYPE").description("optional, set to NGSC or JACUZZI for the controller type that Agent is connected to."),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-agentsettings-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The target state of the device"))));
    }

    @Test
    public void setFilterCycleInterval() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_DEVICE_NUMBER, "0");
        command.put(SpaCommand.REQUEST_FILTER_INTERVAL, "2");
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/setFilterCycleIntervals").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-filtercycle-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_FILTER_INTERVAL).description("The number of 15 minute long filter cycle interval on selected filter"),
                                fieldWithPath(SpaCommand.REQUEST_DEVICE_NUMBER).description("Filter number, starting with 0"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-filtercycle-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("values").description("The target state of the filter cycle"))));
    }

    @Test
    public void agentRestart() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/restartAgent").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-agentrestart-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-agentrestart-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"))));
    }

    @Test
    public void gatewayReboot() throws Exception {
        this.spaCommandRepository.deleteAll();

        final Map<String, String> command = new HashMap<>();
        command.put(SpaCommand.REQUEST_ORIGINATOR, "optional-tag-0001");

        this.mockMvc
                .perform(post("/control/56c7f020c2e65656ab93db17/rebootGateway").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(command)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("control-gatewayreboot-example",
                        requestFields(fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("Optional tag for tracking request").optional())))
                .andDo(document("control-gatewayreboot-response-example",
                        responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
                                fieldWithPath("requestTypeId").description("The type of request"),
                                fieldWithPath(SpaCommand.REQUEST_ORIGINATOR).description("A unique id for this request"),
                                fieldWithPath("sentTimestamp").description("The time the command was sent"),
                                fieldWithPath("processedTimestamp").description("The time the command was processed").optional().type("String"),
                                fieldWithPath("processedResult").description("Indicates if processing was successful or not").optional().type("String"),
                                fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command").optional().type("String"),
                                fieldWithPath("metadata").description("Extra info related to the command").optional().type("HashMap"),
                                fieldWithPath("ackResponseCode").description("The ack response from device").optional().type("String"))));
    }
}
