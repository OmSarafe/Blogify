package com.blogify.entry.services;

import com.blogify.entry.dtos.tagDtos.TagRequestDtos;
import com.blogify.entry.dtos.tagDtos.TagResponseDtos;
import com.blogify.entry.enteties.Tag;
import com.blogify.entry.repositories.PostRepositories;
import com.blogify.entry.repositories.TagsRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TagService {
    @Autowired
    TagsRepositories tagsRepositories;

    @Autowired
    PostRepositories postRepositories;

    public List<Tag>getAll(){
        return tagsRepositories.findAll();
    }

    public Tag create(Tag tag){
        if (tagsRepositories.findByName(tag.getName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"tag with name:" +tag.getName()+"already exists you cannot create duplicate tags");
        }
        return tagsRepositories.save(tag);
    }

    public Tag update(String oldTagName,Tag tag){
        String newTagName=tag.getName();
        if (oldTagName.equals(newTagName)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"you cannot update the same name");
        }
        Tag existingTag=tagsRepositories.findByName(oldTagName).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"tag with name: "+oldTagName+" not found"));
        if (tagsRepositories.findByName(newTagName).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"you cannot update from "+oldTagName+" to "+newTagName+" because "+newTagName+" already exists");
        }
        existingTag.setName(newTagName);
        return tagsRepositories.save(existingTag);
    }

//    public void delete(String tagName){
//        Tag tag=tagsRepositories.findByName(tagName).orElseThrow(()->
//                new ResponseStatusException(HttpStatus.NOT_FOUND,
//                        "tag with name: "+tagName+" not found"));
//        if (!postRepositories.findByTagsName(tagName).isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
//                    "you cannot delete tag name "+tagName+" because there are references in some posts");
//        }
//        tagsRepositories.delete(tag);
//    }

    public  void delete(int id){
        Tag tag = tagsRepositories.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "tag with id " + id + " not found"));
        if (!postRepositories.findByTagsId(id).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "you cannot delete tag with id "+id+" because there are references in some posts");
        }
        tagsRepositories.delete(tag);
    }

    public TagResponseDtos converttoTagResponseDtos(Tag tag){
        return new TagResponseDtos(tag.getId(),tag.getName());
    }

    public Tag covertToTag(TagRequestDtos tagRequestDtos){
        return new Tag(0,tagRequestDtos.getName());
    }
}
