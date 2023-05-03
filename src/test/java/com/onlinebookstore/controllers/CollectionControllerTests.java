package com.onlinebookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.dao.CollectionDao;
import com.onlinebookstore.domain.CollectionEntity;
import com.onlinebookstore.models.CollectionDTO;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CollectionControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CollectionDao collectionsRepository;
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before("")
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void testAddNewCollection() throws Exception {
        CollectionDTO collectionDTO = new CollectionDTO();
        collectionDTO.setName("Пригоди");
        collectionDTO.setDescription("Книги про неймовірні пригоди героїв");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/collection/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(collectionDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        CollectionEntity createdCollection = objectMapper.readValue(response, CollectionEntity.class);

        assertEquals(collectionDTO.getName(), createdCollection.getName());
        assertEquals(collectionDTO.getDescription(), createdCollection.getDescription());

        // Checking adding, but we do request straight to repository (DB)
        CollectionEntity savedCollection = collectionsRepository.findById(createdCollection.getId()).orElse(null);
        assertNotNull(savedCollection);
        assertEquals(createdCollection.getName(), savedCollection.getName());
        assertEquals(createdCollection.getDescription(), savedCollection.getDescription());
    }


    @Test
    public void testUpdatingCollection() throws Exception {
        CollectionDTO collectionDTO = new CollectionDTO();
        collectionDTO.setName("Детективи");
        collectionDTO.setDescription("Книги про розслідування злочинів");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/collection/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(collectionDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        CollectionEntity createdCollection = objectMapper.readValue(response, CollectionEntity.class);

        CollectionDTO updatedCollectionDTO = new CollectionDTO();
        updatedCollectionDTO.setName("Мелодрами");
        updatedCollectionDTO.setDescription("Книги про любов");

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/collection/update/{id}", createdCollection.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCollectionDTO)))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();
        assertEquals("Інформацію про категорію книг оновлено!", response);

        CollectionEntity updatedCollectionEntity = collectionsRepository.findById(createdCollection.getId()).orElse(null);
        assertNotNull(updatedCollectionEntity);
        assertEquals(updatedCollectionDTO.getName(), updatedCollectionEntity.getName());
        assertEquals(updatedCollectionDTO.getDescription(), updatedCollectionEntity.getDescription());
    }


    @Test
    public void testDeleteCollection() throws Exception {
        CollectionDTO collectionDTO = new CollectionDTO();
        collectionDTO.setName("Комедія");
        collectionDTO.setDescription("Опис...");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/collection/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(collectionDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        CollectionEntity createdCollection = objectMapper.readValue(response, CollectionEntity.class);

        mockMvc.perform(delete("/collections/delete/" + createdCollection.getId()))
                .andExpect(status().isOk());

        CollectionEntity deletedCollection = collectionsRepository.findById(createdCollection.getId()).orElse(null);
        assertNull(deletedCollection);
    }

}
