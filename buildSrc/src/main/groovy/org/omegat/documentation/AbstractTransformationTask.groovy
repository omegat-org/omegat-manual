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
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option

// Parts derived from https://github.com/spring-projects/spring-build-gradle

@CompileStatic
abstract class AbstractTransformationTask extends DefaultTask implements DocConfigurable {

    @Input
    String extension

    /**
     * Output type name, used for generating the output folder
     */
    @Input
    final Property<String> outputTypeName = project.objects.property(String)

    /**
     * Language of the documentation (eg 'de', 'ru').
     */
    @Input
    @Optional
    @Option(option = 'language', description = 'The two letter language code for output')
    final Property<String> language = project.objects.property(String)

    /**
     * ID of the (sub-)document to render
     */
    @Input
    @Optional
    @Option(option = "docId", description = 'The document id of the (sub)-document to generate (eg nullguide)')
    final Property<String> docId = project.objects.property(String)

    /**
     * The filename (without extension) of the resulting document. Defaults to the docId if specified, otherwise the
     * setName.
     */
    @Input
    @Optional
    @Option(option = "docName", description = 'The filename (without extension) of the resulting document. Defaults to the docId, or index.')
    final Property<String> docName = project.objects.property(String)

    /**
     * Sources root for the documentation
     */
    @Internal
    final DirectoryProperty docRoot = project.objects.directoryProperty()

    @Internal
    final DirectoryProperty outputRoot = project.objects.directoryProperty()

    @Internal
    final Provider<Directory> docsOutput = outputRoot
            .dir(language.map({ value -> "${outputTypeName.get()}/${value}" }))

    AbstractTransformationTask(String extension) {
        this.extension = extension
        docName.convention(project.provider {
            if (docId.present) {
                return docId.get()
            }
            return 'index'
        })
    }

    @Override
    void configureWith(DocConfigExtension extension) {
        docRoot.convention(extension.docRoot)
        outputRoot.convention(extension.outputRoot)
    }

    @TaskAction
    abstract void transform()
}
