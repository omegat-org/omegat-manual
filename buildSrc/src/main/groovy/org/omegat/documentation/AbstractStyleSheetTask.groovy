package org.omegat.documentation

import com.icl.saxon.ExtendedInputSource
import com.icl.saxon.ParameterSet
import com.icl.saxon.StyleSheet
import com.icl.saxon.TransformerFactoryImpl
import com.icl.saxon.expr.StringValue
import com.icl.saxon.om.NamePool
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.file.CopySpec
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

import javax.xml.transform.Templates
import javax.xml.transform.sax.SAXSource

@CompileStatic
abstract class AbstractStyleSheetTask extends DefaultTask implements DocConfigurable {

    /**
     * Language of the documentation (eg 'de', 'ru').
     */
    @Input
    @Optional
    @Option(option = 'language', description = 'The two letter language code for output')
    final Property<String> language = project.objects.property(String)

    @InputDirectory
    final DirectoryProperty styleDir = project.objects.directoryProperty()
    /**
     * Sources root for the documentation
     */
    @Internal
    final DirectoryProperty docRoot = project.objects.directoryProperty()

    @Internal
    final DirectoryProperty outputRoot = project.objects.directoryProperty()

    @Internal
    final Provider<Directory> docsOutput = outputRoot
            .dir(language.map({ value -> "html/${value}" }))


    @Override
    void configureWith(DocConfigExtension extension) {
        docRoot.convention(extension.docRoot)
        outputRoot.convention(extension.outputRoot)
        styleDir.convention(extension.styleDir)
    }

    @TaskAction
    abstract void transform()

    protected NamePool namePool = NamePool.getDefaultNamePool()
    protected ParameterSet params = new ParameterSet()

    void setParameter(String name, String value) {
        int argcode = namePool.allocate("", "", name);
        params.put(argcode, new StringValue(value));
    }

    void execute(File sourceFile, File sheetFile, File outputFile) {
        def factory = new TransformerFactoryImpl()

        ExtendedInputSource eis = new ExtendedInputSource(sourceFile)
        def sourceInput = new SAXSource(factory.getSourceParser(), eis)
        eis.setEstimatedLength((int)sourceFile.length())

        ExtendedInputSource eis2 = new ExtendedInputSource(sheetFile)
        def styleSource = new SAXSource(factory.getStyleParser(), eis2)
        Templates sheet = factory.newTemplates(styleSource)

        StyleSheet styleSheet = new StyleSheet()
        styleSheet.processFile(sourceInput, sheet, outputFile, params)
    }

}
