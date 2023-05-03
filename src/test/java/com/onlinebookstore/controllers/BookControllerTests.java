package com.onlinebookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.dao.BookDao;
import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.models.BookDTO;
import com.onlinebookstore.services.BooksService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.reflect.Array.get;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class BookControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private BookDao booksRepository;
    @Autowired
    private BooksService booksService;
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before("")
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddNewBookWithAllFields() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Таємничий острів");
        bookDTO.setDescription("Один із найвідоміших пригодницьких романів XX сторіччя, створених Жулем Верном. " +
                "Описує подорож експедиції у невивчену місцевість, яка раптом йде зовсім не так, як було заплановано...");
        bookDTO.setAuthorFirstName("Жуль");
        bookDTO.setAuthorLastName("Верн");
        bookDTO.setPrice(new BigDecimal("499.99"));
        bookDTO.setQuantity(50);
        bookDTO.setCoverImage("https://example.com/testaments.jpg".getBytes());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        BookEntity createdBook = objectMapper.readValue(response, BookEntity.class);

        mvcResult = mockMvc.perform((RequestBuilder) get("/books/{bookId}", createdBook.getId()))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();
        BookDTO resultBook = objectMapper.readValue(response, BookDTO.class);

        assertEquals(bookDTO.getTitle(), resultBook.getTitle());
        assertEquals(bookDTO.getDescription(), resultBook.getDescription());
        assertEquals(bookDTO.getAuthorFirstName(), resultBook.getAuthorFirstName());
        assertEquals(bookDTO.getAuthorLastName(), resultBook.getAuthorLastName());
        assertEquals(bookDTO.getPrice(), resultBook.getPrice());
        assertEquals(bookDTO.getQuantity(), resultBook.getQuantity());
        assertEquals(bookDTO.getCoverImage(), resultBook.getCoverImage());
    }

    @Test
    public void testUpdateBook() throws Exception {
        // Create and post test book
        BookDTO testBook = new BookDTO();
        testBook.setTitle("Тестова книга");
        testBook.setDescription("Опис тестової книги");
        testBook.setPrice(new BigDecimal("10.00"));
        testBook.setQuantity(5);
        testBook.setAvailability(true);
        testBook.setCoverImage(new byte[]{1, 2, 3});

        mockMvc.perform(MockMvcRequestBuilders.post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isCreated());

        // getting test book ID (last added):
        List<BookEntity> books = booksRepository.findAll();
        Integer bookId = books.get(books.size() - 1).getId();

        // creating new data for book updating
        BookDTO updatedBook = new BookDTO();
        updatedBook.setDescription("Новий опис тестової книги");
        updatedBook.setPrice(new BigDecimal("20.00"));
        updatedBook.setQuantity(10);
        updatedBook.setAvailability(false);
        updatedBook.setCoverImage(new byte[]{4, 5, 6});

        // making PUT request for updating...
        MvcResult mvcResult = (MvcResult) mockMvc.perform(MockMvcRequestBuilders.put("/books/update/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk());

        // checking updated result is equal to updatedBook
        BookEntity updatedBookEntity = booksRepository.findById(bookId).orElse(null);
        assertNotNull(updatedBookEntity);
        assertEquals(updatedBook.getDescription(), updatedBookEntity.getDescription());
        assertEquals(updatedBook.getRating(), updatedBookEntity.getRating());
        assertEquals(updatedBook.getPrice(), updatedBookEntity.getPrice());
        assertEquals(updatedBook.getQuantity(), updatedBookEntity.getQuantity());
        assertEquals(updatedBook.getAvailability(), updatedBookEntity.getAvailability());
        assertArrayEquals(updatedBook.getCoverImage(), updatedBookEntity.getCoverImage());

        // checking response status...
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        // checking response body content...
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("Інформацію оновлено!"));
    }


    @Test
    public void testDeleteBookById() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Друге життя Уве");
        bookDTO.setDescription("Опис...");
        bookDTO.setAuthorFirstName("Фредерік");
        bookDTO.setAuthorLastName("Бакман");
        bookDTO.setPrice(new BigDecimal("299.99"));
        bookDTO.setQuantity(50);
        bookDTO.setCoverImage("https://example.com/testaments.jpg".getBytes());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        BookEntity createdBook = objectMapper.readValue(response, BookEntity.class);
        Integer bookId = createdBook.getId();

        mockMvc.perform(delete("/delete/{id}", bookId))
                .andExpect(status().isOk());

        // checking...
        BookEntity deletedBookEntity = booksRepository.findById(bookId).orElse(null);
        assertNull(deletedBookEntity);
    }

    // TESTING GET METHODS
    @Test
    public void testFindBooksByAuthor() throws Exception {
        String authorName = "Петров";
        String url = "/books/books_by_author?author=" + authorName;

        List<BookDTO> booksByAuthor = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            AuthorEntity author = new AuthorEntity();
            author.setId(i);
            author.setFirstName("Іван");
            author.setLastName(authorName);

            BookEntity book = new BookEntity();
            book.setId(i);
            book.setTitle("Книга " + i);
            book.setAuthorId(author.getId());

            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(i);
            bookDTO.setTitle("Книга " + i);
            bookDTO.setAuthorId(i);
            bookDTO.setAuthorFirstName("Іван");
            bookDTO.setAuthorLastName(authorName);

            booksByAuthor.add(bookDTO);

            when(booksService.getBooksDTOByAuthorLastName(eq(authorName), any(Pageable.class)))
                    .thenReturn(Collections.singletonList(bookDTO));
        }

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(3)))
                .andExpect((ResultMatcher) jsonPath("$[0].title", is("Книга 1")))
                .andExpect((ResultMatcher) jsonPath("$[0].authorFirstName", is("Іван")))
                .andExpect((ResultMatcher) jsonPath("$[0].authorLastName", is(authorName)))
                .andExpect((ResultMatcher) jsonPath("$[1].title", is("Книга 2")))
                .andExpect((ResultMatcher) jsonPath("$[1].authorFirstName", is("Іван")))
                .andExpect((ResultMatcher) jsonPath("$[1].authorLastName", is(authorName)))
                .andExpect((ResultMatcher) jsonPath("$[2].title", is("Книга 3")))
                .andExpect((ResultMatcher) jsonPath("$[2].authorFirstName", is("Іван")))
                .andExpect((ResultMatcher) jsonPath("$[2].authorLastName", is(authorName)));

        verify(booksService, times(1)).getBooksDTOByAuthorLastName(eq(authorName), any(Pageable.class));
    }

    @Test
    public void testFindBooksByTitle() throws Exception {
        String bookTitle = "Кобзар";
        String url = "/books/books_search?title=" + bookTitle;

        List<BookDTO> booksByTitle = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            BookEntity book = new BookEntity();
            book.setId(i);
            book.setTitle("Кобзар " + i);

            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(i);
            bookDTO.setTitle("Кобзар " + i);

            booksByTitle.add(bookDTO);

            when(booksService.getBooksDTOByAuthorLastName(eq(bookTitle), any(Pageable.class)))
                    .thenReturn(Collections.singletonList(bookDTO));
        }

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(3)))
                .andExpect((ResultMatcher) jsonPath("$[0].title", is("Кобзар 1")))
                .andExpect((ResultMatcher) jsonPath("$[1].title", is("Кобзар 2")))
                .andExpect((ResultMatcher) jsonPath("$[2].title", is("Кобзар 3")));

        verify(booksService, times(1)).getBooksDTOByTitle(eq(bookTitle), any(Pageable.class));
    }


    @Test
    public void testFindBooksByCollectionName() throws Exception {
        String collectionName = "Художня література";
        int page = 0;
        int size = 20;

        List<BookDTO> bookDTOList = new ArrayList<>();
        bookDTOList.add(new BookDTO());
        bookDTOList.add(new BookDTO());

        when(booksService.getBooksDTOByCategory(collectionName, PageRequest.of(page, size)))
                .thenReturn(bookDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/collection_books")
                        .param("collectionName", collectionName)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(2)));
    }




}
