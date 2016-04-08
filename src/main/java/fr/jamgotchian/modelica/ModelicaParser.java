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

import fr.jamgotchian.modelica.ast.Name;
import fr.jamgotchian.modelica.ast.Node;
import fr.jamgotchian.modelica.ast.StoredDefinition;
import fr.jamgotchian.modelica.parser.ModelicaBaseVisitor;
import fr.jamgotchian.modelica.parser.ModelicaLexer;
import fr.jamgotchian.modelica.parser.ModelicaParser.Class_definitionContext;
import fr.jamgotchian.modelica.parser.ModelicaParser.NameContext;
import fr.jamgotchian.modelica.parser.ModelicaParser.Stored_definitionContext;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at gmail.com>
 */
public class ModelicaParser {

    public static StoredDefinition parse(Path file) throws IOException {
        try (InputStream is = Files.newInputStream(file)) {
            return parse(is);
        }
    }

    public static StoredDefinition parse(InputStream is) throws IOException {
        ModelicaLexer lexer = new ModelicaLexer(new ANTLRInputStream(is));

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        fr.jamgotchian.modelica.parser.ModelicaParser parser = new fr.jamgotchian.modelica.parser.ModelicaParser(tokens);
        parser.setTrace(false);

        return (StoredDefinition) new ModelicaBaseVisitor<Node>() {

            @Override
            public Node visitName(NameContext ctx) {
                StringBuilder name = new StringBuilder();
                for (Iterator<TerminalNode> it = ctx.IDENT().iterator(); it.hasNext();) {
                    name.append(it.next().getText());
                    if (it.hasNext()) {
                        name.append(".");
                    }
                }
                return new Name(name.toString());
            }

            @Override
            public Node visitStored_definition(Stored_definitionContext ctx) {
                Name name = (Name) ctx.name().accept(this);
                for (Class_definitionContext cdctx : ctx.class_definition()) {
                    cdctx.accept(this);
                }
                return new StoredDefinition(name);
            }

            @Override
            public Node visitClass_definition(Class_definitionContext ctx) {
                return super.visitClass_definition(ctx);
            }

        }.visit(parser.stored_definition());
    }

}
