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

import net.sf.saxon.TransformerFactoryImpl
import org.apache.xerces.jaxp.SAXParserFactoryImpl
import org.apache.xml.resolver.tools.CatalogResolver
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.xml.sax.InputSource

import javax.xml.transform.sax.SAXSource
import javax.xml.transform.stream.StreamResult

// Parts derived from https://github.com/spring-projects/spring-build-gradle

/**
 * Task to apply a XSL 1 stylesheet to an input file
 */
class XsltTask extends DefaultTask {

    @InputFile
    final RegularFileProperty inputFile = project.objects.fileProperty()

    /**
     * Absolute or relative URL of a stylesheet. Relative URLs are resolved against {@code stylesheetBase}
     */
    @Input
    final Property<String> styleSheetUri = project.objects.property(String)

    /**
     * URL for resolving relative stylesheets
     */
    @Input
    @Optional
    final Property<String> stylesheetBase = project.objects.property(String)

    @OutputFile
    final RegularFileProperty outputFile = project.objects.fileProperty()

    @TaskAction
    final void transform() {
        switch (project.gradle.startParameter.logLevel) {
            case LogLevel.DEBUG:
            case LogLevel.INFO:
                break
            default:
                logging.captureStandardOutput(LogLevel.INFO)
                logging.captureStandardError(LogLevel.INFO)
        }

        def srcFile = inputFile.get().asFile
        def inputSource = new InputSource(srcFile.getAbsolutePath())

        def outputFile = outputFile.get().asFile
        def result = new StreamResult(outputFile)

        def resolver = new CatalogResolver(org.firebirdsql.documentation.XsltHelper.createCatalogManager())

        def factory = new SAXParserFactoryImpl()
        factory.setXIncludeAware(true)
        def reader = factory.newSAXParser().getXMLReader()
        reader.setEntityResolver(resolver)
        
        def transformerFactory = new TransformerFactoryImpl()
        transformerFactory.setURIResolver(resolver)
        def stylesheetSource = resolver.resolve(styleSheetUri.get(), stylesheetBase.getOrNull())
        def transformer = transformerFactory.newTransformer(stylesheetSource)

        transformer.transform(new SAXSource(reader, inputSource), result)
    }

}
