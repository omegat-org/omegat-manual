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
import org.apache.xerces.jaxp.SAXParserFactoryImpl
import org.apache.xml.resolver.tools.CatalogResolver
import org.gradle.api.file.*
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.xml.sax.InputSource
import org.xml.sax.XMLReader

import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import java.nio.file.Files

// Parts derived from https://github.com/spring-projects/spring-build-gradle

@CompileStatic
class Docbook extends AbstractTransformationTask {

    @InputDirectory
    DirectoryProperty styleDir = project.objects.directoryProperty()

    @InputFile
    Provider<RegularFile> styleSheetFile = project.objects.fileProperty()

    @OutputFile
    Provider<RegularFile> mainOutputFile = project.objects.fileProperty()

    @InputFile
    Provider<RegularFile> inputFile = project.objects.fileProperty()

    Docbook() {
    }

    @Override
    void configureWith(DocConfigExtension extension) {
        super.configureWith(extension)
        styleDir.convention(extension.styleDir)
    }

    @TaskAction
    final void transform() {
        configureLogging()

        System.setProperty("javax.xml.transform.TransformerFactory", "org.apache.xalan.processor.TransformerFactoryImpl")
        File outputFile = mainOutputFile.get().asFile
        outputFile.parentFile.mkdirs()

        InputSource inputSource = new InputSource(new InputStreamReader(Files.newInputStream(inputFile.get().asFile.toPath())))
        StreamResult outputResult = new StreamResult(outputFile)

        CatalogResolver resolver = new CatalogResolver(XsltHelper.createCatalogManager())
        XMLReader xmlReader = initializeXmlReader(resolver)

        Transformer transformer = initializeTransformer(resolver, styleSheetFile.get().asFile)

        preTransform(transformer, inputFile.get().asFile, outputFile)
        transformer.transform(new SAXSource(xmlReader, inputSource), outputResult)
        postTransform(outputFile)
    }

    void configureLogging() {
        // Redirect spurious DocBook task output to INFO unless explicitly configured for debug
        switch (project.gradle.startParameter.logLevel) {
            case LogLevel.DEBUG:
                break
            default:
                logging.captureStandardOutput(LogLevel.INFO)
                logging.captureStandardError(LogLevel.INFO)
        }
    }

    XMLReader initializeXmlReader(CatalogResolver resolver) {
        def factory = new SAXParserFactoryImpl()
        factory.setXIncludeAware(true)
        def xmlReader = factory.newSAXParser().getXMLReader()
        xmlReader.setEntityResolver(resolver)
        return xmlReader
    }

    Transformer initializeTransformer(CatalogResolver resolver, File styleSheetFile) {
        def transformerFactory = TransformerFactory.newInstance()
        transformerFactory.setURIResolver(resolver)

        def styleSheetUrl = styleSheetFile.toURI().toURL()
        def styleSource = new StreamSource(styleSheetUrl.openStream(), styleSheetUrl.toExternalForm())
        return transformerFactory.newTransformer(styleSource)
    }

    protected void preTransform(Transformer transformer, File sourceFile, File outputFile) {
    }

    protected void postTransform(File outputFile) {
    }

}
