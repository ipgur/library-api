/*
 * Copyright 2019 igur.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.strumski.library.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Entity class representing Book
 */
@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Book implements Serializable {

    public Book(String title) {
        this.title = title;
    }
    /**
     * version of class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * title of the book
     */
    private String title;

    /**
     * description of the book
     */
    private String description;

    /**
     * id of the author
     */
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * author of the book
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private Author author;

    /**
     * number of times the book was read
     */
    @JsonProperty("number_read")
    @Column(name="number_read")
    private int numberRead;

    /**
     * number of times the book was opened
     */
    @JsonProperty("number_opened")
    @Column(name="number_opened")
    private int numberOpened;

}
