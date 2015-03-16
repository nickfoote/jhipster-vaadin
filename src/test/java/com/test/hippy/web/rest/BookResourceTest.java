package com.test.hippy.web.rest;

import com.test.hippy.Application;
import com.test.hippy.domain.Book;
import com.test.hippy.repository.BookRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookResource REST controller.
 *
 * @see BookResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BookResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_PUB_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_PUB_DATE = new LocalDate();

    private static final BigDecimal DEFAULT_PRICE = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_PRICE = BigDecimal.ONE;

    @Inject
    private BookRepository bookRepository;

    private MockMvc restBookMockMvc;

    private Book book;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookResource bookResource = new BookResource();
        ReflectionTestUtils.setField(bookResource, "bookRepository", bookRepository);
        this.restBookMockMvc = MockMvcBuilders.standaloneSetup(bookResource).build();
    }

    @Before
    public void initTest() {
        book = new Book();
        book.setName(DEFAULT_NAME);
        book.setDescription(DEFAULT_DESCRIPTION);
        book.setPubDate(DEFAULT_PUB_DATE);
        book.setPrice(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createBook() throws Exception {
        // Validate the database is empty
        assertThat(bookRepository.findAll()).hasSize(0);

        // Create the Book
        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isCreated());

        // Validate the Book in the database
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(1);
        Book testBook = books.iterator().next();
        assertThat(testBook.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBook.getPubDate()).isEqualTo(DEFAULT_PUB_DATE);
        assertThat(testBook.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void getAllBooks() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the books
        restBookMockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(book.getId().intValue()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME.toString()))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.[0].pubDate").value(DEFAULT_PUB_DATE.toString()))
                .andExpect(jsonPath("$.[0].price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(book.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.pubDate").value(DEFAULT_PUB_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Update the book
        book.setName(UPDATED_NAME);
        book.setDescription(UPDATED_DESCRIPTION);
        book.setPubDate(UPDATED_PUB_DATE);
        book.setPrice(UPDATED_PRICE);
        restBookMockMvc.perform(put("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(1);
        Book testBook = books.iterator().next();
        assertThat(testBook.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBook.getPubDate()).isEqualTo(UPDATED_PUB_DATE);
        assertThat(testBook.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void deleteBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get the book
        restBookMockMvc.perform(delete("/api/books/{id}", book.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(0);
    }
}
