package com.blogify.entry.controllers;

import com.blogify.entry.dtos.tagDtos.TagRequestDtos;
import com.blogify.entry.dtos.tagDtos.TagResponseDtos;
import com.blogify.entry.enteties.Tag;
import com.blogify.entry.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    TagService tagService;

    @GetMapping()
    public ResponseEntity<List<TagResponseDtos>>getAllTags(){
        return new ResponseEntity<>(tagService.getAll().stream().map(tag ->
                tagService.converttoTagResponseDtos(tag)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<TagResponseDtos>createTag(@RequestBody TagRequestDtos tagRequestDtos){
        Tag tag=tagService.covertToTag(tagRequestDtos);
        Tag createdtag=tagService.create(tag);
        return new ResponseEntity<TagResponseDtos>(tagService.converttoTagResponseDtos(createdtag),HttpStatus.CREATED);
    }

    @PutMapping("/{tagname}")
    public ResponseEntity<TagResponseDtos>updatetag(@PathVariable String tagname,@RequestBody TagRequestDtos tagRequestDtos){
        Tag tag = tagService.covertToTag(tagRequestDtos);
        Tag updatedtag = tagService.update(tagname, tag);
        return  new ResponseEntity<>(tagService.converttoTagResponseDtos(updatedtag),HttpStatus.OK);
    }

//    @DeleteMapping("/{tagname}")
//    public ResponseEntity<Void> deleteTag(@PathVariable String tagname){
//        tagService.delete(tagname);
//        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag2(@PathVariable int id){
        tagService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
