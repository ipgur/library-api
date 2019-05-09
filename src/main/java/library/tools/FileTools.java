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
package library.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Provides some basic file tools
 */
public class FileTools {

    /**
     * @param path to the file to be read
     * @return the content of the file as a string
     * @throws IOException
     */
    public static String readResourceFile(final String path) throws IOException {
       ClassLoader classLoader = FileTools.class.getClassLoader();
       File file = new File(Objects.requireNonNull(classLoader.getResource(path)).getFile());
       return new String(Files.readAllBytes(file.toPath()));
    }
}
