package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.model.AuthorRepository;
import com.example.demo.model.Book;
import com.example.demo.model.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
public class BookController {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @RequestMapping("/")
    public String listCourses(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("books", bookRepository.findAll()); //generate select * statement
        return "list";
    }

    @GetMapping("/add")
    public String bookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorRepository.findAll());
        return "bookform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("authors", authorRepository.findAll());
            return "bookform";
        }
        bookRepository.save(book);//generate SQL statement and insert into database
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showBook(@PathVariable("id") long id, Model model) {
        model.addAttribute("book", bookRepository.findById(id).get());
        model.addAttribute("authors", authorRepository.findAll());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateBook(@PathVariable("id") long id, Model model) {
        model.addAttribute("book", bookRepository.findById(id).get());
        model.addAttribute("authors", authorRepository.findAll());
        return "bookform";
    }

    @RequestMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") long id){
        bookRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteBooks(@RequestParam("check") long[] ids){
        for(long id : ids){
            bookRepository.deleteById(id);
        }
        return "redirect:/";
    }

    @PostMapping("/search")
    public String searchword(Model model, @RequestParam String search) {
        ArrayList<Book> results = (ArrayList<Book>)
                bookRepository.findAllByTitleContainingOrDescriptionContainingAllIgnoreCase(search,search);
        model.addAttribute("books", results);
        model.addAttribute("authors", authorRepository.findAll());
        return "list";
    }

    @GetMapping("/about")
    public String getAbout(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        return "about";
    }
}
