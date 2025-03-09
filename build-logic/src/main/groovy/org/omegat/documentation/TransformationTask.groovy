/*
 * Copyright 2002-2025 the original author or authors.
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
import org.apache.xml.resolver.tools.CatalogResolver
import org.gradle.api.file.*
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.xml.sax.InputSource
import org.xml.sax.XMLReader

import javax.xml.transform.Transformer
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.stream.StreamResult

@CompileStatic
class TransformationTask extends AbstractTransformationTask {

    @InputFile
    Provider<RegularFile> styleSheetFile = project.objects.fileProperty()

    @OutputFile
    Provider<RegularFile> outputFile = project.objects.fileProperty()

    @InputFile
    final Property<RegularFile> inputFile = project.objects.fileProperty()

    TransformationTask() {
    }

    @Override
    void configureWith(DocConfigExtension extension) {
        super.configureWith(extension)
    }

    @TaskAction
    final void transform() {
        configureLogging()
        configureTransformerFactory()

        File outputFile = outputFile.get().asFile

        InputSource inputSource = new InputSource(inputFile.get().asFile.getAbsolutePath())
        StreamResult outputResult = new StreamResult(outputFile)

        CatalogResolver resolver = new CatalogResolver(XsltHelper.createCatalogManager())
        XMLReader xmlReader = initializeXmlReader(resolver)
        Transformer transformer = initializeTransformer(resolver, styleSheetFile.get().asFile)

        preTransform(transformer, inputFile.get().asFile, outputFile)
        transformer.transform(new SAXSource(xmlReader, inputSource), outputResult)
        postTransform(outputFile)
    }

    protected void preTransform(Transformer transformer, File sourceFile, File outputFile) {
    }

    protected void postTransform(File outputFile) {
    }

}
