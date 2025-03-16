package com.aznos.generators

import com.aznos.util.AssetFetcher
import com.aznos.util.CodeGenerator
import com.palantir.javapoet.FieldSpec
import com.palantir.javapoet.JavaFile
import com.palantir.javapoet.MethodSpec
import com.palantir.javapoet.ParameterSpec
import com.palantir.javapoet.TypeSpec
import org.jetbrains.annotations.NotNull
import javax.lang.model.element.Modifier

class DamageTypeGenerator : CodeGenerator() {
    override fun generate() {
        val enum = TypeSpec.enumBuilder("DamageTypes").addModifiers(Modifier.PUBLIC)

        enum.addField(FieldSpec.builder(String::class.java, "messageId", Modifier.PUBLIC, Modifier.FINAL).build())
        enum.addMethod(MethodSpec.constructorBuilder()
            .addParameter(ParameterSpec.builder(String::class.java, "messageId").addAnnotation(NotNull::class.java).build())
            .addStatement("this.messageId = messageId")
            .build())

        for (entry in AssetFetcher.fetchChildrenJsonFiles("data/minecraft/damage_type")) {
            enum.addEnumConstant(entry.key.uppercase(), TypeSpec.anonymousClassBuilder("\$S", entry.key).build())
        }

        writeClass(JavaFile.builder("com.aznos.data", enum.build()).build())
    }
}