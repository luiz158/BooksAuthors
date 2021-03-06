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

@Controller
public class AuthorController {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @RequestMapping("/authorlist")
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        return "authorslist";
    }

    @GetMapping("/addauthor")
    public String authorForm(Model model){
        model.addAttribute("author", new Author());
        model.addAttribute("authors", authorRepository.findAll());
        return "authorform";
    }

    @PostMapping("/processauthor")
    public String processAuthor(@Valid Author author, BindingResult result,
                                 Model model){
        if(result.hasErrors()){
            return "authorform";
        }
        if(authorRepository.findByName(author.getName()) != null){
            model.addAttribute("message", "You already have a subject called " +
                    author.getName() + "!" + " Try something else.");
            return "authorform";
        }
        authorRepository.save(author);
        return "redirect:/authorlist";
    }

    @RequestMapping("/detailauthor/{id}")
    public String showAuthor(@PathVariable("id") long id, Model model) {
        model.addAttribute("author", authorRepository.findById(id).get());
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("authorbooks", bookRepository.findAllByAuthors_Id(id));
        return "authorlist";
    }

    @RequestMapping("/updateauthor/{id}")
    public String updateAuthor(@PathVariable("id") long id, Model model) {
        model.addAttribute("author", authorRepository.findById(id).get());
        return "authorform";
    }

    @RequestMapping("/deleteauthor/{id}")
    public String deleteAuthor(@PathVariable("id") long id){
        Author author = authorRepository.findById(id).get();
        for(Book book :author.getBooks()){
            long book_id = book.getId();
//            bookRepository.deleteById(book_id);
        }
        authorRepository.deleteById(id);
        return "redirect:/authorlist";
    }

    @PostMapping("/deleteauthor")
    public String deleteAuthors(@RequestParam("check") long[] ids){
        for(long id : ids){
            authorRepository.deleteById(id);
        }
        return "redirect:/authorlist";
    }
}
