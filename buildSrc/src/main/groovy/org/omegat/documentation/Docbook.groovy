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
import net.sf.saxon.TransformerFactoryImpl
import org.apache.xerces.jaxp.SAXParserFactoryImpl
import org.apache.xml.resolver.tools.CatalogResolver
import org.gradle.api.file.*
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.*
import org.xml.sax.InputSource

import javax.xml.transform.Transformer
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

import static org.gradle.api.file.DuplicatesStrategy.EXCLUDE

// Parts derived from https://github.com/spring-projects/spring-build-gradle

@CompileStatic
class Docbook extends AbstractTransformationTask {

    /**
     * Directory containing the XSLT files
     */
    @InputDirectory
    final DirectoryProperty styleDir = project.objects.directoryProperty()

    /**
     * XSLT filename.
     */
    @InputFile
    final Provider<RegularFile> styleSheetFile = styleDir.file("docbook-utf8.xsl")

    @InputFiles
    final Provider<ConfigurableFileTree> imageSource = project.provider {
        project.fileTree(docRoot.dir('images'))
    }

    @Input
    final SetProperty<String> imageExcludes = project.objects.setProperty(String)

    @Input
    @Optional
    final Property<FileCollection> extraFilesToOutput = project.objects.property(FileCollection)

    Docbook() {
        this('html')
    }

    Docbook(String extension) {
        super(extension)
    }

    @Override
    void configureWith(DocConfigExtension extension) {
        super.configureWith(extension)
        styleDir.convention(extension.styleDir)
    }

    @TaskAction
    final void transform() {
        // the docbook tasks issue spurious content to the console. redirect to INFO level
        // so it doesn't show up in the default log level of LIFECYCLE unless the user has
        // run gradle with '-d' or '-i' switches -- in that case show them everything
        switch (project.gradle.startParameter.logLevel) {
            case LogLevel.DEBUG:
            case LogLevel.INFO:
                break
            default:
                logging.captureStandardOutput(LogLevel.INFO)
                logging.captureStandardError(LogLevel.INFO)
        }

        docsOutput.get().asFile.mkdirs()

        def setNameValue = setName.get()
        def srcFile = docRoot.get().file("${setNameValue}/${setNameValue}.xml").asFile
        def inputSource = new InputSource(srcFile.getAbsolutePath())

        def outputFile = mainOutputFile.get().asFile
        def result = new StreamResult(outputFile)

        def resolver = new CatalogResolver(XsltHelper.createCatalogManager())

        def factory = new SAXParserFactoryImpl()
        factory.setXIncludeAware(true)

        def reader = factory.newSAXParser().getXMLReader()
        reader.setEntityResolver(resolver)

        def transformerFactory = new TransformerFactoryImpl()
        transformerFactory.setURIResolver(resolver)
        def url = styleSheetFile.get().asFile.toURI().toURL()
        def source = new StreamSource(url.openStream(), url.toExternalForm())
        def transformer = transformerFactory.newTransformer(source)

        preTransform(transformer, srcFile, outputFile)

        transformer.transform(new SAXSource(reader, inputSource), result)

        postTransform(outputFile)
    }

    protected void preTransform(Transformer transformer, File sourceFile, File outputFile) {
        copyImages()
        copyExtraFiles()
        if (docId.isPresent()) {
            // rootid is used in the stylesheet to represent the root of the document
            transformer.setParameter("rootid", docId.get())
        }
    }

    protected void postTransform(File outputFile) {
    }

    private void copyImages() {
        project.copy { CopySpec copySpec ->
            copySpec.from imageSource.get()
            copySpec.exclude imageExcludes.get()
            copySpec.duplicatesStrategy = EXCLUDE
            copySpec.into docsOutput.get().dir('images')
        }
    }

    private void copyExtraFiles() {
        if (extraFilesToOutput.present) {
            project.copy { CopySpec copySpec ->
                copySpec.from extraFilesToOutput.get()
                copySpec.into docsOutput.get()
            }
        }
    }

}
