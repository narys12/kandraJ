package com.kandra.kandraJ.generator;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

public class ImageGenerator {

    public static JavaFile generaImage(String site, String selector, String attribute) {
        ClassName parser = ClassName.get("com.workit.crawl.parsing.api.common", "Parser");
        ClassName element = ClassName.get("org.jsoup.nodes", "Element");
        ClassName functions = ClassName.get("com.google.common.base", "Functions");
        ClassName jSoupElements = ClassName.get("com.workit.crawl.parsing.api.jsoup", "JSoupElements");
        ClassName jSoupAttribute = ClassName.get("com.workit.crawl.parsing.api.jsoup", "JSoupAttributes");
        TypeName implParser = ParameterizedTypeName.get(parser, WildcardTypeName.get(String.class));

        FieldSpec imageSelector = FieldSpec.builder(String.class, "IMAGE_SELECTOR")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$S", selector)
                .build();

        FieldSpec imageAttribute = FieldSpec.builder(String.class, "IMAGE_ATTRIBUTE")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$S", attribute)
                .build();

        MethodSpec apply = MethodSpec.methodBuilder("apply")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addAnnotation(Override.class)
                .addParameter(element, "element")
                .addStatement("return compose(fromAttribute($S), toElement()).apply(element)", attribute)
                .build();

        TypeSpec imageParser = TypeSpec.classBuilder("ImageParser")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(implParser)
                .addMethod(apply)
                .addField(imageSelector)
                .addField(imageAttribute)
                .build();

        JavaFile javaFile = JavaFile.builder("com.workit.crawl." + site + ".data.specification", imageParser)
                .addStaticImport(functions, "compose")
                .addStaticImport(jSoupElements, "toElement")
                .addStaticImport(jSoupAttribute, "fromAttribute")
                .build();

        return javaFile;
    }
}
