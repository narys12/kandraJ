package com.kandra.kandraJ.generator;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class TitleGenerator {

    public static JavaFile generateTitle(String site, String selector) {
        ClassName parser = ClassName.get("com.workit.crawl.parsing.api.common", "Parser");
        ClassName element = ClassName.get("org.jsoup.nodes", "Element");
        ClassName functions = ClassName.get("com.google.common.base", "Functions");
        ClassName jSoupElements = ClassName.get("com.workit.crawl.parsing.api.jsoup", "JSoupElements");
        TypeName implParser = ParameterizedTypeName.get(parser, WildcardTypeName.get(String.class));

        FieldSpec titleSelector = FieldSpec.builder(String.class, "TITLE_SELECTOR")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$S", selector)
                .build();

        MethodSpec apply = MethodSpec.methodBuilder("apply")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addAnnotation(Override.class)
                .addParameter(element, "element")
                .addStatement("return compose(fromElementText(), toElement()).apply(element)")
                .build();

        TypeSpec titleParser = TypeSpec.classBuilder("TitleParser")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(implParser)
                .addMethod(apply)
                .addField(titleSelector)
                .build();

        JavaFile javaFile = JavaFile.builder("com.workit.crawl." + site + ".data.specification", titleParser)
                .addStaticImport(functions, "compose")
                .addStaticImport(jSoupElements, "fromElementText", "toElement")
                .build();

        return javaFile;
    }
}
