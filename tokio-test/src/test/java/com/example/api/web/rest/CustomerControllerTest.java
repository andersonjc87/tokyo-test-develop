package com.example.api.web.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.HashSet;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class CustomerControllerTest {
	protected MockMvc mvc;
	@Autowired
	WebApplicationContext webApplicationContext;

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Test
	public void findAll_retorna2Clientes() throws Exception {
		String uri = "/customers";
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Customer[] lista = this.mapFromJson(content, Customer[].class);
		Assertions.assertThat(lista.length == 2);
		Customer item = lista[0];
		Assertions.assertThat(item.getName().equals("Joãozinho"));
		Assertions.assertThat(item.getEmail().equals("joaozinho@email.com"));
		Assertions.assertThat(item.getAddresses().isEmpty());
		
		item = lista[1];
		Assertions.assertThat(item.getName().equals("Mariazinha"));
		Assertions.assertThat(item.getEmail().equals("mariazinha@email.com"));
		Assertions.assertThat(!item.getAddresses().isEmpty());
		Address addr = item.getAddresses().iterator().next();
		
		Assertions.assertThat(addr.getStreet().equals("Bosque encantado"));
		Assertions.assertThat(addr.getCity().equals("Neverland"));
		Assertions.assertThat(addr.getState().equals("NL"));
		Assertions.assertThat(addr.getZipCode() == 90023888);
	}
	
	@Test
	public void save_gravaDados() throws Exception {
		String uri = "/customers";
		Address addr = new Address();
		addr.setId(2L);
		addr.setStreet("Rua Teste");
		addr.setCity("Cidade Teste");
		addr.setState("ET");
		addr.setZipCode(12345678L);
		Customer esp = new Customer("Carlos", "carlos@gmail.com");
		esp.setId(3L);
		esp.setAddresses(new HashSet<>());
		esp.getAddresses().add(addr);

		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.content(this.mapToJson(esp))
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString();
		Assert.assertEquals(200, status);
		Customer res = this.mapFromJson(content, Customer.class);

		assertThat(res.getName().equals(esp.getName()));
		
	}
}
