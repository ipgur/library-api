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
package com.strumski.library.secrets;

import java.util.List;
import java.util.function.BiConsumer;

public interface SecretProvider {
    /**
     *
     * @param keys to find a secret for
     * @param keyCallback callback for the returned (key, secret) for each key
     */
    void fetchSecrets(List<String> keys, BiConsumer<String, String> keyCallback);
}
