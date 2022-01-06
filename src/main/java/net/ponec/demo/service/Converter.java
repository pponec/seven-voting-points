/*
 * Copyright 2020-2021 Pavel Ponec, https://github.com/pponec
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

package net.ponec.demo.service;


/**
 *
 * @author Pavel Ponec
 */
public class Converter {

    public int id2index(int id) {
        switch (id) {
            case -2: return 0;
            case -1: return 1;
            case 1: return 2;
            case 2: return 3;
            default: throw new IllegalArgumentException("Unsupported id: " + id);
        }
    }

    public int index2id(int idx) {
        switch (idx) {
            case 0: return -2;
            case 1: return -1;
            case 2: return 1;
            case 3: return 2;
            default: throw new IllegalArgumentException("Unsupported id: " + idx);
        }
    }

}
