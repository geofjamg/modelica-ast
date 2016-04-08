/*
 * Copyright 2016 Geoffroy Jamgotchian <geoffroy.jamgotchian at gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.jamgotchian.modelica;

import fr.jamgotchian.modelica.ast.StoredDefinition;

import static org.junit.Assert.*;

/**
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at gmail.com>
 */
public class ModelicaParserTest {

    @org.junit.Test
    public void parse() throws Exception {
        StoredDefinition storedDefinition = ModelicaParser.parse(getClass().getResourceAsStream("/FirstOrder.mo"));
        storedDefinition.accept(new ModelicaPrinter(System.out), null);
    }

}