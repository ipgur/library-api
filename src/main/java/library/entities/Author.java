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
package library.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Entity class representing author
 */
@Entity
@Table(name = "authors")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Author implements Serializable {
    /**
     * version of class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * id of the author
     */
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * name of the author
     */
    @NotNull
    private String name;

    /**
     * birthplace
     */
    private String birthplace;

    /**
     * short bio
     */
    private String description;
}
