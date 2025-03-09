package org.omegat.documentation

import com.xmlmind.util.StringUtil
import groovy.transform.CompileStatic
import org.gradle.api.file.Directory
import org.gradle.api.file.FileTree
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import com.xmlmind.whc.Compiler
import org.gradle.api.tasks.options.Option

@CompileStatic
class WhcTask extends AbstractDocumentTask {

    @InputFile
    final Provider<RegularFile> inputFile = project.objects.fileProperty()

    @InputFiles
    final Provider<FileTree> contentFiles = project.objects.property(FileTree)

    @InputFile
    final Provider<RegularFile> tocFile = project.objects.fileProperty()

    @InputFile
    final Provider<RegularFile> headerFile = project.objects.fileProperty()

    @OutputDirectory
    final Provider<Directory> outputDirectory = project.objects.directoryProperty()

    @Input
    @Option
    ListProperty<String> parameterList = project.objects.listProperty(String)

    @Input
    @Option
    Property<String> documentLayout = project.objects.property(String)

    @Input
    @Option
    Property<Boolean> localJQuery = project.objects.property(Boolean)


    @TaskAction
    void transform() {
        configureLogging()

        Compiler compiler = new Compiler(null)
        compiler.setVerbose(true)
        compiler.setUserHeader(headerFile.get().asFile.toPath().toUri().toURL())
        if (documentLayout.present) {
            compiler.setLayout(documentLayout.get())
        }
        if (localJQuery.present) {
            compiler.setLocalJQuery(localJQuery.get())
        }
        File[] contents = contentFiles.get().getFiles().toArray(new File[0])
        if (parameterList.get().size() > 1) {
            compiler.parseParameters((String[])parameterList.get().toArray(StringUtil.EMPTY_LIST))
        }
        compiler.compile(contents, tocFile.get().asFile, inputFile.get().asFile, outputDirectory.get().asFile)
    }

}
