/*
 * Copyright 2002-2018 the original author or authors.
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
package org.omegat.documentation

import groovy.transform.CompileStatic

import javax.xml.transform.Transformer

// Parts derived from https://github.com/spring-projects/spring-build-gradle

@CompileStatic
class DocbookHtml extends Docbook {

    DocbookHtml() {
        super('html')
    }

    @Override
    protected void preTransform(Transformer transformer, File sourceFile, File outputFile) {
        super.preTransform(transformer, sourceFile, outputFile)
        transformer.setParameter("root.filename", docId.orElse(setName).get())
        transformer.setParameter("base.dir", outputFile.getParent() + File.separator)
    }
}
