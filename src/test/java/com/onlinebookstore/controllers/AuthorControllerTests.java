package com.onlinebookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.dao.AuthorDao;
import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.models.AuthorDTO;
import com.onlinebookstore.services.AuthorsService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AuthorControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AuthorDao authorsRepository;
    private AuthorsService authorsService;
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before("")
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void testAddNewAuthor() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setFirstName("Іван");
        authorDTO.setLastName("Франко");
        authorDTO.setBio("Відомий український письменник, поет, громадський і політичний діяч, учитель");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/authors/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        AuthorEntity createdAuthor = objectMapper.readValue(response, AuthorEntity.class);

        AuthorEntity foundAuthor = authorsRepository.findById(createdAuthor.getId()).orElse(null);
        assertNotNull(foundAuthor);
        assertEquals(authorDTO.getFirstName(), foundAuthor.getFirstName());
        assertEquals(authorDTO.getLastName(), foundAuthor.getLastName());
        assertEquals(authorDTO.getBio(), foundAuthor.getBio());
    }


    @Test
    public void testUpdateAuthor() throws Exception {
        AuthorDTO author = new AuthorDTO();
        author.setFirstName("Іван");
        author.setLastName("Франко");
        author.setBio("Версія біо 1 ...");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/authors/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated())
                .andReturn();

        AuthorEntity createdAuthor = objectMapper.readValue(result.getResponse().getContentAsString(), AuthorEntity.class);

        // update the author
        AuthorDTO updatedAuthor = new AuthorDTO();
        updatedAuthor.setFirstName("Тарас");
        updatedAuthor.setLastName("Шевченко");
        updatedAuthor.setBio("Версія біо 1 ...");

        mockMvc.perform(MockMvcRequestBuilders.put("/authors/update/{id}", createdAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthor)))
                .andExpect(status().isOk());

        // verifying...
        AuthorEntity updatedAuthorEntity = authorsRepository.findById(createdAuthor.getId()).orElse(null);
        assertNotNull(updatedAuthorEntity);
        assertEquals(updatedAuthor.getFirstName(), updatedAuthorEntity.getFirstName());
        assertEquals(updatedAuthor.getLastName(), updatedAuthorEntity.getLastName());
        assertEquals(updatedAuthor.getBio(), updatedAuthorEntity.getBio());
    }


    @Test
    public void testDeleteAuthor() throws Exception {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setFirstName("Іван");
        authorDTO.setLastName("Франко");
        authorDTO.setBio("Український письменник, поет, громадський і політичний діяч");
        AuthorEntity author = authorsService.addNewAuthor(authorDTO);

        // Виконання запиту на видалення автора
        mockMvc.perform(delete("/authors/delete/{id}", author.getId()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().string(containsString("Автор був успішно видалений з бази.")));

        // Перевірка, що автора видалено з бази
        AuthorEntity deletedAuthor = authorsRepository.findById(author.getId()).orElse(null);
        assertNull(deletedAuthor);
    }
}
