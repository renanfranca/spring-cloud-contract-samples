package com.example;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(classes = ClientApplication.class, webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ContextConfiguration(initializers = {WireMockInitializer.class})
//remove::start[]
@AutoConfigureWireMock(stubs = "classpath:/META-INF/com.example/beer-api-producer-restdocs/**/*.json")
//remove::end[]
public class BeerControllerTest extends AbstractTest {

	@Autowired MockMvc mockMvc;
	@Autowired BeerController beerController;

	@Test public void should_give_me_a_beer_when_im_old_enough() throws Exception {
		//remove::start[]
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.json.write(new Person("marcin", 22)).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("THERE YOU GO"));
		//remove::end[]
	}

	@Test public void should_reject_a_beer_when_im_too_young() throws Exception {
		//remove::start[]
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.json.write(new Person("marcin", 17)).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("GET LOST"));
		//remove::end[]
	}
}