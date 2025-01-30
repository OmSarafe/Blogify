package com.blogify.entry.enteties;

import com.blogify.entry.configuration.AuditingConfiguration;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="posts")
@NamedEntityGraph(name = "Post.tags",attributeNodes = @NamedAttributeNode("tags"))
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "posts_tags",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<Tag> tags = new HashSet<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createddate;

    @LastModifiedDate
    private LocalDateTime lastmodifieddate;
}